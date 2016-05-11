package JavaUtils.RestAPI;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import JavaUtils.TCPManager.TcpServerMode;

public class RestAPI {

	public static RestAPIServer startRestAPIServer(RestAPIActionSet set, TcpServerMode mode){
	    try {
			return new RestAPIServer(set,mode);
		} catch (IOException e) {
			// TODO Auto-generated catch block##
			e.printStackTrace();
			return null;
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
