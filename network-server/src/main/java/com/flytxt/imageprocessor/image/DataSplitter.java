package com.flytxt.imageprocessor.image;

import java.nio.file.Path;
/**
 * 
 * @author shiju.john
 *
 * @param <T>
 * @param <E>
 */
public interface DataSplitter<T,E> {
	
	public T getDataSplit(Path path, E e);
	
	public T getEvalImageSplit(Path path, E e);
	
	

}
 