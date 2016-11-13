package JavaUtils.Html.Extended.Client;

public interface Module {

    public String createModuleFunction();
        
    public String getName();
    
    public String[] getRequiredModules();

    public String parseArguments(String args);

}
