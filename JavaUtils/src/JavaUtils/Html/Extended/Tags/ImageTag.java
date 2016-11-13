package JavaUtils.Html.Extended.Tags;

import java.awt.Image;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.ImageIcon;

import JavaUtils.Html.HtmlTag;
import JavaUtils.Html.Style;
import JavaUtils.RestAPI.ImageAction;
import JavaUtils.RestAPI.RestAPIActionSet;

public class ImageTag extends ExtendedTag implements Cloneable{

    static ArrayList<String> imageUUIDs = new ArrayList<String>(); 
    
    public ImageTag(RestAPIActionSet raas, Image img) {
        this(raas,new ImageIcon(img));
    }
    
    public ImageTag(RestAPIActionSet raas, ImageIcon img) {
        this(raas,img,img.getIconWidth(),img.getIconHeight());
    }
    
    public ImageTag(RestAPIActionSet raas, ImageIcon img, int width, int height){
        this(raas,img.getImage(),width,height);
    }
    
    public ImageTag(RestAPIActionSet raas, Image img, int width, int height){
        super("img");
        Style s = getStyle();
        s.width(width);
        s.height(height);
        UUID uid = UUID.randomUUID();
        while(imageUUIDs.contains(uid.toString())){
            uid = UUID.randomUUID();
        }
        imageUUIDs.add(uid.toString());
        String url = "images/" + uid.toString();
        raas.addAction(url, new ImageAction(img));
    }

    
    
}
