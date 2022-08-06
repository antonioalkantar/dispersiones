package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.ResultadoEjecucionDTO;

/**
 * @author raul
 */
public class ValidaBeneficiarioCallable implements Callable<ResultadoEjecucionDTO> {

	private static final Logger LOGGER = LogManager.getLogger(ValidaBeneficiarioCallable.class);
	
	
	private List<BeneficiarioDTO> lstBeneficiarios;
	private DispersionDTO archivoPadronDTO;
	
	private int indexInicio;
	
	public ValidaBeneficiarioCallable(DispersionDTO archivoPadronDTO, List<BeneficiarioDTO> lstBeneficiarios, int indexInicio) {
		this.lstBeneficiarios = lstBeneficiarios;
		this.archivoPadronDTO = archivoPadronDTO;
		this.indexInicio = indexInicio;
	}

	@Override
	public ResultadoEjecucionDTO call() throws Exception {
		ResultadoEjecucionDTO resultadoEjecucionDTO = new ResultadoEjecucionDTO();
		
		try(Connection conn = PostgresDatasource.getInstance().getConnection();) {
			conn.setAutoCommit(false);
			

			for (BeneficiarioDTO beneficiarioDTO : lstBeneficiarios) {
				
				validaEstatusConexion(conn);
				
			}
			
			// Se insertan todos los preparedStatements
			//int[] resultadosActualizacion = null;
			conn.commit();
			conn.setAutoCommit(true);
			
			// Se suman los totales.
			resultadoEjecucionDTO.setTotalRegistros(lstBeneficiarios.size());
			//resultadoEjecucionDTO.setTotalCorrectos(resultadosActualizacion[0]); //Número de updates que actualizaron por lo menos 1 registro en la BD
			//resultadoEjecucionDTO.setTotalIncorrectos(resultadosActualizacion[1]); //Número de updates que NO actualizaron registros en la BD, es decir, que la CURP no se encontró activa en el Programa
			
			//ContadorProgresoSynchronized.incrementarAvance(resultadosActualizacion[0] + resultadosActualizacion[1]);
			
			//LOGGER.info("El hilo "+Thread.currentThread().getName()+ " terminó la ejecución de "+lstBeneficiarios.size()+" registros. Correctos: "+totalCorrectos+", Incorrectos: "+totalIncorrectos);
		} catch (Exception e) {
			LOGGER.error("Ocurrió un error en la ejecución del Thread:", e);
		} finally {
			//miDAO.closeResources();
		}
		return resultadoEjecucionDTO;
	}

	private void validaEstatusConexion(Connection conn) throws SQLException {
		if(conn.isClosed()) {
			//miDAO.closeResources();
			LOGGER.warn("Se dectectó conexión cerrada. Se renueva la conexión");
			conn = PostgresDatasource.getInstance().getConnection();
			//miDAO = new MiDAO(conn);
		}
	}
}
