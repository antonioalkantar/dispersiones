package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.MciRequest;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.MciResponse;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.MciWS;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.MciWSService;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.UTPasswordCallback;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.environment.Environment;

/**
 * @author raul
 * 
 * Para crear el cliente del Webservice se realizaron los siguientes pasos:
 * 
 * 1. La entidad educativa nos compartió su endpoint: https://www6.aefcm.gob.mx:9024/mciws/services/mciService?wsdl
 * 2. Con dicho endpoint, desde línea de comandos y con ayuda de la herramienta "wsimport" del jdk de Java se generan las clases del cliente:
 * 
 *     wsimport -keep -verbose -p mx.gob.cdmx.adip.beca.soap.client.aefcm https://www6.aefcm.gob.mx:9024/mciws/services/mciService?wsdl
 *     
 * 3. En este proyecto, se crea el package mx.gob.cdmx.adip.beca.soap.client.aefcm y se copian las clases
 * 
 * 4. A las clases creadas, solo se tuvieron que hacer 2 pequeños ajustes:
 * 4.1 A la clase QueryByCurp en la anotación @XmlType(name = "queryByCurpOriginal"... se le agregó "...Original"
 * 4.2 A la clase QueryByCurpResponse en la anotación @XmlType(name = "queryByCurpResponseOriginal"... se le agregó Original
 *     Lo anterior se tuvo que hacer un problema que se genera si no se hace esto:
 *     
 *     IllegalAnnotationException: Two classes have the same XML type name
 *     
 *     Más información: https://stackoverflow.com/questions/4254334/illegalannotationexception-two-classes-have-the-same-xml-type-name
 * 
 * 5. Una vez modificado lo anterior, se agrega en el pom.xml las dependencias de CXR de Apache para generar el cliente del WS:
 *    cxf-rt-frontend-jaxws
 *    cxf-rt-transports-http
 *    cxf-rt-ws-security
 * 
 * 6. Se crea esta clase cliente, implementando el Cliente de CXF (Nótese la clase UTPasswordCallback que complementa esta solución).
 *    Se requiere de CXF ya que el Webservice SOAP de la Entidad Educativa tiene implementado un perfil de Autenticación de tipo "UserNameToken" con WS-Security.
 *    
 * 7. CXF usa internamente la librería BouncyCastle. Dicha librería usa un Proveedor de Seguridad que no viene por default configurado en la instalación de Java.
 *    Por lo tanto, se debe configurar dicho proveedor de seguridad:
 *    7.1 En la carpeta de instalación del JDK que usa el Wildfly, en la ruta /jre/lib/ext se agrega el jar bcprov-jdk15on-1.60.jar
 *    7.2 En la carpeta de instalación del JDK que usa el Wildfly, en el archivo /jre/lib/security/java.security se agrega el proveedor de seguridad de BC:
 *    	  # List of providers and their preference orders (see above):
 *        security.provider.1=sun.security.provider.Sun
 *        security.provider.2=sun.security.rsa.SunRsaSign
 *        ...
 *        ...
 *        security.provider.11=org.bouncycastle.jce.provider.BouncyCastleProvider
 */
public class EntidadEducativaSoapClient {
	
	public static MciResponse consultarCurp(final String curp) throws IOException {
		
		MciWSService mciWsService = new MciWSService();
		MciWS mciWs = mciWsService.getMciWSPort();
		
		MciRequest mciRequest = new MciRequest();
		mciRequest.setCurp(curp);
		
		Client client = ClientProxy.getClient(mciWs);
		Endpoint endpoint = client.getEndpoint();
		
		HTTPConduit conduit = (HTTPConduit)client.getConduit();
		
		conduit.getClient().setConnectionTimeout(1000 * 5); //Timeout de conexión en 5 segs 
		conduit.getClient().setReceiveTimeout(1000 * 5); //Timeout de lectura en 5 segs
		
		// Pasos para pasarle la autenticación mediante "Username Token" al webservice
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		props.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		props.put(WSHandlerConstants.PW_CALLBACK_CLASS, UTPasswordCallback.class.getName());
		props.put(WSHandlerConstants.USER, Environment.getServiceAEFCMUser());
		
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(props);
		endpoint.getOutInterceptors().add(wssOut);
		
		MciResponse response = mciWs.queryByCurp(mciRequest);
		
		((java.io.Closeable)mciWs).close();
		client.destroy();
		
		return response;
	}

	public static void main(String[] args) throws IOException {
		try {
			MciResponse mciEntidadEducativaResponse = 
					EntidadEducativaSoapClient.consultarCurp("TECL080404MMCLNZA4");
			System.out.println("servicioEducativo ID "+mciEntidadEducativaResponse.getServicioEducativoId());
			System.out.println("servicioEducativo "+mciEntidadEducativaResponse.getServicioEducativo());
			System.out.println("NIVEL ID "+mciEntidadEducativaResponse.getNivelEducativoId());
			System.out.println("NIVEL EDUCATIVO: "+mciEntidadEducativaResponse.getNivelEducativo());		
			System.out.println("Nivel educativo ADIP: "+ mciEntidadEducativaResponse.getNivelEducativoFIBIEDCDMX());
			System.out.println("ID Nivel educativo ADIP: "+ mciEntidadEducativaResponse.getNivelEducativoFIBIEDCDMXId());
//			System.out.println(mciEntidadEducativaResponse.getTipoEscuela());
//			
			System.out.println(mciEntidadEducativaResponse.getEstatus());
//			
//			System.out.println(mciEntidadEducativaResponse.getGradoEscolar());
//			System.out.println(mciEntidadEducativaResponse.getNivelEducativo());
//			System.out.println(mciEntidadEducativaResponse.getTurno());
//			System.out.println(mciEntidadEducativaResponse.getNombreCCT());
//			
			System.out.println("Alcaldia: " + mciEntidadEducativaResponse.getAlcaldia());
			System.out.println("Id Alcaldia: " + mciEntidadEducativaResponse.getAlcaldiaId());
			System.out.println("Id Alcaldia Parse Long: " + Long.parseLong(mciEntidadEducativaResponse.getAlcaldiaId()));
//			System.out.println(mciEntidadEducativaResponse.getCodigoPostal());
//			System.out.println(mciEntidadEducativaResponse.getColonia());
//			System.out.println(mciEntidadEducativaResponse.getCalle());
//			System.out.println(mciEntidadEducativaResponse.getNumeroExterior());
			
//			System.out.println(mciEntidadEducativaResponse.getCct());
//			System.out.println(mciEntidadEducativaResponse.getCicloEscolar());
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
	}

}
