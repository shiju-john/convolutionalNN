package com.flytxt.imageprocessor.coordinator;

import java.io.IOException;

import com.flytxt.imageprocessor.Processor;
import com.flytxt.imageprocessor.config.Configuration;
import com.flytxt.imageprocessor.processor.NetworkProcessor;
import com.flytxt.imageprocessor.processor.ProcessorBuilder;
/**
 * 
 * @author shiju.john
 *
 */
public enum LOAD_STRATEGY {
	CREATE {
		@Override
		public NetworkProcessor getProcessor(Configuration configuration,Processor imageProcessor) throws IOException, InterruptedException {
			NetworkProcessor networkProcessor= initializeProcessor(configuration);
			networkProcessor.training(imageProcessor.getTrainingData());
			networkProcessor.save();
			return networkProcessor;
		}
	},
	LOAD {
		@Override
		public NetworkProcessor getProcessor(Configuration configuration,Processor imageProcessor) throws IOException, InterruptedException {
			NetworkProcessor networkProcessor = new ProcessorBuilder().loadNetworkProcessor(configuration);
			networkProcessor.training(imageProcessor.getTrainingData());
			return networkProcessor;
		}
	},
	LOADTRAIN {
		@Override
		public NetworkProcessor getProcessor(Configuration configuration,Processor imageProcessor) throws IOException, InterruptedException {
			NetworkProcessor networkProcessor  = LOAD.getProcessor(configuration,imageProcessor);
			networkProcessor.training(imageProcessor.getTrainingData());
			networkProcessor.save();
			return networkProcessor;
		}
	};
	
	public abstract NetworkProcessor getProcessor(Configuration configuration, Processor imageProcessor) throws IOException,InterruptedException;

	/**
	 * 
	 * @param config
	 */
	private static NetworkProcessor initializeProcessor(Configuration config) {
		NetworkProcessor networkProcessor = new ProcessorBuilder().buildNetworkProcessor(config);
		return networkProcessor;
	}
}
