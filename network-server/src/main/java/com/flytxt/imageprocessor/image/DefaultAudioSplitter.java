package com.flytxt.imageprocessor.image;

import java.io.File;
import java.nio.file.Path;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.io.labels.PathLabelGenerator;
import org.datavec.api.split.FileSplit;

import com.flytxt.imageprocessor.audio.AudioProcessor;
import com.flytxt.imageprocessor.config.Configuration;
/**
 * 
 * @author shiju.john
 *
 */
public class DefaultAudioSplitter implements DataSplitter<inputDataSplit, Configuration> {

	AudioProcessor audioProcessor;
	private PathLabelGenerator labelGenerator = null;
	
	public DefaultAudioSplitter(AudioProcessor audioProcessor) {
		this.audioProcessor = audioProcessor;
		this.labelGenerator = new ParentPathLabelGenerator();
			
	}

	@Override
	public inputDataSplit getDataSplit(Path path, Configuration configuration) {
		
		File mainPath = path.toFile();			
		FileSplit fileSplit = new FileSplit(mainPath,  configuration.getRandom());
		
	/*	BalancedPathFilter pathFilter = new BalancedPathFilter(configuration.getRandom(), 
				labelGenerator, configuration.getInputCount(), configuration.getOutLabelCount(), 
				configuration.getBatchSize()); // ----- batch size 
		*/
		/*InputSplit[] inputSplit = fileSplit.sample(pathFilter, 1.0);
		InputSplit trainData = inputSplit[0];	*/	   
		return new inputDataSplit(fileSplit,labelGenerator);
	}

	@Override
	public inputDataSplit getEvalImageSplit(Path path, Configuration configuration) {
		File mainPath = path.toFile();			
		FileSplit fileSplit = new FileSplit(mainPath,  configuration.getRandom());
		return new inputDataSplit(fileSplit,labelGenerator);
	}

}
