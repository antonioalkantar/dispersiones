package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContadorErroresSynchronized {

	private static final Logger LOGGER = LogManager.getLogger(ContadorErroresSynchronized.class);

	//Lista completa de curps con errores
	private static List<String> noDispersadosPorErrorWS = new ArrayList<>();
	private static List<String> noDispersadosPorBeneficiarioNoActivo = new ArrayList<>();
	private static List<String> noDispersadosPorTutorNoAprobado = new ArrayList<>();
	private static List<String> noDispersadosPorNumeroCuentaVacia = new ArrayList<>();
	
	//Lista de curps con errores por hilo 
	private List<String> noDispersadosPorErrorWSPorHilo = new ArrayList<>();
	private List<String> noDispersadosPorBeneficiarioNoActivoPorHilo = new ArrayList<>();
	private List<String> noDispersadosPorTutorNoAprobadoPorHilo = new ArrayList<>();
	private List<String> noDispersadosPorNumeroCuentaVaciaPorHilo = new ArrayList<>();
	
	//Lista completa de curps con errores
	public static synchronized void agregarCurpNoDispersadaPorErrorWS(String curpBeneficiario) {
		noDispersadosPorErrorWS.add(curpBeneficiario);
	}
	
	public static synchronized void agregarCurpNoDispersadaPorBeneficiarioNoActivo(String curpBeneficiario) {
		noDispersadosPorBeneficiarioNoActivo.add(curpBeneficiario);
	}
	
	public static synchronized void agregarCurpNoDispersadaPorTutorNoAprobado(String curpBeneficiario) {
		noDispersadosPorTutorNoAprobado.add(curpBeneficiario);
	}
	
	public static synchronized void agregarCurpNoDispersadaPorNumeroCuentaVacia(String curpBeneficiario) {
		noDispersadosPorNumeroCuentaVacia.add(curpBeneficiario);
	}
	
	//Lista de curps con errores por hilo 
	public void agregarCurpNoDispersadaPorErrorWSPorHilo(String curpBeneficiario) {
		noDispersadosPorErrorWSPorHilo.add(curpBeneficiario);
	}
	
	public void agregarCurpNoDispersadaPorBeneficiarioNoActivoPorHilo(String curpBeneficiario) {
		noDispersadosPorBeneficiarioNoActivoPorHilo.add(curpBeneficiario);
	}
	
	public void agregarCurpNoDispersadaPorTutorNoAprobadoPorHilo(String curpBeneficiario) {
		noDispersadosPorTutorNoAprobadoPorHilo.add(curpBeneficiario);
	}
	
	public void agregarCurpNoDispersadaPorNumeroCuentaVaciaPorHilo(String curpBeneficiario) {
		noDispersadosPorNumeroCuentaVaciaPorHilo.add(curpBeneficiario);
	}
	
	public static void mostrarCurpsConErrores() {
		LOGGER.info("############## INICIA - CURPS NO DISPERSADOS ############### ");
		
		LOGGER.info("******* CURPS NO DISPERSADOS POR ERROR WS: " + noDispersadosPorErrorWS.size());
		LOGGER.info(noDispersadosPorErrorWS.stream().map(Object::toString).collect(Collectors.joining(",")).toString());
		
		LOGGER.info("******* CURPS NO DISPERSADOS POR ERROR BENEFICIARIO NO ACTIVO: " + noDispersadosPorBeneficiarioNoActivo.size());
		LOGGER.info(noDispersadosPorBeneficiarioNoActivo.stream().map(Object::toString).collect(Collectors.joining(",")).toString());
		
		LOGGER.info("******* CURPS NO DISPERSADOS POR ERROR TUTOR NO APROBADO: " + noDispersadosPorTutorNoAprobado.size());
		LOGGER.info(noDispersadosPorTutorNoAprobado.stream().map(Object::toString).collect(Collectors.joining(",")).toString());
		
		LOGGER.info("******* CURPS NO DISPERSADOS POR ERROR SIN NUMERO DE CUENTA: " + noDispersadosPorNumeroCuentaVacia.size());
		LOGGER.info(noDispersadosPorNumeroCuentaVacia.stream().map(Object::toString).collect(Collectors.joining(",")).toString());
		
		LOGGER.info("############## TERMINA - CURPS NO DISPERSADOS ############### ");
	}
	
	public void mostrarCurpsConErroresPorHilo(String hiloEnCurso) {
		
		LOGGER.info("############## INICIA - CURPS NO DISPERSADOS DEL HILO: " + hiloEnCurso);
		
		LOGGER.info("******* CURPS NO DISPERSADOS POR ERROR WS - HILO: " + noDispersadosPorErrorWSPorHilo.size());
		LOGGER.info(noDispersadosPorErrorWSPorHilo.stream().map(Object::toString).collect(Collectors.joining(",")).toString());
		
		LOGGER.info("******* CURPS NO DISPERSADOS POR ERROR BENEFICIARIO NO ACTIVO - HILO: " + noDispersadosPorBeneficiarioNoActivoPorHilo.size());
		LOGGER.info(noDispersadosPorBeneficiarioNoActivoPorHilo.stream().map(Object::toString).collect(Collectors.joining(",")).toString());
		
		LOGGER.info("******* CURPS NO DISPERSADOS POR ERROR TUTOR NO APROBADO - HILO: " + noDispersadosPorTutorNoAprobadoPorHilo.size());
		LOGGER.info(noDispersadosPorTutorNoAprobadoPorHilo.stream().map(Object::toString).collect(Collectors.joining(",")).toString());
		
		LOGGER.info("******* CURPS NO DISPERSADOS POR ERROR SIN NUMERO DE CUENTA - HILO: " + noDispersadosPorNumeroCuentaVaciaPorHilo.size());
		LOGGER.info(noDispersadosPorNumeroCuentaVaciaPorHilo.stream().map(Object::toString).collect(Collectors.joining(",")).toString());
		
		LOGGER.info("############## TERMINA - CURPS NO DISPERSADOS DEL HILO: " + hiloEnCurso);
	}
	
	public static void reiniciarListasDeErrores() {
		noDispersadosPorErrorWS = new ArrayList<>();
		noDispersadosPorBeneficiarioNoActivo = new ArrayList<>();
		noDispersadosPorTutorNoAprobado = new ArrayList<>();
		noDispersadosPorNumeroCuentaVacia = new ArrayList<>();
	}
	
	public void reiniciarListasDeErroresPorHilo() {
		noDispersadosPorErrorWSPorHilo = new ArrayList<>();
		noDispersadosPorBeneficiarioNoActivoPorHilo = new ArrayList<>();
		noDispersadosPorTutorNoAprobadoPorHilo = new ArrayList<>();
		noDispersadosPorNumeroCuentaVaciaPorHilo = new ArrayList<>();
	}
	
}
