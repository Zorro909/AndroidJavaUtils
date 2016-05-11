package JavaUtils.RestAPI;

import java.util.HashMap;

import JavaUtils.Parser.ParseObject;

public interface Action {

	public ParseObject executeRequest(HashMap<String,String> conf, HashMap<String,String> vars);

    public boolean isRaw();
	
}
