package com.flytxt.imageprocessor.coordinator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flytxt.imageprocessor.config.Configuration;
import com.flytxt.imageprocessor.config.ConfigurationBuilder;
import com.flytxt.imageprocessor.config.ImageConfiguration;
import com.flytxt.imageprocessor.dataset.DataSetRecordIterator;
import com.flytxt.imageprocessor.processor.ImageProcessor;
import com.flytxt.imageprocessor.processor.NetworkProcessor;
/**
 * 
 * @author shiju.john
 *
 */
public class ImageCoordinator {
	
	private static  volatile ImageCoordinator me = null;
	private NetworkProcessor networkProcessor ;
	private ImageProcessor imageProcessor ;
	private Configuration configuration ;
	
	private Path traingPath= Paths.get("src/main/resources/dataset");
	
		
	
	/**
	 * strategy 
	 * @param stratgey
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private ImageCoordinator(LOAD_STRATEGY strategy) throws IOException, InterruptedException{
		this.configuration = new ConfigurationBuilder().build(getProperties());
		this.imageProcessor = getImageProcessor();
		networkProcessor = strategy.getProcessor(configuration,imageProcessor);	
	}
	
	
	/**
	 * 
	 * @param configuration
	 */
	private ImageProcessor getImageProcessor() {
		return  new ImageProcessor(configuration,getImageConfiguration(),traingPath);	
	}

	/**
	 * 
	 * @return
	 */
	public  Map<String, Object> getProperties() {		
		Map<String, Object> builderProperties = new HashMap<>();
		builderProperties.put("OUT_LABEL_COUNT",6);
		builderProperties.put("EPOCHS",1);
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
			dataSetIterator = imageProcessor.getEvaluationData(path);
			return networkProcessor.evaluate(dataSetIterator);
		} catch (IOException e) {
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
	public static ImageCoordinator getInstance(LOAD_STRATEGY stratgey) throws IOException, InterruptedException{
		if(me==null){
			synchronized (ImageCoordinator.class) {
				if(null==me){
					me = new ImageCoordinator(stratgey);
				}
			}
		}
		return me;
	}
	

	
	/**
	 * 
	 * @return
	 */
	private ImageConfiguration getImageConfiguration() {
		ImageConfiguration imageConfig = new ImageConfiguration();
		imageConfig.addConfiguration(ImageConfiguration.TOTAL_IMAGE_COUNT, 576);
		imageConfig.addConfiguration("BATCH_SIZE",96);
		return imageConfig;
	}
	
}
