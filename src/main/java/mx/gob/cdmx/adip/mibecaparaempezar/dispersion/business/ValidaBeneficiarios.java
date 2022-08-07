package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BeneficiarioDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BeneficiarioDispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BeneficiarioSinDispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.CatMontoApoyoDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.DispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSolicitudTutorDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatEstatusDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMontoApoyoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.ResultadoEjecucionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading.ContadorProgresoSynchronized;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading.ValidaBeneficiarioCallable;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.Constantes;

/**
 * @author raul
 */
public class ValidaBeneficiarios {

	private static final Logger LOGGER = LogManager.getLogger(ValidaBeneficiarios.class);

	private DispersionDAO dispersionDAO = new DispersionDAO();

	private BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO();

	private CatMontoApoyoDAO catMontoApoyoDAO = new CatMontoApoyoDAO();

	private List<CatMontoApoyoDTO> lstCatMontoApoyo = new ArrayList<>();

	private BeneficiarioDispersionDAO beneficiarioDispersionDAO = new BeneficiarioDispersionDAO();

	private BeneficiarioSinDispersionDAO beneficiarioSinDispersionDAO = new BeneficiarioSinDispersionDAO();

	public ValidaBeneficiarios() {
//		miDAO = new MiDAO();
	}

	public void validar() {

		lstCatMontoApoyo = catMontoApoyoDAO.buscarTodos();

		LOGGER.info("************* INICIA PROCESO DE DISPERSIÓN ****************");
		// 1. Se obtiene de la BD las dispersiones en espera de procesar
		DispersionDTO dispersionBuscarDTO = new DispersionDTO();
		dispersionBuscarDTO
				.setCatEstatusDispersion(new CatEstatusDispersionDTO(Constantes.ID_ESTATUS_DISPERSION_EN_PROCESO));

		List<DispersionDTO> lstDispersionesDTO = dispersionDAO.buscarPorCriterios(dispersionBuscarDTO);

		if (lstDispersionesDTO.size() == 0) {
			// Si no hay dispersiones pendientes de procesar se termina el proceso, se
			// retorna al método main para que ponga en pausa la aplicación por N minutos
			// para volver a revisar si ya hay dispersiones pendientes.
			return;
		}

		LOGGER.info("Número de dispersiones pendientes de procesar: " + lstDispersionesDTO.size());
		for (DispersionDTO dispersionDTO : lstDispersionesDTO) {
			LOGGER.info("**************** Procesando id_archivo: " + dispersionDTO.getIdDispersion() + ", tipo:"
					+ dispersionDTO.getCatTipoDispersion().getIdTipoDispersion() + "  **********************");
			ExecutorService executor = null;
			try {
				// 2. Se actualiza el estatus a procesando
				dispersionDAO.actualizarEstatus(dispersionDTO.getIdDispersion(),
						Constantes.ID_ESTATUS_DISPERSION_PROCESANDO);

				// 3. Se obtienen los registros de beneficiarios
				List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios = beneficiarioDAO.buscarBeneficiariosActivos();
				LOGGER.info("Total Registros a procesar:" + lstBeneficiarios.size());

				ContadorProgresoSynchronized.reset();
				ContadorProgresoSynchronized.setMeta(lstBeneficiarios.size());

				// 4. Se divide la carga del archivos en hilos de ejecución (Callables)
				LOGGER.info("Preparando los hilos de ejecución...");
				List<Callable<ResultadoEjecucionDTO>> lstHilosBeneficiarios = dividirCarga(dispersionDTO,
						lstBeneficiarios, lstCatMontoApoyo, beneficiarioDispersionDAO, beneficiarioSinDispersionDAO,
						dispersionDAO);
				LOGGER.info("Se prepararon " + lstHilosBeneficiarios.size() + " hilos de ejecución.");

				// 5. Se invocan los diversos hilos de ejecución para realizar los inserts de
				// manera paralela
				LOGGER.info(
						"Procesadores disponibles para multithreading: " + Runtime.getRuntime().availableProcessors());
				LOGGER.info("Invocando hilos de ejecución...");
				executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
				List<Future<ResultadoEjecucionDTO>> lstFuturosResultados = executor.invokeAll(lstHilosBeneficiarios);

				// 6. Se obtienen los resultados
				double intTotalRegistrosProcesados = 0;
				double intTotalRegistrosDispersados = 0;
				double intTotalRegistrosNoDispersados = 0;
				for (Future<ResultadoEjecucionDTO> future : lstFuturosResultados) {
					intTotalRegistrosProcesados += future.get().getTotalRegistros();
					intTotalRegistrosDispersados += future.get().getTotalDispersados();
					intTotalRegistrosNoDispersados += future.get().getTotalNoDispersados();
				}
				double porcentajeDispersados =  Math.round((intTotalRegistrosDispersados * 100) / intTotalRegistrosProcesados);
				double porcentajeNoDispersados =  Math.round((intTotalRegistrosNoDispersados * 100) / intTotalRegistrosProcesados);

				LOGGER.info("*********************************************************************");
				LOGGER.info("************ RESULTADOS ARCHIVO_ID: " + dispersionDTO.getIdDispersion() + " *************************");
				LOGGER.info("************ Total registros procesados  :" + intTotalRegistrosProcesados + " **************");
				LOGGER.info("************ Total registros dispersados   :" + intTotalRegistrosDispersados + " **************");
				LOGGER.info("************ Total registros no dispersados :" + intTotalRegistrosNoDispersados + " **************");
				LOGGER.info("*********************************************************************");

				// 7. Actualizar datos generales de la dispersion(Num de registros, dispersados, No dispersados)
				if (intTotalRegistrosProcesados != lstBeneficiarios.size()) {
					LOGGER.warn("No coincidió el número de registros procesados vs el número de registros obtenidos de la BD");
				}

				dispersionDAO.actualizarContadores(dispersionDTO, porcentajeDispersados, (int) intTotalRegistrosDispersados, porcentajeNoDispersados, (int) intTotalRegistrosNoDispersados);

				// 8. Se actualiza el estatus de la dispersion a Concluido y se coloca la fechaConclusion del proceso
				dispersionDAO.actualizarEstatus(dispersionDTO.getIdDispersion(), Constantes.ID_ESTATUS_DISPERSION_CONCLUIDO);
				dispersionDAO.actualizarFechaConcluido(dispersionDTO.getIdDispersion(), new Date());
			} catch (Exception e) {
				LOGGER.error("Ocurrió un error al procesar la dispersión con ID:" + dispersionDTO.getIdDispersion() + ":", e);
				// 9. En caso de algún error en el archivo, se cambia su estatus a "Error"
				// dispersionDAO.actualizarEstatus(dispersionDTO.getIdDispersion(),
				// CatEstatusDispersionDTO.ID_ESTATUS_DISPERSION_ERROR);
			} finally {
				executor.shutdown(); // Al final se cierra el ExecutorService
			}
		}
	}

	private List<Callable<ResultadoEjecucionDTO>> dividirCarga(DispersionDTO dispersionDTO,
			List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios, List<CatMontoApoyoDTO> lstCatMontoApoyo,
			BeneficiarioDispersionDAO beneficiarioDispersionDAO,
			BeneficiarioSinDispersionDAO beneficiarioSinDispersionDAO, DispersionDAO dispersionDAO) {
		List<Callable<ResultadoEjecucionDTO>> lstThreads = new ArrayList<>();
		int tamanioSublistas = 100; // 100 registros a procesar por thread
		List<List<BeneficiarioSolicitudTutorDTO>> lstCargaDividida = ListUtils.partition(lstBeneficiarios,
				tamanioSublistas);

		int indexInicial = 1; // Con esta variable indicamos a partir de que registro se va a procesar la info
								// en cada Thread
		for (List<BeneficiarioSolicitudTutorDTO> listBeneficiarios : lstCargaDividida) {
			lstThreads.add(new ValidaBeneficiarioCallable(dispersionDTO, listBeneficiarios, indexInicial,
					lstCatMontoApoyo, beneficiarioDispersionDAO, beneficiarioSinDispersionDAO, dispersionDAO));
			indexInicial += 100;
		}
		return lstThreads;
	}
}
