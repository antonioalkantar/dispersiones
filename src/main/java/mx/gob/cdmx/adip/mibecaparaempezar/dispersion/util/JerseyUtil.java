package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.environment.Environment;

/**
 * @author raul
 */
public class JerseyUtil {

	private static final Logger LOGGER = LogManager.getLogger(JerseyUtil.class);
	
	private static JerseyUtil instance;
	private Client clientWithAuth;
	
	private JerseyUtil() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 30000); //30seg
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		
		confiarEnTodosLosCertificados();
		
		clientWithAuth = Client.create(clientConfig);
		clientWithAuth.setConnectTimeout(10000); // Establecer a 10 segundos
		clientWithAuth.setReadTimeout(30000); //Establece tiempo de lectura 30 segundos;
		clientWithAuth.addFilter(new HTTPBasicAuthFilter(Environment.getServiceAecdmxUser(), Environment.getServiceAecdmxPassword()));
	}
		
	public static JerseyUtil getInstance() {
		if(instance == null) {
			instance = new JerseyUtil();
		}
		return instance;
	}
	
	public Client getClientWithAuth() {
		return clientWithAuth;
	}

	/** 
	 * Este método lo que causa es que el cliente permita conectarse con servicios que están publicados en HTTPS 
	 * y que la JVM no tiene registrado en el cacerts el certificado de ese dominio donde se encuentra el servicio.
	 *  
	 * Por ejemplo, si no se invoca este método y en la cacerts no se agrega el certificado, marcará el error: 
	 * sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	 */
	private void confiarEnTodosLosCertificados() {
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
		    public X509Certificate[] getAcceptedIssuers(){return null;}
		    public void checkClientTrusted(X509Certificate[] certs, String authType){}
		    public void checkServerTrusted(X509Certificate[] certs, String authType){}
		}};

		// Install the all-trusting trust manager
		try {
		    SSLContext sc = SSLContext.getInstance("TLS");
		    sc.init(null, trustAllCerts, new SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			LOGGER.error("Ocurrio un error al hacer que se confie en todos los certificados en la JVM:", e);
		}
	}
}
