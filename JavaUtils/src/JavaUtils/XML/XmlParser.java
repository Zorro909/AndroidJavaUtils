package JavaUtils.XML;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import JavaUtils.HTTPManager.Connection;
import JavaUtils.HTTPManager.InetManager;
import JavaUtils.Parser.Parser;
import JavaUtils.Parser.Section;
import JavaUtils.UtilHelpers.FileUtils;

public class XmlParser extends XmlElement implements Parser{
	
	public XmlParser(String xml){
		super("root",null,xml);
	}
	
	public XmlParser(File f) throws IOException{
		super("root",null,FileUtils.readAll(f));
	}
	
	public XmlParser(URL url) throws IOException{
		super("root",null,InetManager.openConnection(url).initGet(false, new HashMap<String,String>()).get());
	}
	
	public XmlParser(Connection c) throws IOException{
		super("root",null,c.get());
	}
	
	public XmlParser(String xml, XmlParserEngine engine){
		super("root",null,xml,engine);
	}
	
	public XmlParser(File f, XmlParserEngine engine) throws IOException{
		super("root",null,FileUtils.readAll(f),engine);
	}
	
	public XmlParser(URL url, XmlParserEngine engine) throws IOException{
		super("root",null,InetManager.openConnection(url).initGet(false, new HashMap<String,String>()).get(),engine);
	}
	
	public XmlParser(Connection c, XmlParserEngine engine) throws IOException{
		super("root",null,c.get(),engine);
	}

	@Deprecated
	@Override
	public Section getSection(String name) throws NoElementFoundException {
		return super.getSections(name).get(0);
	}
	
}
