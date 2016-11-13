package JavaUtils.Html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import JavaUtils.Html.Extended.Tags.CustomHtmlTag;
import JavaUtils.Html.Extended.Tags.ExtendedTag;
import JavaUtils.UtilHelpers.DeepClone;

public class HtmlTag implements Cloneable {

    protected LinkedList<HtmlTag> tags = new LinkedList<HtmlTag>();
    private String value;
    protected HashMap<String, HtmlAttribute> attributes = new HashMap<String, HtmlAttribute>();
    private HtmlTag upper;
    private Style s;
    private transient HtmlTag preClone;
    private String tag;

    public HtmlTag(String tag) {
        this.tag = tag;
    }

    public void setUpperHtmlTag(HtmlTag upper) {
        this.upper = upper;
    }

    public HtmlTag setValue(String value) {
        this.value = value;
        return this;
    }

    public Style getStyle() {
        if (s == null) s = new Style(this);
        return s;
    }

    public String getValue() {
        return value;
    }

    public ArrayList<HtmlTag> getTag(String string) {
        if (string.contains(".")) {
            String[] splitted = string.split("\\.");
            ArrayList<HtmlTag> ts = getTag(splitted[0]);
            if (ts.isEmpty()) return ts;
            for (HtmlTag h : ts) {
                ArrayList<HtmlTag> a = h.getTag(string.split("\\.", 2)[1]);
                if (a != null || a.isEmpty()) { return a; }
            }
            return new ArrayList<HtmlTag>();
        }
        ArrayList<HtmlTag> t = new ArrayList<HtmlTag>();
        for (HtmlTag tag : tags) {
            if (tag.getTagName().equalsIgnoreCase(string)) {
                t.add(tag);
            }
        }
        return t;
    }

    protected String getTagName() {
        return tag;
    }

    public boolean hasValue() {
        return (value == null ? false : true);
    }

    public HtmlTag addAttribute(String name, HtmlAttribute att) {
        if (att instanceof CustomAttribute) {
            if (upper != null) {
                upper.registerCustomAttribute((CustomAttribute) att);
            }
        }
        if (name.equalsIgnoreCase("style")) {
            getStyle().setHtmlAttribute(att);
            attributes.put("style", att);
            return this;
        }
        attributes.put(name, att);
        return this;
    }

    void registerCustomAttribute(CustomAttribute att) {
        upper.registerCustomAttribute(att);
    }

    public void addTag(HtmlTag tag) {
        tags.add(tag);
        tag.setUpperHtmlTag(this);
    }

    public void registerCustomTag(CustomHtmlTag tag) {
        if (upper != null) upper.registerCustomTag(tag);
    }

    public void preSetupCloning(HtmlTag upper) {
        preClone = upper;
    }

    public HtmlTag clone() {
        HtmlTag clone = null;
        try {
            clone = (HtmlTag) super.clone();
            clone.setUpperHtmlTag(preClone);
            clone.resetCustomThings();
            clone.tags = new LinkedList<HtmlTag>();
            for (HtmlTag h : tags) {
                h.preSetupCloning(clone);
                HtmlTag h2 = h.clone();
                clone.addTag(h2);
                if (h2 instanceof CustomHtmlTag) {
                    clone.registerCustomTag((CustomHtmlTag) h2);
                }
            }
            clone.attributes = DeepClone.deepClone(attributes);
            for (HtmlAttribute att : clone.attributes.values()) {
                if (att instanceof CustomAttribute) {
                    CustomAttribute ca = (CustomAttribute) att;
                    clone.registerCustomAttribute(ca);
                }
            }
            if (s != null) {
                clone.s = s.clone();
                clone.s.setHtmlAttribute(clone.attributes.get("style"));
            }
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return clone;
    }

    @Deprecated
    void resetCustomThings() {
    }

}
