package JavaUtils.RestAPI;

import java.util.HashMap;

import JavaUtils.Parser.ParseObject;

public abstract class RedirectAction implements Action{

    String defaultRedirect = "";
    
    public RedirectAction(String defaultRedirect){
        this.defaultRedirect = defaultRedirect;
    }
    
    public static RedirectAction createStaticRedirectAction(String redirect){
        return new RedirectAction(redirect) {
            @Override
            public String execute(HashMap<String, String> conf, HashMap<String, String> vars) {
                return null;
            }
        };
    }
    
    @Override
    public ParseObject executeRequest(HashMap<String, String> conf, HashMap<String, String> vars) {
        ParseObject po = new ParseObject("");
        po.add("status", "303 Moved");
        String r = execute(conf, vars);
        if(r==null){
            r = defaultRedirect;
        }
        po.add("header", "Location: " + r);
        po.add("row", "");
        return po;
    }
    
    public abstract String execute(HashMap<String, String> conf, HashMap<String, String> vars);

    @Override
    public boolean isRaw() {
        return true;
    }

}
