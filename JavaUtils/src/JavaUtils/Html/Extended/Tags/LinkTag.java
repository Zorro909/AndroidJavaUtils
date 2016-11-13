package JavaUtils.Html.Extended.Tags;

import JavaUtils.Html.HtmlAttribute;
import JavaUtils.Html.HtmlTag;

public class LinkTag extends ExtendedTag implements Cloneable{

    public LinkTag(String url, String displayText) {
        super("a");
        addAttribute("href", new HtmlAttribute(url));
        setValue(displayText);
    }

}
