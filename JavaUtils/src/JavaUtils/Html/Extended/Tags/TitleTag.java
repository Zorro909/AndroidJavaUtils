package JavaUtils.Html.Extended.Tags;

public class TitleTag extends ExtendedTag {


    /**
     * 
     * @param string
     * @param i Higher Value means smaller Text
     */
    public TitleTag(String string, int i) {
        super("h" + i);
        setValue(string);
    }

}
