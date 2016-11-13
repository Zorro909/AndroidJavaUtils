package JavaUtils.RestAPI;

import java.io.File;
import java.io.FileInputStream;
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
    int id = 0;

    RestAPIServer(final RestAPIActionSet set, TcpServerMode mode)
                    throws IOException, KeyManagementException, KeyStoreException,
                    NoSuchAlgorithmException, CertificateException {
        server = TCPManager.startServer(mode.getPort(), true, new TcpServerListener() {

            @Override
            public boolean clientConnect(TcpConnection connect, int index) {
                boolean keepAlive = true;
                while (keepAlive) {
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
                            if (set.isFile(conf.get("Request-URL"))) {
                                try {
                                    File file = set.getFile(conf.get("Request-URL"));
                                    String content_type = "";
                                    boolean streaming = false;
                                    String name = file.getName();
                                    if (name.contains(".")) {
                                        name = name.split("\\.")[1];
                                    }
                                    switch (name) {
                                    case "mp3":
                                        content_type = "Content-Type: audio/mpeg";
                                        streaming = true;
                                        break;
                                    case "mp4":
                                        content_type = "Content-Type: video/mp4";
                                        streaming = true;
                                        break;
                                    case "webm":
                                        content_type = "Content-Type: video/webm";
                                        streaming = true;
                                    }
                                    if (!streaming) {
                                        connect.writeLine("HTTP/1.1 200 OK"
                                                        + (content_type.isEmpty() ? ""
                                                                        : "\n" + content_type));
                                        connect.writeLine("Content-Length: " + file.length()
                                                        + "\n\n");
                                        connect.writeLine(FileUtils.readAll(file));
                                        connect.flush();
                                    } else {
                                        if (conf.containsKey("Range")) {
                                            connect.writeLine("HTTP/1.1 206 Partial Content"
                                                            + (content_type.isEmpty() ? ""
                                                                            : "\n" + content_type));
                                            String range = conf.get("Range");
                                            range = range.split("=")[1];
                                            FileInputStream fileInputStream = new FileInputStream(
                                                            file);
                                            long needsSkip = Long.valueOf(range.split("-")[0]);
                                            long skipped = 0;
                                            while (skipped < needsSkip) {
                                                skipped += fileInputStream
                                                                .skip(needsSkip - skipped);
                                            }
                                            boolean send = true;
                                            long length = 0;
                                            if (range.endsWith("-")) {
                                                length = Long.valueOf(file.length() - Long
                                                                .valueOf(range.split("-")[0]));
                                                send = false;
                                            } else {
                                                length = Long.valueOf(range.split("-")[1]) - 1;
                                            }
                                            byte[] data = null;
                                            connect.writeLine(
                                                            "X-Content-Duration: 1440.00\nContent-Duration: 1440.00");
                                            connect.writeLine(
                                                            "Accept-Ranges: bytes\nContent-Length: "
                                                                            + (length));
                                            connect.writeLine("Content-Range: bytes "
                                                            + range.split("-")[0] + "-"
                                                            + (length - 1 + Long.valueOf(
                                                                            range.split("-")[0]))
                                                            + (!send ? "/" + file.length() : ""));
                                            connect.writeLine("\n");
                                            if (send) {
                                                data = new byte[(int) length];
                                                fileInputStream.read(data);
                                                connect.writeBytes(data);
                                                fileInputStream.close();
                                            } else {
                                                int i = id;
                                                id++;
                                                data = new byte[(int) length];
                                                fileInputStream.read(data);
                                                connect.writeBytes(data);
                                                connect.flush();
                                                fileInputStream.close();
                                            }
                                        } else {
                                            connect.writeLine("HTTP/1.1 200 OK"
                                                            + (content_type.isEmpty() ? ""
                                                                            : "\n" + content_type));
                                            connect.writeLine("Content-Length: " + file.length()
                                                            + "\nAccept-Ranges: bytes\n\n");
                                            FileInputStream fileInputStream = new FileInputStream(
                                                            file);
                                            while (fileInputStream.available() > 0
                                                            && !connect.getSocket().isClosed()) {
                                                byte[] data = new byte[2048];
                                                fileInputStream.read(data);
                                                connect.writeBytes(data);
                                                connect.flush();
                                            }
                                        }
                                    }
                                    connect.flush();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    connect.writeLine("HTTP/1.1 404 Not Found\n\n");
                                }
                            } else {
                                try {
                                    XmlElement element = set.request(conf);
                                    String decode = element.decode();
                                    String content_type = null;
                                    String s = element.decode();
                                    if (s.equals("bytes")) {

                                        connect.writeLine("HTTP/1.1 200 OK\n" + content_type
                                                        + "Content-Length: " + s.length()
                                                        + "\nAccess-Control-Allow-Origin: *\n");
                                        connect.writeBytes(((bxml) element).toBytes());

                                    } else {
                                        if (decode.startsWith("http_content_type=")) {
                                            content_type = "Content-Type: "
                                                            + decode.split(":", 2)[0].split("=",
                                                                            2)[1]
                                                            + "\n";
                                            s = s.split(":", 2)[1];
                                        } else {
                                            content_type = (decode.startsWith("<")
                                                            && !decode.startsWith("<html>")
                                                                            ? "Content-Type: text/xml\n"
                                                                            : "");
                                        }
                                        String header = "";
                                        if (s.startsWith("header:")) {
                                            header = s.split("&%")[0].split("header:")[1];
                                            if (!header.endsWith("\n")) {
                                                header += "\n";
                                            }
                                            s = s.split("&%", 2)[1];
                                        }
                                        String status = "200 OK\n";
                                        if (s.startsWith("status:")) {
                                            status = s.split("&%")[0].split("status:")[1];
                                            if (!status.endsWith("\n")) {
                                                status += "\n";
                                            }
                                            s = s.split("&%", 2)[1];
                                        }
                                        connect.writeLine("HTTP/1.1 " + status + content_type
                                                        + (content_type.endsWith("\n")
                                                                        && !content_type.isEmpty()
                                                                                        ? "" : "\n")
                                                        + "Access-Control-Allow-Origin: *\n"
                                                        + header + "\n" + s);
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    connect.writeLine("HTTP/1.1 404 Not Found\n\n");
                                }
                            }
                            break;
                        } else {
                            String[] s = line.split(":", 2);
                            if (s[1].startsWith(" ")) s[1] = s[1].substring(1);
                            conf.put(s[0], s[1]);
                        }
                    }
                    keepAlive = saveGet(conf, "Connection").equals("Keep-Alive") ? true : false;
                }
                try {
                    connect.getSocket().close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return false;
            }

        }, mode);
    }

    protected String saveGet(HashMap<String, String> conf, String string) {
        if (conf.containsKey(string)) {
            return conf.get(string);
        } else {
            return "";
        }
    }

}
