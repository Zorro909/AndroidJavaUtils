package JavaUtils.Html.Extended.Client.Modules;

import JavaUtils.Html.Client;
import JavaUtils.Html.Extended.Client.Module;

public class EditElementModule implements Module {

    @Override
    public String createModuleFunction() {
        return "function(args){var search = args.split(' ')[0];" + "var type = args.split(' ')[1];"
                        + "var name = args.split(' ')[2];" + "var content = args.substring(search.length + 1 + type.length + 1 + name.length + 1);"
                        + "var e = GetElementModule(search);" + "if(type=='content'){"
                        + "e.innerHTML=content;" + "}else if(type=='attribute'){"
                        + "e.setAttribute(name,content);" + "}" + "}";
    }

    @Override
    public String getName() {
        return "EditElementModule";
    }

    @Override
    public String[] getRequiredModules() {
        return new String[] { "GetElementModule" };
    }

    public void editElementContent(Client c, String elementSearch, String content) {
        c.send("EditElementModule " + parseArguments(elementSearch + " content _ " + content));
    }

    public void editElementAttribute(Client c, String elementSearch, String attribute,
                    String content) {
        c.send("EditElementModule " + parseArguments(elementSearch + " attribute " + attribute + " " + content));
    }

    @Override
    public String parseArguments(String args) {
        args = args.replace(args.split(" ")[0], new GetElementModule().parseArguments(args.split(" ")[0]));
        return args;
    }

}
