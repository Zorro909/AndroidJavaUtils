package JavaUtils.RestAPI;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;

import JavaUtils.TCPManager.TCPManager;
import JavaUtils.TCPManager.TcpConnection;
import JavaUtils.TCPManager.TcpServer;
import JavaUtils.TCPManager.TcpServerListener;
import JavaUtils.TCPManager.TcpServerMode;
import JavaUtils.UtilHelpers.FileUtils;
import JavaUtils.XML.XmlElement;

public class RestAPIServer {

	TcpServer server;

	RestAPIServer(final RestAPIActionSet set,TcpServerMode mode) throws IOException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException {	    
	    server = TCPManager.startServer(mode.getPort(), true, new TcpServerListener() {

			@Override
			public boolean clientConnect(TcpConnection connect, int index) {
				boolean keepAlive = true;
				while(keepAlive){
				String line = "";
				HashMap<String, String> conf = new HashMap<String, String>();
				while ((line = connect.readLine()) == null) {
					try {
						Thread.sleep(20L);
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
				String[] request = line.split(" ");
				conf.put("Request-Type", request[0]);
				conf.put("Request-URL", request[1]);
				conf.put("HTTP-Version", request[2]);
				while ((line = connect.readLine()) != null) {
					if (line.equals("")) {
						if(set.isFile(conf.get("Request-URL"))){
							try {
								connect.writeLine("HTTP/1.1 200 OK\n\n" + FileUtils.readAll(set.getFile(conf.get("Request-URL"))));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
						try {
							XmlElement element = set.request(conf);
							String decode = element.decode();
							String content_type = null;
							if(decode.startsWith("http_content_type=")){
							    content_type = "Content-Type: " + decode.split(":",2)[0].split("=",2)[1] + "\n";
							}else{
							    content_type = (decode.startsWith("<") && !decode.startsWith("<html>") ?  "Content-Type: text/xml\n" : "");
							}
							connect.writeLine("HTTP/1.1 200 OK\n" + content_type + "Access-Control-Allow-Origin: *\n\n" + element.decode());
						} catch (Exception e1) {
							connect.writeLine("HTTP/1.1 404 Not Found\n\n");
						}
						}
						break;
					} else {
						String[] s = line.split(":", 2);
						if (s[1].startsWith(" "))
							s[1] = s[1].substring(1);
						conf.put(s[0], s[1]);
					}
				}
				keepAlive = saveGet(conf,"Connection").equals("Keep-Alive") ? true : false;
				}
				try {
					connect.getSocket().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}

		},mode);
	}

	protected String saveGet(HashMap<String, String> conf, String string) {
		if(conf.containsKey(string)){
			return conf.get(string);
		}else{
			return "";
		}
	}

}
