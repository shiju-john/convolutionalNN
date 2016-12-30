package com.flytxt.imageprocessor.image;

import org.datavec.api.io.labels.PathLabelGenerator;
import org.datavec.api.split.InputSplit;
/**
 * Contains the splitted images  
 * @author shiju.john
 *
 */
public class inputDataSplit {
	
	private InputSplit inputSplit;
	private PathLabelGenerator labelGenerator;
	
	public inputDataSplit(InputSplit imageSplit, PathLabelGenerator labelGenerator){
		this.setLabelGenerator(labelGenerator);
		setInputSplit(imageSplit);		
	}
	
		
	/**
	 * @return the imageSplit
	 */
	public InputSplit getInputSplit() {
		return inputSplit;
	}

	/**
	 * @param imageSplit the imageSplit to set
	 */
	private void setInputSplit(InputSplit imageSplit) {
		this.inputSplit = imageSplit;
	}


	/**
	 * @return the labelGenerator
	 */
	public PathLabelGenerator getLabelGenerator() {
		return labelGenerator;
	}


	/**
	 * @param labelGenerator the labelGenerator to set
	 */
	private void setLabelGenerator(PathLabelGenerator labelGenerator) {
		this.labelGenerator = labelGenerator;
	}

}
