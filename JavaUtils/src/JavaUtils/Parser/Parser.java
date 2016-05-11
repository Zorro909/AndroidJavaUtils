package JavaUtils.Parser;

import JavaUtils.XML.NoElementFoundException;

public interface Parser {

	public Section getSection(String name) throws NoElementFoundException;
	public String getValue(String name) throws NoElementFoundException;
	
}
