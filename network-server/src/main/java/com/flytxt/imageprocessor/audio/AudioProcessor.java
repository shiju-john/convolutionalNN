package com.flytxt.imageprocessor.audio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.audio.formats.input.WavInputFormat;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.MultipleEpochsIterator;

import com.flytxt.imageprocessor.Processor;
import com.flytxt.imageprocessor.config.Configuration;
import com.flytxt.imageprocessor.dataset.DataSetRecordIterator;
import com.flytxt.imageprocessor.image.DataSplitter;
import com.flytxt.imageprocessor.image.SplitterFactory;
import com.flytxt.imageprocessor.image.inputDataSplit;
/**
 * @author shiju.john
 *
 */
public class AudioProcessor implements Processor{
	
	private final Configuration configuration ;
	private final Path traingPath;

	public AudioProcessor(Configuration configuration, Path traingPath) {
		this.configuration = configuration;
		this.traingPath = traingPath;
	}

	
	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public DataSetRecordIterator getTrainingData() throws IOException, InterruptedException {
		
		
		inputDataSplit audioSplit = getInputData();
		RecordReader recordReader = new WavInputFormat().createReader(audioSplit.getInputSplit());
		
		recordReader = new AudioRecordReader();
		recordReader.initialize(audioSplit.getInputSplit());

		// setLabels(recordReader.getLabels());
		
		 /*SequenceRecordReaderDataSetIterator sequenceIter =
	                new SequenceRecordReaderDataSetIterator(recordReader, configuration.getBatchSize(), configuration.getOutLabelCount(), 1);
		*/
		 
		 RecordReaderDataSetIterator dataIterator = new RecordReaderDataSetIterator(recordReader,
				configuration.getBatchSize(), 1, configuration.getOutLabelCount());

		// DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
		// scaler.fit(dataIterator);
		// dataIterator.setPreProcessor(scaler);

		MultipleEpochsIterator trainIter = new MultipleEpochsIterator(configuration.getEpochs(), dataIterator,
				Runtime.getRuntime().availableProcessors());

		DataSetRecordIterator dataSetIterator = new DataSetRecordIterator();
		dataSetIterator.setDataIterator(trainIter);

		return dataSetIterator;
	}
	
	
	/**
	 * 
	 */
	public DataSetRecordIterator getEvaluationData(String path) throws IOException, InterruptedException {
		
		DataSplitter<inputDataSplit, Configuration> imageSplitter = SplitterFactory.getDefaultAudioSpliter(this);
		inputDataSplit audioSplit = imageSplitter.getDataSplit(Paths.get(path), configuration);

		RecordReader recordReader = new WavInputFormat().createReader(audioSplit.getInputSplit());
		RecordReaderDataSetIterator dataIterator = new RecordReaderDataSetIterator(recordReader,
				configuration.getBatchSize(), 1, configuration.getOutLabelCount());// ----------------------

		DataSetRecordIterator dataSetIterator = new DataSetRecordIterator();
		dataSetIterator.setDataIterator(dataIterator);
		// imageDataSetIterator.setLabels(getLabels());
		return dataSetIterator;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private inputDataSplit getInputData() {
		DataSplitter<inputDataSplit , Configuration>  audioSplitter = SplitterFactory.getDefaultAudioSpliter(this);
		return  audioSplitter.getDataSplit(traingPath, configuration);		
	}

}
