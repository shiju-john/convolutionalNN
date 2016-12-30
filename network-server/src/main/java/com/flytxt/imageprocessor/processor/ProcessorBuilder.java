package com.flytxt.imageprocessor.processor;

import java.io.IOException;

import com.flytxt.imageprocessor.config.Configuration;
import com.flytxt.imageprocessor.config.Logger;
import com.flytxt.imageprocessor.config.LoggerFactory;
/**
 * 
 * @author shiju.john
 *
 */
public class ProcessorBuilder {
	
	Logger log = LoggerFactory.getLogger(ProcessorBuilder.class);
		

	/**
	 * 
	 * @param builderProperties
	 * @return
	 */
	public NetworkProcessor buildNetworkProcessor ( Configuration configuration) {				
		return new NetworkProcessor(configuration);
	}
	
	/**
	 * 
	 */
	public NetworkProcessor loadNetworkProcessor ( Configuration configuration) {		
		try {			
			return NetworkProcessor.load(configuration);
			
		} catch (IOException e) {
			log.error("Unable to load the network"+ e.getMessage());
		}		
		log.error("going to create new network");
		return this.buildNetworkProcessor(configuration);
	}

}
