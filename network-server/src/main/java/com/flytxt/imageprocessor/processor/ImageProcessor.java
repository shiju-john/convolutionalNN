package com.flytxt.imageprocessor.processor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.MultipleEpochsIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import com.flytxt.imageprocessor.Processor;
import com.flytxt.imageprocessor.config.Configuration;
import com.flytxt.imageprocessor.config.ImageConfiguration;
import com.flytxt.imageprocessor.config.Logger;
import com.flytxt.imageprocessor.config.LoggerFactory;
import com.flytxt.imageprocessor.dataset.DataSetRecordIterator;
import com.flytxt.imageprocessor.image.inputDataSplit;
import com.flytxt.imageprocessor.image.DataSplitter;
import com.flytxt.imageprocessor.image.SplitterFactory;
/**
 * 
 * @author shiju.john
 *
 */
public class ImageProcessor implements Processor{

	
	private final Configuration configuration;	
	private final Path path;
	private final ImageConfiguration imageConfig;
	private List<String> labels = null ;
	
	Logger log = LoggerFactory.getLogger(ImageProcessor.class);
	

	
	/**
	 * 
	 * @param configuration
	 */
	public ImageProcessor(Configuration configuration,ImageConfiguration imageConfig,Path path) {		
		this.configuration = configuration;	
		this.imageConfig = imageConfig;	
		this.path = path;
	}
	
	

	/**
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

		
	/**
	 * 
	 * @param path
	 * @param imageConfig
	 * @return
	 * @throws IOException
	 */
	public List<String> getLabelFromTrainingData() throws IOException{
		
		DataSplitter<inputDataSplit , ImageConfiguration>  imageSplitter = SplitterFactory.getDefaultImageSpliter(null, this);
		inputDataSplit imageSplit = imageSplitter.getDataSplit(path, imageConfig);	
		
		try(ImageRecordReader recordReader = new ImageRecordReader(imageConfig.getImageHeight(), imageConfig.getImageWidth(), 
				getConfiguration().getChannel(), imageSplit.getLabelGenerator())){
			recordReader.initialize(imageSplit.getInputSplit());
			this.labels =  recordReader.getLabels();
			return labels;
		}
		
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public DataSetRecordIterator getTrainingData() throws IOException {
		
		
		inputDataSplit  imageSplit = getInputData(path,imageConfig);
		
		ImageRecordReader recordReader = new ImageRecordReader(imageConfig.getImageHeight(), imageConfig.getImageWidth(), 
				getConfiguration().getChannel(), imageSplit.getLabelGenerator());
		recordReader.initialize(imageSplit.getInputSplit(), null);		
			
		setLabels(recordReader.getLabels());
		RecordReaderDataSetIterator dataIterator = new RecordReaderDataSetIterator(recordReader, 
							imageConfig.getBatchSize(),	1, configuration.getOutLabelCount()); 	
		
		DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
		scaler.fit(dataIterator);	
		dataIterator.setPreProcessor(scaler);
		
		
		MultipleEpochsIterator trainIter = new MultipleEpochsIterator(configuration.getEpochs(), 
				dataIterator, Runtime.getRuntime().availableProcessors());
		
		DataSetRecordIterator dataSetIterator = new DataSetRecordIterator();
		dataSetIterator.setDataIterator(trainIter);
		
		return dataSetIterator;
	}
	
	
	
	private inputDataSplit getInputData(Path path,ImageConfiguration config) {
		DataSplitter<inputDataSplit , ImageConfiguration>  imageSplitter = SplitterFactory.getDefaultImageSpliter(null, this);
		return  imageSplitter.getDataSplit(path, config);		
	}


	/**
	 * 
	 * @param evalPath
	 * @return
	 * @throws IOException 
	 */
	public DataSetRecordIterator getEvaluationData(String evalPath) throws IOException {
		
		DataSplitter<inputDataSplit , ImageConfiguration>  imageSplitter = SplitterFactory.getDefaultImageSpliter(null, this);
		inputDataSplit imageSplit = imageSplitter.getDataSplit(Paths.get(evalPath), imageConfig);	
		
		
		ImageRecordReader recordReader = new ImageRecordReader(imageConfig.getImageHeight(), imageConfig.getImageWidth(), 
				getConfiguration().getChannel(), imageSplit.getLabelGenerator());	
		
		recordReader.initialize(imageSplit.getInputSplit());
		RecordReaderDataSetIterator dataIterator = new RecordReaderDataSetIterator(recordReader, 
								imageConfig.getBatchSize(), 1,configuration.getOutLabelCount());//----------------------
					
		DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
		scaler.fit(dataIterator);		
        dataIterator.setPreProcessor(scaler);
        
        DataSetRecordIterator imageDataSetIterator = new DataSetRecordIterator();
        imageDataSetIterator.setDataIterator(dataIterator);
        imageDataSetIterator.setLabels(getLabels());
        return imageDataSetIterator; 
		
	}
	
	/**
	 * 
	 * @param labels
	 */
	private void setLabels(List<String> labels) {
		this.labels = labels;		
	}

	/**
	 * @return the labels
	 * @throws IOException 
	 */
	public List<String> getLabels() throws IOException {
		return null!=labels ? labels:getLabelFromTrainingData();
		
	}



	

}
