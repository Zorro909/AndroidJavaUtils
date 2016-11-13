package JavaUtils.Html.Extended.Tags;

import JavaUtils.Html.HtmlTag;

public class CustomHtmlTag extends HtmlTag implements Cloneable{

    String uid;
    
    public CustomHtmlTag(String tag,String uid, String value){
        super(tag);
        this.uid = uid;
        setValue(value);
    }
    
    public CustomHtmlTag(String tag, String uid) {
        this(tag,uid,"");
    }

    public String getId() {
        return uid;
    }

}
