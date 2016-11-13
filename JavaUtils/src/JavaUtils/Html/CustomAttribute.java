package JavaUtils.Html;

public class CustomAttribute extends HtmlAttribute implements Cloneable{

    private String uid = "";
    
    public CustomAttribute(String UID,String value) {
        super(value);
        this.uid = UID;
    }
    
    public String getID(){
        return uid;
    }

}
