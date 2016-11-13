package JavaUtils.Html.Extended.Client.Modules;

import JavaUtils.Html.Client;
import JavaUtils.Html.Extended.Client.Module;

public class AlertUserModule implements Module{

    @Override
    public String createModuleFunction() {
        return "function(message){alert(message);}";
    }

    @Override
    public String getName() {
        return "AlertUserModule";
    }

    @Override
    public String[] getRequiredModules() {
        return new String[0];
    }
    
    public void alertUser(Client c, String message){
        c.send("AlertUserModule " + message);
    }

    @Override
    public String parseArguments(String args) {
        return args;
    }

}
