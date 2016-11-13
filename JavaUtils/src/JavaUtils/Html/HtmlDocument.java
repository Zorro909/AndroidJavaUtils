package JavaUtils.Html;

import java.util.ArrayList;
import java.util.HashMap;

import JavaUtils.Html.Extended.Tags.CustomHtmlTag;
import JavaUtils.Parser.ParseObject;

public class HtmlDocument extends HtmlTag implements Cloneable {

    HashMap<String, CustomAttribute> customAttributes = new HashMap<String, CustomAttribute>();
    HashMap<String, CustomHtmlTag> customTags = new HashMap<String, CustomHtmlTag>();

    public HtmlDocument() {
        super("");
    }
    
    

    @Override
    public void registerCustomAttribute(CustomAttribute att) {
        if (customAttributes.containsKey(att.getID())) {
            String name = findNameA(att.getID(), 0);
            customAttributes.put(name, att);
        } else {
            customAttributes.put(att.getID(), att);
            customAttributes.put(att.getID() + "_0", att);
        }
    }

    private String findNameA(String id, int i) {
        if (customAttributes.containsKey(id + "_" + i)) {
            i++;
            if (customAttributes.containsKey(id + "_" + i)) {
                i++;
                if (customAttributes.containsKey(id + "_" + i)) {
                    i++;
                    if (customAttributes.containsKey(id + "_" + i)) {
                        return findNameA(id, i + 1);
                    }
                    return id + "_" + i;
                }
                return id + "_" + i;
            }
            return id + "_" + i;
        }
        return id + "_" + i;
    }

    private String findNameT(String id, int i) {
        if (customTags.containsKey(id + "_" + i)) {
            i++;
            if (customTags.containsKey(id + "_" + i)) {
                i++;
                if (customTags.containsKey(id + "_" + i)) {
                    i++;
                    if (customTags.containsKey(id + "_" + i)) {
                        return findNameT(id, i + 1);
                    }
                    return id + "_" + i;
                }
                return id + "_" + i;
            }
            return id + "_" + i;
        }
        return id + "_" + i;
    }

    public HtmlTag body() {
        ArrayList<HtmlTag> h = getTag("html.body");
        if (!h.isEmpty()) return h.get(0);
        html().addTag(new HtmlTag("body"));
        return getTag("html.body").get(0);
    }

    private HtmlTag html() {
        ArrayList<HtmlTag> h = getTag("html");
        if (!h.isEmpty()) return h.get(0);
        addTag(new HtmlTag("html"));
        return getTag("html").get(0);
    }

    public HtmlTag head() {
        ArrayList<HtmlTag> h = getTag("html.head");
        if (!h.isEmpty()) return h.get(0);
        html().addTag(new HtmlTag("head"));
        return getTag("html.head").get(0);
    }

    public void setCustomAttribute(String id, String value) {
        customAttributes.get(id).setValue(value);
    }

    public void setCustomTag(String id, String value) {
        customTags.get(id).setValue(value);
    }

    public String generateHTML() {
        String html = "<!DOCTYPE html>";
        for (HtmlTag ta : tags) {
            html += "<" + ta.getTagName() + " " + generateAttributes(ta) + ">" + generateHTML(ta)
                            + "</" + ta.getTagName() + ">";
        }
        return html;
    }

    private String generateAttributes(HtmlTag tag) {
        String att = "";
        for (String a : tag.attributes.keySet()) {
            att += a + "=\"" + tag.attributes.get(a).getValue() + "\" ";
        }
        return att;
    }

    private String generateHTML(HtmlTag tag) {
        String html = "";
        if (tag.hasValue()) {
            return tag.getValue();
        } else {
            for (HtmlTag ta : tag.tags) {
                html += "<" + ta.getTagName() + " " + generateAttributes(ta) + ">"
                                + generateHTML(ta) + "</" + ta.getTagName() + ">";
            }
        }
        return html;
    }

    public void setTitle(String title) {
        HtmlTag t = new HtmlTag("title");
        t.setValue(title);
        head().addTag(t);
    }

    public ParseObject generateParseObject() {
        ParseObject po = new ParseObject("html");
        po.add("raw", generateHTML());
        po.add("content_type", "text/html");
        return po;
    }

    @Override
    public void registerCustomTag(CustomHtmlTag tag) {
        if (customTags.containsKey(tag.getId())) {
            String name = findNameT(tag.getId(), 0);
            customTags.put(name, tag);
        } else {
            customTags.put(tag.getId(), tag);
            customTags.put(tag.getId() + "_0", tag);
        }
    }

    @Override
    public HtmlDocument clone() {
        HtmlDocument hd = (HtmlDocument) super.clone();
        return hd;
    }

    @Override
    void resetCustomThings() {
        customAttributes = new HashMap<String, CustomAttribute>();
        customTags = new HashMap<String, CustomHtmlTag>();
    }

}
