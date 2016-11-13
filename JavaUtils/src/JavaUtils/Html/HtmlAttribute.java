package JavaUtils.Html;

public class HtmlAttribute implements Cloneable{

    public String value;
    
    public HtmlAttribute(String value){
        setValue(value);
    }
    
    public String getValue(){
        return value;
    }
    
    public void setValue(String value){
        this.value = value.replace("\"", "'");
    }
    
    @Override
    public HtmlAttribute clone(){
        try {
            return (HtmlAttribute) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
