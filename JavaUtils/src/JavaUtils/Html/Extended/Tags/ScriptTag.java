package JavaUtils.Html.Extended.Tags;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import JavaUtils.Html.HtmlAttribute;
import JavaUtils.Html.HtmlDocument;
import JavaUtils.Html.HtmlTag;
import JavaUtils.RestAPI.RestAPIActionSet;

public class ScriptTag extends ExtendedTag implements Cloneable {

    static int scripts = 0;
    String url = "";

    public ScriptTag(HtmlDocument doc, File f, RestAPIActionSet raas) {
        super("script");
        scripts++;
        if (doc != null) doc.head().addTag(this);
        raas.addFile("/scripts/script-" + scripts + ".js", f);
        url = "/scripts/script-" + scripts + ".js";
        addAttribute("src", new HtmlAttribute("/scripts/script-" + scripts + ".js"));
        addAttribute("type", new HtmlAttribute("text/javascript"));
    }

    public ScriptTag(HtmlDocument doc, URI uri) {
        super("script");
        if (doc != null) doc.head().addTag(this);
        url = uri.toString();
        addAttribute("src", new HtmlAttribute("//" + uri.toString()));
        addAttribute("type", new HtmlAttribute("text/javascript"));
    }

    public ScriptTag(HtmlDocument doc, String script, RestAPIActionSet raas) throws IOException {
        this(doc, writeToTempFile(script), raas);
    }

    private static File writeToTempFile(String script) throws IOException {
        File tempFile = File.createTempFile("script-" + (scripts + 1), ".js");
        PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
        pw.println(script);
        pw.flush();
        pw.close();
        return tempFile;
    }

    public String getScriptURL() {
        return url;
    }

}
