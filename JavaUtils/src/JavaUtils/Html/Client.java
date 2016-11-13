package JavaUtils.Html;

import java.nio.channels.NotYetConnectedException;
import java.util.Arrays;
import java.util.HashMap;

import org.java_websocket.WebSocket;

import JavaUtils.Html.Extended.Client.ListenerModule;
import JavaUtils.Html.Extended.Client.Module;
import JavaUtils.Html.Extended.Client.ModuleNotLoadedException;

public class Client {

    WebSocket ws;
    String uid;
    private String incoming = "";
    private HashMap<String, Module> modules = new HashMap<String, Module>();

    public Client(WebSocket conn, String string) {
        ws = conn;
        uid = string;
    }

    public void loadModule(Module m) throws InterruptedException, ModuleNotLoadedException {
        if (modules.containsKey(m.getName())) return;
        if (!checkModule(m)) { throw new ModuleNotLoadedException(
                        Arrays.toString(m.getRequiredModules())); }
        sendExecute("registerModule('" + m.getName() + "'," + m.createModuleFunction() + ");");
        modules.put(m.getName(), m);
    }

    private boolean checkModule(Module m) {
        for (String s : m.getRequiredModules()) {
            if (!modules.containsKey(s) && checkModule(modules.get(s))) { return false; }
        }
        return true;
    }

    @Deprecated
    public void executeModule(String name, String args) {
        args = modules.get(name).parseArguments(args);
        send(name + " " + args);
        try {
            read();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public WebSocket getWebSocket() {
        return ws;
    }

    public String getUUID() {
        return uid;
    }

    public void onIncoming(String message) {
        if (message.contains(":") && modules.containsKey(message.split(":")[0])) {
            try {
                ((ListenerModule) modules.get(message.split(":")[0]))
                                .receiveMessage(message.split(":", 2)[1]);
            } catch (Exception e) {
                if (!incoming.isEmpty()) {
                    incoming += "\n";
                }
                incoming += message;
            }
        } else {
            if (!incoming.isEmpty()) {
                incoming += "\n";
            }
            incoming += message;
        }
    }

    public String read(int timeout) throws InterruptedException {
        while (ws.isOpen()) {
            synchronized (incoming) {
                if (!incoming.isEmpty()) {
                    break;
                }
            }
            Thread.sleep(200L);
        }
        if (!ws.isOpen()) { throw new NotYetConnectedException(); }
        String s = incoming;
        if (s.contains("\n")) {
            incoming = s.split("\n", 2)[1];
            s = s.split("\n", 2)[0];
        } else {
            incoming = "";
        }
        return s;
    }

    public String read() throws InterruptedException {
        return read(60000);
    }

    public String sendExecute(String script) throws InterruptedException {
        if (!ws.isOpen()) { throw new NotYetConnectedException(); }
        send("execute " + script);
        return read();
    }

    public void sendSave(String script) {
        send("save " + script);
    }

    public void send(String message) {
        ws.send(message);
    }

    public String read(boolean b) {
        if (b) {
            try {
                return read();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                return null;
            }
        } else {
            String i = incoming;
            incoming = "";
            return i;
        }
    }

    public boolean isModuleLoaded(String string) {
        return modules.containsKey(string);
    }

}
