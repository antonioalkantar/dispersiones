package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContadorProgresoSynchronized {

	private static final Logger LOGGER = LogManager.getLogger(ContadorProgresoSynchronized.class);
	
	private static long meta = 0;
	private static long progreso = 0;
	
	public static synchronized void incrementarAvance(long valorSumar) {
		progreso = progreso + valorSumar;
		LOGGER.info("Suma registros:" + progreso + " de un total de: "+meta);
	}
	
	public static void reset() {
		progreso = 0;
	}
	
	public static void setMeta(long nuevaMeta) {
		meta = nuevaMeta;
	}
	
}
