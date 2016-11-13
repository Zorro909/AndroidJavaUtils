package JavaUtils.Html.Extended.Client;

public class ModuleNotLoadedException extends Exception {

    public ModuleNotLoadedException(String s) {
        super("Module " + s + " was not loaded into the Document!");
    }

    
    
}
