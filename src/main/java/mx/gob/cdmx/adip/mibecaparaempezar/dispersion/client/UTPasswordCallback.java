package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.environment.Environment;


/**
 * @author raul
 */
public class UTPasswordCallback implements CallbackHandler {
	
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (Callback callback : callbacks) {
			WSPasswordCallback wpc = (WSPasswordCallback) callback;
			if (wpc.getIdentifier().equals(Environment.getServiceAEFCMUser())) {
				wpc.setPassword(Environment.getServiceAEFCMPassword());
				return;
			}
		}
	}
	

}