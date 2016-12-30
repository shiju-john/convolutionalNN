package com.flytxt.imageprocessor.processor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.DataSet;

import com.flytxt.imageprocessor.config.Configuration;
import com.flytxt.imageprocessor.config.Logger;
import com.flytxt.imageprocessor.config.LoggerFactory;
import com.flytxt.imageprocessor.dataset.DataSetRecordIterator;

public class NetworkProcessor {
	
	private final MultiLayerNetwork multiLayerNetwork ; 	
	private final Configuration configuration;
	
	Logger log = LoggerFactory.getLogger(NetworkProcessor.class);
	private static final String FILE_PATH = "src/main/resources/network/network.zip";
	
	public NetworkProcessor(Configuration configuration){
		this.configuration = configuration;
		multiLayerNetwork =  new MultiLayerNetwork(configuration.getConfiguration());
		init();		
	}

	/**
	 * 
	 * @param configuration
	 * @param restoredNetwork
	 */
	private NetworkProcessor(Configuration configuration, MultiLayerNetwork restoredNetwork) {
		this.configuration = configuration;
		multiLayerNetwork =  restoredNetwork;
		multiLayerNetwork.setListeners(new ScoreIterationListener(1));
	}


	private void init() {
		getMultiLayerNetwork().init();
		getMultiLayerNetwork().setListeners(new ScoreIterationListener(1));		
	}
	
	
	/**
	 * 
	 * @param dataSetIterator
	 */
	public void training(DataSetRecordIterator dataSetIterator){
		if(null!=dataSetIterator.getDataIterator()){
			getMultiLayerNetwork().fit(dataSetIterator.getDataIterator());
		}else{
			log.error("Please add the training Data Before start the training");
		}
	}
	
	/**
	 * 
	 * @param dataSetIterator
	 * @return
	 */
	public String evaluate(DataSetRecordIterator dataSetIterator){
		Evaluation eval = getMultiLayerNetwork().evaluate(dataSetIterator.getDataIterator());
		eval.stats(true);		
		dataSetIterator.reset();
	    String predictVal =null;
	    for(;dataSetIterator.hasNext();){
	        DataSet testDataSet = dataSetIterator.next();
	        testDataSet.setLabelNames(dataSetIterator.getLabels());	        
	        List<String> predict = getMultiLayerNetwork().predict(testDataSet);
	        predictVal = predict.get(0);
	        System.out.println("Predication  :" + predictVal);
	    }
	    return  predictVal;
	}

	
	
	/**
	 * @return the multiLayerNetwork
	 */
	public MultiLayerNetwork getMultiLayerNetwork() {
		return multiLayerNetwork;
	}

	/**
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}
	
	/**
	 * 
	 * @param imageProcessor
	 */
	public void save () {		
		 File locationToSave = new File(FILE_PATH); 
		 boolean saveUpdater = true;  
		 try {
			ModelSerializer.writeModel(getMultiLayerNetwork(), locationToSave, saveUpdater);
		} catch (IOException e) {
			log.error("Unable to save the network"+ e.getMessage());			
		}
	}

	/**
	 * 
	 * @param configuration
	 * @return
	 * @throws IOException 
	 */
	public synchronized static NetworkProcessor load(Configuration configuration) throws IOException {
		MultiLayerNetwork restored = ModelSerializer.restoreMultiLayerNetwork(FILE_PATH);
		return new NetworkProcessor(configuration,restored);
	}

}
