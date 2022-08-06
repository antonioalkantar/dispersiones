package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.environment;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author raul
 */
public class Environment {
	
	private static final Logger LOGGER = LogManager.getLogger(Environment.class);
	
	private static String appProfile = "";
	private static String dbUrl = "";
	private static String dbUser = "";
	private static String dbPassword = "";
	
	private static String pathFolderPadrones = ""; 
	
	private static String serviceAecdmx = "";
	private static String serviceAecdmxUser = "";
	private static String serviceAecdmxPassword = "";
	
	private static String serviceAEFCMUser = "";
	
	private static String serviceAEFCMPassword = "";
	
	private static int timeWaitingMin = 1;

	private Environment() {
		/** Constructor privado para evitar que se instancie esta clase */
	}
	
	static {
		/**Crear un Objeto de tipo Properties*/
		Properties properties = new Properties();   
		
		try {
			properties.load(Environment.class.getClassLoader().getResourceAsStream("META-INF/env.properties"));
			
			appProfile = properties.getProperty("app.profile", null);
			dbUrl = properties.getProperty("db.url", null);
			dbUser = properties.getProperty("db.user", null);
			dbPassword = properties.getProperty("db.password", null);
			
			pathFolderPadrones = properties.getProperty("path.folder.padrones", null);
			
			serviceAecdmx = properties.getProperty("services.thirdparty.aecdmx.valida.alumno", null);
			serviceAecdmxUser = properties.getProperty("services.thirdparty.aecdmx.valida.alumno.username", null);
			serviceAecdmxPassword = properties.getProperty("services.thirdparty.aecdmx.valida.alumno.password", null);
			
		   serviceAEFCMUser = properties.getProperty("service.aefcm.user");
            LOGGER.info("ENV [service.aefcm.user:\t\t{}]", serviceAEFCMUser);
            
            serviceAEFCMPassword = properties.getProperty("service.aefcm.password");
            LOGGER.info("ENV [service.aefcm.password:\t\t{}]", serviceAEFCMPassword);
			
			timeWaitingMin = Integer.parseInt(properties.getProperty("app.params.time-waiting-min"));
		} catch (IOException e) {
			LOGGER.error("Ocurrió un error al obtener los parámetros del Environment:", e);
		}
	}
	
	public static String getAppProfile() {
		return appProfile;
	}

	public static String getDbUrl() {
		return dbUrl;
	}

	public static String getDbUser() {
		return dbUser;
	}

	public static String getDbPassword() {
		return dbPassword;
	}

	public static String getPathFolderPadrones() {
		return pathFolderPadrones;
	}

	public static int getTimeWaitingMin() {
		return timeWaitingMin;
	}

	public static String getServiceAecdmx() {
		return serviceAecdmx;
	}

	public static String getServiceAecdmxUser() {
		return serviceAecdmxUser;
	}

	public static String getServiceAecdmxPassword() {
		return serviceAecdmxPassword;
	}

	public static String getServiceAEFCMUser() {
		return serviceAEFCMUser;
	}

	public static String getServiceAEFCMPassword() {
		return serviceAEFCMPassword;
	}
	
}