package JavaUtils.RestAPI;

import java.io.IOException;

import JavaUtils.Parser.ParseAble;
import JavaUtils.XML.XmlElement;

abstract class bxml extends XmlElement {

    public bxml(ParseAble f) throws IOException {
        super(f);
        // TODO Auto-generated constructor stub
    }

    public abstract byte[] toBytes();
    
    @Override
    public String decode(){
        return "bytes";
    }
}
