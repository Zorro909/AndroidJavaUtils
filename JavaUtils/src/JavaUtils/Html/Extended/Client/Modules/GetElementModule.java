package JavaUtils.Html.Extended.Client.Modules;

import java.util.ArrayList;

import JavaUtils.Html.Extended.Client.Module;

public class GetElementModule implements Module {

    @Override
    public String createModuleFunction() {
        return "function(search){return eval(search);}";
    }

    @Override
    public String getName() {
        return "GetElementModule";
    }

    @Override
    public String[] getRequiredModules() {
        return new String[0];
    }

    @Override
    public String parseArguments(String args) {
        String tag = "";
        if (args.contains("#")) {
            tag = args.split("#")[0];
        } else if (args.contains("\\.")) {
            tag = args.split("\\.")[0];
        }
        String id = "";
        ArrayList<String> classes = new ArrayList<String>();
        if (args.contains("#")) {
            id = args.split("#")[1];
            if (id.contains("\\.")) {
                id = id.split("\\.")[0];
            }
            return "document.getElementById('" + id + "');";
        } else if (args.contains(".")) {
            String[] c = args.split(".");
            for (int i = 1; i < c.length; i++) {
                classes.add(c[i]);
            }
            String a = "var r = document.getElementsByTagName('" + tag + "');var r2 = []";
            int i = 0;
            for (String s : classes) {
                a += "if(r.length==1)return r[0];" + "for(var i" + i + " = 0;i" + i + "<r.length;i"
                                + i + "++){"
                                + "if(r[i].getAttribute('class')!=null&&r[i].getAttribute('class').search('"
                                + s + "')!=-1){" + "r2[r2.length] = r[i];" + "}" + "r = r2;";

            }
            a+="r;";
            return a;
        }
        return "document.getElementsByTagName('" + tag + "');";
    }

}
