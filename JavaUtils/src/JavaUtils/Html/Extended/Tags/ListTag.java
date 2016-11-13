package JavaUtils.Html.Extended.Tags;

import java.util.List;

import JavaUtils.Html.HtmlTag;

public class ListTag extends ExtendedTag {

    public ListTag() {
        super("ul");
    }
    
    public int addEntry(String entry){
        addTag(new HtmlTag("li").setValue(entry));
        return tags.size()-1;
    }
    
    public HtmlTag getEntry(int i){
        return tags.get(i);
    }
    
    public void addAll(String[] entries){
        for(String s : entries){
            addEntry(s);
        }
    }
    
    public void addAll(List<String> entries){
        for(String s : entries){
            addEntry(s);
        }
    }
    
    public static ListTag toList(String[] entries){
        ListTag l = new ListTag();
        for(String s : entries){
            l.addEntry(s);
        }
        return l;
    }
    
    public static ListTag toList(List<String> entries){
        ListTag l = new ListTag();
        for(String s : entries){
            l.addEntry(s);
        }
        return l;
    }

}
