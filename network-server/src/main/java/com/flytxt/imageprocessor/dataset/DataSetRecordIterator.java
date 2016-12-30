package com.flytxt.imageprocessor.dataset;

import java.util.List;

import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

/**
 * 
 * @author shiju.john
 *
 */
public class DataSetRecordIterator {
	
	
	private DataSetIterator dataIterator  = null;
	private List<String> labels;
	
	
	/**
	 * @return the dataIterator
	 */
	public DataSetIterator getDataIterator() {
		return dataIterator;
	}

	/**
	 * @param dataIterator the dataIterator to set
	 */
	public void setDataIterator(DataSetIterator dataIterator) {
		this.dataIterator = dataIterator;
	}
	
	
	/**
	 * 
	 */
	public void reset() {
		if(null!=dataIterator){
			dataIterator.reset();
		}	
	}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean hasNext() {
		return dataIterator.hasNext();
	}
	
	/**
	 * 
	 * @return
	 */
	public DataSet next() {
		return dataIterator.next();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getLabels() {
		return labels;
	}
	
	/**
	 * 
	 * @return
	 */
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

}
