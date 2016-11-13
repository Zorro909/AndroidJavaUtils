package JavaUtils.JavaScript;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.UUID;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import JavaUtils.Html.Client;
import JavaUtils.Html.HtmlAttribute;
import JavaUtils.Html.HtmlDocument;
import JavaUtils.Html.HtmlTag;
import JavaUtils.Html.Extended.Client.Module;
import JavaUtils.Html.Extended.Client.ModuleNotLoadedException;
import JavaUtils.Html.Extended.Tags.ScriptTag;
import JavaUtils.RestAPI.RestAPIActionSet;
import JavaUtils.UtilHelpers.FileUtils;

public class ScriptManager extends WebSocketServer {

    private ArrayList<Client> clients = new ArrayList<Client>();
    ScriptTag st;

    public ScriptManager(InetSocketAddress a, RestAPIActionSet raas) {
        super(a);
        try {
            st = new ScriptTag(null, FileUtils.readAll(this.getClass()
                            .getResource("/JavaScriptSource/ConnectingCommand.js").openStream()),
                            raas);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        start();
    }

    public String setupConnectionScript(HtmlDocument doc) {
        String clientID = UUID.randomUUID().toString();
        doc.getTag("html.head.conInfo");
        HtmlTag h = new HtmlTag("conInfo");
        h.addAttribute("info", new HtmlAttribute(getAddress().getAddress().getHostAddress() + ":" + getPort() + ":" + clientID));
        doc.head().addTag(h);
        doc.head().addTag(st);
        return clientID;
    }

    public Client getClientByID(String id) {
        for (Client c : clients) {
            if (c.getUUID().equals(id)) { return c; }
        }
        return null;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getWebSocket() == conn) {
                synchronized (clients) {
                    clients.remove(i);
                }
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message.startsWith("connect:")) {
            Client c = new Client(conn, message.split(":")[1]);
            clients.add(c);
        } else {
            for (Client c : clients) {
                if (c.getWebSocket() == conn) {
                    c.onIncoming(message);
                }
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getWebSocket() == conn) {
                synchronized (clients) {
                    clients.remove(i);
                }
            }
        }
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public Client broadcastClient() {
        return new Client(null, null) {
            public void loadModule(Module m) throws InterruptedException, ModuleNotLoadedException {
                ArrayList<Client> cl = (ArrayList<Client>) clients.clone();
                for (Client c : cl) {
                    if (!c.getWebSocket().isClosed()) {
                        c.loadModule(m);
                    }
                }
            }

            @Deprecated
            public void executeModule(String name, String args) {
                ArrayList<Client> cl = (ArrayList<Client>) clients.clone();
                for (Client c : cl) {
                    c.executeModule(name, args);
                }
            }

            public WebSocket getWebSocket() {
                return null;
            }

            public String getUUID() {
                return null;
            }

            public void onIncoming(String message) {

            }

            public String read() throws InterruptedException {
                return null;
            }

            public String sendExecute(String script) throws InterruptedException {
                    ArrayList<Client> cl = (ArrayList<Client>) clients.clone();
                    for (Client c : cl) {
                        if (c.getWebSocket().isOpen() && !c.getWebSocket().isClosing()) {
                            try {
                                c.sendExecute(script);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                return null;
            }

            public void sendSave(String script) {
                ArrayList<Client> cl = (ArrayList<Client>) clients.clone();
                for (Client c : cl) {
                    c.send(script);
                }
            }

            public void send(String message) {
                ArrayList<Client> cl = (ArrayList<Client>) clients.clone();
                for (Client c : cl) {
                    c.send(message);
                }
            }

            public String read(boolean b) {
                return null;
            }

            public boolean isModuleLoaded(String string) {
                return false;
            }
        };
    }

    public String getScript() throws IOException {
        return FileUtils.readAll(this.getClass()
                        .getResource("/JavaScriptSource/ConnectingCommand.js").openStream());
    }

}
