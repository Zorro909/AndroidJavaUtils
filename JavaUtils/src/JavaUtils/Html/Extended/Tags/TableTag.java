package JavaUtils.Html.Extended.Tags;

import java.util.ArrayList;
import java.util.HashMap;

import JavaUtils.Html.HtmlTag;

public class TableTag extends ExtendedTag implements Cloneable{

    HtmlTag tbody;
    HtmlTag firstRow;
    HashMap<Integer,HtmlTag> rows = new HashMap<Integer,HtmlTag>();
    
    public TableTag() {
        super("table");
        tbody = new HtmlTag("tbody");
        addTag(tbody);
        firstRow = new HtmlTag("tr");
        tbody.addTag(firstRow);
    }

    public void addColumn(String string) {
        firstRow.addTag(new HtmlTag("td").setValue(string));
    }

    public void addRow(String... row){
        HtmlTag r = new HtmlTag("tr");
        tbody.addTag(r);
        for(String s : row){
            r.addTag(new HtmlTag("td").setValue(s));
        }
        rows.put(rows.size(), r);
    }
    
    public HtmlTag getRow(int i){
        if(i==-1)return firstRow;
        return rows.get(i);
    }
    
    @Override
    public TableTag clone(){
        TableTag tt = (TableTag) super.clone();
        tt.tbody = tt.getTag("tbody").get(0);
        tt.firstRow = tt.tbody.getTag("tr").get(0);
        ArrayList<HtmlTag> r = tt.tbody.getTag("tr");
        tt.rows = new HashMap<Integer,HtmlTag>();
        for(int i = 1;i<=rows.size();i++){
            tt.rows.put(i-1, r.get(i));
        }
        return tt;
        
    }
    
}
