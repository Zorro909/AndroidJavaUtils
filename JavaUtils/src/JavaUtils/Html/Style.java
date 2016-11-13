package JavaUtils.Html;

public class Style implements Cloneable{

    private HtmlAttribute style;
    
    public Style(HtmlTag htmlTag) {
        style = new HtmlAttribute("");
        htmlTag.attributes.put("style", style);
    }

    public void setHtmlAttribute(HtmlAttribute att) {
        style = att;
    }
    
    public Style width(int width){
        setAttribute("width",String.valueOf(width) + "px");
        return this;
    }
    
    public Style height(int height){
        setAttribute("height",String.valueOf(height) + "px");
        return this;
    }

    public Style margin(int margin){
        setAttribute("margin",String.valueOf(margin) + "px");
        return this;
    }
    
    public Style margin_bottom(int margin){
        setAttribute("margin-bottom",String.valueOf(margin) + "px");
        return this;
    }
    
    public Style margin_top(int margin){
        setAttribute("margin-top",String.valueOf(margin) + "px");
        return this;
    }
    
    public Style margin_left(int margin){
        setAttribute("margin-left",String.valueOf(margin) + "px");
        return this;
    }
    
    public Style margin_right(int margin){
        setAttribute("margin-right",String.valueOf(margin) + "px");
        return this;
    }
    
    private void setAttribute(String string, String value) {
        if(style.getValue().contains(string+":")){
            String st = style.getValue();
            String c = st.split(string+":")[1];
            if(c.contains(";")){
                c = c.split(";")[0];
            }
            style.setValue(st.replace(c, value));
        }else{
            style.setValue(style.getValue() + string + ":" + value + ";");
        }
    }

    public Style text_align(String align){
        setAttribute("text-align",align);
        return this;
    }

    public Style background_color(String color) {
        setAttribute("background-color",color);
        return this;
    }

    public Style custom(String string, String string2) {
        setAttribute(string,string2);
        return this;
    }

    @Override
    public Style clone(){
        try {
            return (Style) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Style Float(String string) {
        return custom("float",string);
    }
    
}
