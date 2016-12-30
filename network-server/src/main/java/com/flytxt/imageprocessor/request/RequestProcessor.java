package com.flytxt.imageprocessor.request;

import java.util.List;
/**
 * 
 * @author shiju.john
 *
 */
public interface RequestProcessor {
	
	
	
	/**
	 * Predict single input from the given path  
	 * @param path
	 * @return
	 */
	public String predict(String path);
	
	
	/**
	 * Predict all the inputs from the given path 
	 * @param path
	 * @return
	 */
	public List<String> evaluates(String path);

}
