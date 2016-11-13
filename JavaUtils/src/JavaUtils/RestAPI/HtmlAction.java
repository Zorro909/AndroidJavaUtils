package JavaUtils.RestAPI;

import java.util.HashMap;

import JavaUtils.Html.HtmlDocument;
import JavaUtils.Parser.ParseObject;

public abstract class HtmlAction implements Action {

    protected HtmlDocument doc;

    public HtmlAction(HtmlDocument doc) {
        this.doc = doc;
    }

    @Override
    public ParseObject executeRequest(HashMap<String, String> conf, HashMap<String, String> vars) {
        return createModifiedHtmlDocument(doc.clone(), conf, vars).generateParseObject();
    }

    public abstract HtmlDocument createModifiedHtmlDocument(HtmlDocument clone,
                    HashMap<String, String> conf, HashMap<String, String> vars);

    @Override
    public boolean isRaw() {
        return true;
    }

}
