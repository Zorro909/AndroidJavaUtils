package JavaUtils.Parser;

import java.util.ArrayList;
import java.util.LinkedList;

import JavaUtils.XML.NoElementFoundException;

public interface Section {

	public Section getSection(String name) throws NoElementFoundException;
	public String getValue(String name) throws NoElementFoundException;
	public LinkedList<ParseAble> getParseAble(String string);
	public ArrayList<String> getStringList(String string) throws NoElementFoundException;
}
