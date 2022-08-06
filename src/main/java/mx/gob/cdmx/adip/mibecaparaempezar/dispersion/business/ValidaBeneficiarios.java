package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.business;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.DispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.ResultadoEjecucionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.environment.Environment;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading.ValidaBeneficiarioCallable;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading.ContadorProgresoSynchronized;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.CsvUtils;

/**
 * @author raul
 */
public class ValidaBeneficiarios {
	
	private static final Logger LOGGER = LogManager.getLogger(ValidaBeneficiarios.class);
	
	private DispersionDAO dispersionDAO;
	
	public ValidaBeneficiarios() {
//		miDAO = new MiDAO();
	}
	
	public void validar() {
		LOGGER.info("************* INICIA PROCESO DE DISPERSIÓN ****************");
		// 1. Se obtiene de la BD las dispersiones en espera de procesar
		DispersionDTO dispersionBuscarDTO = new DispersionDTO();
//		dispersionDTO.setCatEstatusDispersionDTO(new CatEstatusDispersionDTO(CatEstatusDispersionDTO.ID_ESTATUS_DISPERSION_ESPERA_VALIDACION));
		
		List<DispersionDTO> lstDispersionesDTO = dispersionDAO.buscarPorCriterios(dispersionBuscarDTO);
		
		if(lstDispersionesDTO.size() == 0) {
			//Si no hay dispersiones pendientes de procesar se termina el proceso, se retorna al método main para que ponga en pausa la aplicación por N minutos para volver a revisar si ya hay dispersiones pendientes.
			return;
		}
		
		LOGGER.info("Número de dispersiones pendientes de procesar: "+lstDispersionesDTO.size());
		for (DispersionDTO dispersionDTO : lstDispersionesDTO) {
			//LOGGER.info("**************** Procesando id_archivo: " + dispersionDTO.getIdDispersion() + ", tipo:"+dispersionDTO.getTipoDispersion()+"  **********************");
			ExecutorService executor = null;
			try {
				// 2. Se actualiza el estatus del archivo a "validando"
				//dispersionDAO.actualizarEstatus(dispersionDTO.getIdDispersion(), CatEstatusDispersionDTO.ID_ESTATUS_DISPERSION_VALIDANDO);
				
				// 3. Se obtienen los registros del CSV
				//List<BeneficiarioDTO> lstBeneficiarios = .....
				//LOGGER.info("Total Registros a procesar:"+lstBeneficiarios.size());
				
				ContadorProgresoSynchronized.reset();
				//ContadorProgresoSynchronized.setMeta(lstBeneficiarios.size());
				
				// 4. Se divide la carga del archivos en hilos de ejecución (Callables)
				LOGGER.info("Preparando los hilos de ejecución...");
				//List<Callable<ResultadoEjecucionDTO>> lstHilosBeneficiarios = dividirCarga(dispersionDTO, lstBeneficiarios);
				//LOGGER.info("Se prepararon "+ lstHilosBeneficiarios.size() +" hilos de ejecución.");
				
				// 5. Se invocan los diversos hilos de ejecución para realizar los inserts de manera paralela
				LOGGER.info("Procesadores disponibles para multithreading: "+Runtime.getRuntime().availableProcessors());
				LOGGER.info("Invocando hilos de ejecución...");
	            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	            //List<Future<ResultadoEjecucionDTO>> lstFuturosResultados = executor.invokeAll(lstHilosInsercionBeneficiarios);
	            
	            // 6. Se obtienen los resultados
	            int intTotalRegistrosProcesados = 0;
	            int intTotalRegistrosCorrectos = 0;
	            int intTotalRegistrosIncorrectos = 0;
//	            for (Future<ResultadoEjecucionDTO> future : lstFuturosResultados) {
//					intTotalRegistrosProcesados += future.get().getTotalRegistros();
//					intTotalRegistrosCorrectos += future.get().getTotalCorrectos();
//					intTotalRegistrosIncorrectos += future.get().getTotalIncorrectos();
//				}
	            
	            LOGGER.info("*********************************************************************");
	            LOGGER.info("************ RESULTADOS ARCHIVO_ID: " + dispersionDTO.getIdDispersion() + " *************************");
	            LOGGER.info("************ Total registros procesados  :"+ intTotalRegistrosProcesados + " **************");
	            LOGGER.info("************ Total registros correctos   :"+ intTotalRegistrosCorrectos + " **************");
	            LOGGER.info("************ Total registros incorrectos :"+ intTotalRegistrosIncorrectos + " **************");
	            LOGGER.info("*********************************************************************");
	            
	            // 7. Actualizar datos generales de la dispersion(Num de registros, correctos, incorrectos) 
//	            if(intTotalRegistrosProcesados != lstBeneficiarios.size()) {
//	            	LOGGER.warn("No coincidió el número de registros procesados vs el número de registros obtenidos de la BD");
//	            }
//	            dispersionDTO.setNumRegistros(lstBeneficiarios.size());
//	            dispersionDTO.setNumCorrectos(intTotalRegistrosCorrectos);
//	            dispersionDTO.setNumIncorrectos(intTotalRegistrosIncorrectos);
	            
	            dispersionDAO.actualizarContadores(dispersionDTO);
	            
				// 8. Se actualiza el estatus del archivo a "validado"
				//dispersionDAO.actualizarEstatus(dispersionDTO.getIdDispersion(), CatEstatusDispersionDTO.ID_ESTATUS_DISPERSION_VALIDADO);
			} catch (Exception e) {
				LOGGER.error("Ocurrió un error al procesar la dispersión con ID:"+dispersionDTO.getIdDispersion() + ":", e);
				// 9. En caso de algún error en el archivo, se cambia su estatus a "Error"
				//dispersionDAO.actualizarEstatus(dispersionDTO.getIdDispersion(), CatEstatusDispersionDTO.ID_ESTATUS_DISPERSION_ERROR);
			} finally {
				executor.shutdown(); // Al final se cierra el ExecutorService
			}
		}
	}
	
	private List<Callable<ResultadoEjecucionDTO>> dividirCarga(DispersionDTO dispersionDTO, List<BeneficiarioDTO> lstBeneficiarios) {
		List<Callable<ResultadoEjecucionDTO>> lstThreads = new ArrayList<>();
		int tamanioSublistas = 100; //100 registros a procesar por thread
		List<List<BeneficiarioDTO>> lstCargaDividida = ListUtils.partition(lstBeneficiarios, tamanioSublistas);
		
		int indexInicial = 1; //Con esta variable indicamos a partir de que registro se va a procesar la info en cada Thread
		for (List<BeneficiarioDTO> listBeneficiarios : lstCargaDividida) {
			lstThreads.add(new ValidaBeneficiarioCallable(dispersionDTO, listBeneficiarios, indexInicial));
			indexInicial += 100;
		}
		return lstThreads;
	}
}
