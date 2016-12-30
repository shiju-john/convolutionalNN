package com.flytxt.imageprocessor;

import java.io.IOException;

import com.flytxt.imageprocessor.dataset.DataSetRecordIterator;

public interface Processor {
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public DataSetRecordIterator getEvaluationData(String path) throws IOException,InterruptedException;
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public DataSetRecordIterator getTrainingData() throws IOException, InterruptedException;

}
