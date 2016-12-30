package com.flytxt.imageprocessor.coordinator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flytxt.imageprocessor.audio.AudioProcessor;
import com.flytxt.imageprocessor.config.Configuration;
import com.flytxt.imageprocessor.config.ConfigurationBuilder;
import com.flytxt.imageprocessor.dataset.DataSetRecordIterator;
import com.flytxt.imageprocessor.processor.NetworkProcessor;

public class AudioCoordinator {
	

	private static  volatile AudioCoordinator me = null;
	private NetworkProcessor networkProcessor ;
	private AudioProcessor audioProcessor ;
	private Configuration configuration ;
	
	private Path traingPath= Paths.get("src/main/resources/dataset/audio");
	
		
	
	/**
	 * strategy 
	 * @param stratgey
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private AudioCoordinator(LOAD_STRATEGY strategy) throws IOException, InterruptedException{
		this.configuration = new ConfigurationBuilder().build(getProperties());
		this.audioProcessor = getAudioProcessor();
		networkProcessor = strategy.getProcessor(configuration,audioProcessor);	
	}
	
	
	/**
	 * 
	 * @param configuration
	 */
	private AudioProcessor getAudioProcessor() {
		return  new AudioProcessor(configuration,traingPath);	
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getProperties() {
		Map<String, Object> builderProperties = new HashMap<>();
		builderProperties.put("OUT_LABEL_COUNT", 3);
		builderProperties.put("EPOCHS", 1);
		builderProperties.put(Configuration.BATCH_SIZE, 1);
		builderProperties.put(Configuration.TOTAL_INPUT_COUNT, 6);
		return builderProperties;
	}

	

	/**
	 * 
	 * @param path
	 * @return
	 */
	public String predict(String path) {
		DataSetRecordIterator dataSetIterator;
		try {
			dataSetIterator = audioProcessor.getEvaluationData(path);
			return networkProcessor.evaluate(dataSetIterator);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}


	public List<String> evaluates(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static AudioCoordinator getInstance(LOAD_STRATEGY stratgey) throws IOException, InterruptedException{
		if(me==null){
			synchronized (AudioCoordinator.class) {
				if(null==me){
					me = new AudioCoordinator(stratgey);
				}
			}
		}
		return me;
	}
	

	

}
