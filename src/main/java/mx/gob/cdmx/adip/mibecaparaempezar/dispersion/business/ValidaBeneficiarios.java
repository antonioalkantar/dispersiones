package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionReporteDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSolicitudTutorDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatEstatusDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMontoApoyoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.ResultadoEjecucionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.environment.Environment;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading.ContadorProgresoSynchronized;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading.ValidaBeneficiarioCallable;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.Constantes;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.CsvUtils;

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
				List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios = null;
				List<BeneficiarioSolicitudTutorDTO> lstBeneficiariosNoDispersados = null;
				
				if(dispersionDTO.getCatTipoDispersion().getIdTipoDispersion() == Constantes.ID_TIPO_DISPERSION_COMPLEMENTARIA) {
					// 3.1 Se obtiene la ultima dispersion para procesar sus beneficiarios no dispersados
					DispersionDTO ultimaDispersion = dispersionDAO.obtenerUltimaDispersionPorFechaConclusion(dispersionDTO);
					// 3.2 Se obtienen los registros de beneficiarios
					lstBeneficiarios = beneficiarioDAO.buscarBeneficiariosActivosComplementaria(ultimaDispersion.getIdDispersion());
					LOGGER.info("Total Registros a procesar:" + lstBeneficiarios.size());
				} else {
					// 3.2 Se obtienen los registros de beneficiarios
					lstBeneficiarios = beneficiarioDAO.buscarBeneficiariosActivos();
					// 3.3 Se obtiene la ultima dispersion para procesar sus beneficiarios no dispersados
					DispersionDTO ultimaDispersion = dispersionDAO.obtenerUltimaDispersionPorFechaConclusion(dispersionDTO);
					if(ultimaDispersion != null) {
						// 3.4 Se obtienen los registros de beneficiarios
						lstBeneficiariosNoDispersados = beneficiarioDAO.buscarBeneficiariosActivosComplementaria(ultimaDispersion.getIdDispersion());
					}
					LOGGER.info("Total Registros a procesar:" + lstBeneficiarios.size());
				}

				ContadorProgresoSynchronized.reset();
				ContadorProgresoSynchronized.setMeta(lstBeneficiarios.size());

				// 4. Se divide la carga del archivos en hilos de ejecución (Callables)
				LOGGER.info("Preparando los hilos de ejecución...");
				List<Callable<ResultadoEjecucionDTO>> lstHilosBeneficiarios = dividirCarga(dispersionDTO, lstBeneficiarios, lstCatMontoApoyo, beneficiarioDispersionDAO, beneficiarioSinDispersionDAO, dispersionDAO);
				LOGGER.info("Se prepararon " + lstHilosBeneficiarios.size() + " hilos de ejecución.");

				// 5. Se invocan los diversos hilos de ejecución para realizar los inserts de
				// manera paralela
				LOGGER.info("Procesadores disponibles para multithreading: " + Runtime.getRuntime().availableProcessors());
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
				
				// 7. Se divide la carga del archivos en hilos de ejecución (Callables)
				LOGGER.info("Preparando los hilos de ejecución...");
				if(lstBeneficiariosNoDispersados != null && lstBeneficiariosNoDispersados.size() > 0) {
					List<Callable<ResultadoEjecucionDTO>> lstHilosBeneficiariosNoDispersados = dividirCarga(dispersionDTO, lstBeneficiarios, lstCatMontoApoyo, beneficiarioDispersionDAO, beneficiarioSinDispersionDAO, dispersionDAO);
					LOGGER.info("Se prepararon " + lstHilosBeneficiariosNoDispersados.size() + " hilos de ejecución.");
				}

				LOGGER.info("*********************************************************************");
				LOGGER.info("************ RESULTADOS ARCHIVO_ID: " + dispersionDTO.getIdDispersion() + " *************************");
				LOGGER.info("************ Total registros procesados  :" + intTotalRegistrosProcesados + " **************");
				LOGGER.info("************ Total registros dispersados   :" + intTotalRegistrosDispersados + " **************");
				LOGGER.info("************ Total registros no dispersados :" + intTotalRegistrosNoDispersados + " **************");
				LOGGER.info("*********************************************************************");

				// 8. Actualizar datos generales de la dispersion(Num de registros, dispersados, No dispersados)
				if (intTotalRegistrosProcesados != lstBeneficiarios.size()) {
					LOGGER.warn("No coincidió el número de registros procesados vs el número de registros obtenidos de la BD");
				}

				dispersionDAO.actualizarContadores(dispersionDTO, porcentajeDispersados, (int) intTotalRegistrosDispersados, porcentajeNoDispersados, (int) intTotalRegistrosNoDispersados);

				// 9. Lista para reportes CSV con Dispersion
				Map<String, List<BeneficiarioDispersionReporteDTO>> mapaReportesNivelAcademicoConDispersion = new HashMap<>();
				mapaReportesNivelAcademicoConDispersion.put("preescolar", new ArrayList<>());
				mapaReportesNivelAcademicoConDispersion.put("primaria", new ArrayList<>());
				mapaReportesNivelAcademicoConDispersion.put("secundaria", new ArrayList<>());
				mapaReportesNivelAcademicoConDispersion.put("laboral", new ArrayList<>());
				
				List<BeneficiarioDispersionReporteDTO> lstBeneficiariosDispersionReporte = beneficiarioDAO.consultarBeneficiariosDispersadosPorIdDispersion(dispersionDTO);
				for (BeneficiarioDispersionReporteDTO beneficiarioDispersion : lstBeneficiariosDispersionReporte) {
					asignarListaReportesDispersion(beneficiarioDispersion, mapaReportesNivelAcademicoConDispersion);
				}
				
				// 10. Generamos los CSV
				crearReportesCSV(dispersionDTO, mapaReportesNivelAcademicoConDispersion);
				
				// 11. Se actualiza el estatus de la dispersion a Concluido y se coloca la fechaConclusion del proceso
				dispersionDAO.actualizarEstatus(dispersionDTO.getIdDispersion(), Constantes.ID_ESTATUS_DISPERSION_CONCLUIDO);
				
				if(dispersionDTO.getCatTipoDispersion().getIdTipoDispersion() == Constantes.ID_TIPO_DISPERSION_COMPLEMENTARIA) {
				// 12. Se actualiza la bandera para solo mostrar icono de ejecutar validacion al ultimo registro
					dispersionDAO.actualizarPermiteEjecucion(dispersionDTO);
				}
				// 13. Se actualiza la fecha Concluido del proceso
				dispersionDAO.actualizarFechaConcluido(dispersionDTO.getIdDispersion(), new Date());
			} catch (Exception e) {
				LOGGER.error("Ocurrió un error al procesar la dispersión con ID:" + dispersionDTO.getIdDispersion() + ":", e);
				// En caso de algún error en el archivo, se cambia su estatus a "Error"
				// dispersionDAO.actualizarEstatus(dispersionDTO.getIdDispersion(),
				// CatEstatusDispersionDTO.ID_ESTATUS_DISPERSION_ERROR);
			} finally {
				executor.shutdown(); // Al final se cierra el ExecutorService
			}
		}
	}
	
	private void asignarListaReportesDispersion(BeneficiarioDispersionReporteDTO beneficiarioDispersion, Map<String, List<BeneficiarioDispersionReporteDTO>> mapaReportesNivelAcademicoConDispersion) {
		switch (beneficiarioDispersion.getIdNivelEducativo()) {
		case Constantes.ID_PREESCOLAR:
			mapaReportesNivelAcademicoConDispersion.get("preescolar").add(beneficiarioDispersion);
			break;
		case Constantes.ID_PRIMARIA:
			mapaReportesNivelAcademicoConDispersion.get("primaria").add(beneficiarioDispersion);
			break;
		case Constantes.ID_SECUNDARIA:
			mapaReportesNivelAcademicoConDispersion.get("secundaria").add(beneficiarioDispersion);
			break;
		case Constantes.ID_CAM_LABORAL:
			mapaReportesNivelAcademicoConDispersion.get("laboral").add(beneficiarioDispersion);
			break;
		case Constantes.ID_PRIMARIA_ADULTOS:
			mapaReportesNivelAcademicoConDispersion.get("primaria").add(beneficiarioDispersion);
			break;
		case Constantes.ID_SECUNDARIA_ADULTOS:
			mapaReportesNivelAcademicoConDispersion.get("secundaria").add(beneficiarioDispersion);
			break;
		case Constantes.ID_CAM_PREESCOLAR:
			mapaReportesNivelAcademicoConDispersion.get("laboral").add(beneficiarioDispersion);
			break;
		case Constantes.ID_CAM_PRIMARIA:
			mapaReportesNivelAcademicoConDispersion.get("laboral").add(beneficiarioDispersion);
			break;
		case Constantes.ID_CAM_SECUNDARIA:
			mapaReportesNivelAcademicoConDispersion.get("laboral").add(beneficiarioDispersion);
			break;
		default:
			break;
		}
	}
	
	private List<String[]> mapearDispersion(List<BeneficiarioDispersionReporteDTO> lstBeneficiariosConDispersion) {
		List<String[]> datosReporte = new ArrayList<String[]>();
		for (BeneficiarioDispersionReporteDTO registroDispersionReporte : lstBeneficiariosConDispersion) {
			datosReporte.add(new String[] { registroDispersionReporte.getCurpTutor(), registroDispersionReporte.getNumeroCuenta(), registroDispersionReporte.getMonto().toString() });
		}
		return datosReporte;
	}
	
	private synchronized void crearReportesCSV(DispersionDTO dispersion, Map<String, List<BeneficiarioDispersionReporteDTO>> mapaReportesNivelAcademicoConDispersion) {
		List<String[]> reporteDispersionPreescolar = mapearDispersion(mapaReportesNivelAcademicoConDispersion.get("preescolar"));
		List<String[]> reporteDispersionPrimaria = mapearDispersion(mapaReportesNivelAcademicoConDispersion.get("primaria"));
		List<String[]> reporteDispersionSecundaria = mapearDispersion(mapaReportesNivelAcademicoConDispersion.get("secundaria"));
		List<String[]> reporteDispersionLaboral = mapearDispersion(mapaReportesNivelAcademicoConDispersion.get("laboral"));
		CsvUtils.addDataToCSV(Environment.getPathFolderPadrones() + dispersion.getIdDispersion() + "_reporte_preescolar.csv", reporteDispersionPreescolar);
		CsvUtils.addDataToCSV(Environment.getPathFolderPadrones() + dispersion.getIdDispersion() + "_reporte_primaria.csv", reporteDispersionPrimaria);
		CsvUtils.addDataToCSV(Environment.getPathFolderPadrones() + dispersion.getIdDispersion() + "_reporte_secundaria.csv", reporteDispersionSecundaria);
		CsvUtils.addDataToCSV(Environment.getPathFolderPadrones() + dispersion.getIdDispersion() + "_reporte_laboral.csv", reporteDispersionLaboral);
		dispersionDAO.actualizarArchivos(dispersion, dispersion.getIdDispersion() + "_reporte_preescolar.csv", dispersion.getIdDispersion() + "_reporte_primaria.csv", dispersion.getIdDispersion() + "_reporte_secundaria.csv", dispersion.getIdDispersion() + "_reporte_laboral.csv");
	}

	private List<Callable<ResultadoEjecucionDTO>> dividirCarga(DispersionDTO dispersionDTO,
			List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios, List<CatMontoApoyoDTO> lstCatMontoApoyo,
			BeneficiarioDispersionDAO beneficiarioDispersionDAO,
			BeneficiarioSinDispersionDAO beneficiarioSinDispersionDAO, DispersionDAO dispersionDAO) {
		List<Callable<ResultadoEjecucionDTO>> lstThreads = new ArrayList<>();
		int tamanioSublistas = 10; // 100 registros a procesar por thread
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
