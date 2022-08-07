package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.init;

import java.nio.file.NotDirectoryException;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.business.ValidaBeneficiarios;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.environment.Environment;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.FileUtils;

/**
 * @author raul
 */
public class DispersionesInit {
	
    private static final Logger LOGGER = LogManager.getLogger(DispersionesInit.class);

	public static void main(String[] args) {
		LOGGER.info("*******************************************************************");
		LOGGER.info("*****************INICIALIZANDO APLICACIÓN***************************");
		LOGGER.info("*******************************************************************");
		
		DispersionesInit dispersionesInit = new DispersionesInit();
		dispersionesInit.mantenerAplicacionViva();
	}
	
	
	private synchronized void mantenerAplicacionViva() {
		while (true) {
			LOGGER.info("Aplicación activa " + Calendar.getInstance().getTime());
			try {
				
				iniciarProcesoValidacion();
				
				this.wait(1000 * 60 * Environment.getTimeWaitingMin()); // Se para el hilo de la aplicación por 10 minutos tras cada ejecución
			} catch (InterruptedException e) {
				LOGGER.error("Ocurrió un error al mantener la aplicación activa:", e);
			}
		}
 
	}
	
	public void iniciarProcesoValidacion() {
		LOGGER.info("*******************************************************************");
		LOGGER.info("*****************INICIA PROCESO DE VALIDACIÓN***************************");
		LOGGER.info("*******************************************************************");
		
		LOGGER.info(Environment.getAppProfile());
		LOGGER.info(Environment.getDbUrl());
		LOGGER.info(Environment.getDbUser());
		
		try {
			verificaRecursos();
			
			ValidaBeneficiarios validaBeneficiarios = new ValidaBeneficiarios();
			validaBeneficiarios.validar();
			
			PostgresDatasource.getInstance().printDataSourceStats();
		} catch (SQLException e) {
			LOGGER.error("Ocurrió un error al obtener la conexión a la BD:", e);
		} catch (NotDirectoryException e) {
			LOGGER.error("Ocurrió un error al leer la carpeta de archivos en el filesystem:", e);
		} catch (SecurityException e) {
			LOGGER.error("Ocurrió un error al leer la carpeta de archivos en el filesystem (Revise que tenga permisos de lectura):", e);
		} finally {
			PostgresDatasource.getInstance().shutdownDataSource();
		}
		LOGGER.info("*******************************************************************");
		LOGGER.info("*****************CONCLUYE PROCESO DE VALIDACIÓN*************************");
		LOGGER.info("*******************************************************************");
	}
	
	
	private static void verificaRecursos() throws SQLException, NotDirectoryException {
		LOGGER.info("***********REALIZANDO VERIFICACIONES PREVIO A LA VALIDACIÓN DE BENEFICIARIOS*************");
		LOGGER.info("Verificando conexión a la BD...");
		PostgresDatasource.getInstance().close(null, null, PostgresDatasource.getInstance().getConnection());
		LOGGER.info("Conexión a la BD correcta");
		
		LOGGER.info("Verificando acceso a filesystem con archivos del padrón...");
		if(!FileUtils.existsFileOrDirectory(Environment.getPathFolderPadrones())) {
			throw new NotDirectoryException("No se pudo tener acceso a la carpeta de archivos del padrón, revise el estado de los permisos");
		}
		LOGGER.info("Permisos a filesystem correctos");
	}
}
