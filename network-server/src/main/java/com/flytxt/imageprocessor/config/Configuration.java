package com.flytxt.imageprocessor.config;

import java.util.Map;
import java.util.Random;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.distribution.Distribution;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.lossfunctions.LossFunctions;


public class Configuration {
	
	private MultiLayerConfiguration configuration = null;
	
	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(Configuration.class);
	
	/** For generating the random number seed. Value should be an integer  */ 
	private static final String RANDOM_SEED ="SEED";
	private static final String ITERATION ="ITERATION";
	private static final String OUT_LABEL_COUNT ="OUT_LABEL_COUNT";
	
	private static final String EPOCHS ="EPOCHS";
	
	private static final  long DEFAULT_SEED = 42;	
	private static final  int DEFAULT_ITERATIION = 1;
	private static final  int DEFAULT_CHANNEL = 1;
	
	public static final String TOTAL_INPUT_COUNT = "TOTAL_INPUT_COUNT";
	public static final String IMAGE_HEIGHT = "IMAGE_HEIGHT";
	public static final String IMAGE_WIDTH ="IMAGE_WIDTH";
	public static final String BATCH_SIZE = "BATCH_SIZE";

	private static final String RANDAM = "RANDAM";	
	
	private Map<String,Object> builderProperties = null;
	
	double nonZeroBias = 1;
    double dropOut = 0.5;
	
    public Configuration(Map<String,Object> builderProperties,boolean ladConfig){
    	this. builderProperties = builderProperties;
    	if(ladConfig){
    		configure();
    	}
    }

	private void  configure () {
			/*setConfiguration(new NeuralNetConfiguration.Builder()
            .seed(getSeedValue())
            .weightInit(WeightInit.DISTRIBUTION)
            .dist(new NormalDistribution(0.0, 0.01))
            .activation("relu")
            .updater(Updater.NESTEROVS)
            .iterations(getIteration())
            .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer) 
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .learningRate(1e-2)
            .biasLearningRate(1e-2*2)
            .learningRateDecayPolicy(LearningRatePolicy.Step)
            .lrPolicyDecayRate(0.1)
            .lrPolicySteps(100000)
            .regularization(true)
            .l2(5 * 1e-4)
            .momentum(0.9)
            .miniBatch(false)
            .list()
            .layer(0, convInit("cnn1", DEFAULT_CHANNEL, 96, new int[]{11, 11}, new int[]{2, 2}, new int[]{3, 3}, 0))
            .layer(1, new LocalResponseNormalization.Builder().name("lrn1").build())
            .layer(2, maxPool("maxpool1", new int[]{3,3}))
            .layer(3, conv5x5("cnn2", 256, new int[] {1,1}, new int[] {2,2}, nonZeroBias))
            .layer(4, new LocalResponseNormalization.Builder().name("lrn2").build())
            .layer(5, maxPool("maxpool2", new int[]{3,3}))
            .layer(6,conv3x3("cnn3", 384, 0))
            .layer(7,conv3x3("cnn4", 384, nonZeroBias))
            .layer(8,conv3x3("cnn5", 256, nonZeroBias))
            .layer(9, maxPool("maxpool3", new int[]{3,3}))
            .layer(10, fullyConnected("ffn1", 4096, nonZeroBias, dropOut, new GaussianDistribution(0, 0.005)))
            .layer(11, fullyConnected("ffn2", 4096, nonZeroBias, dropOut, new GaussianDistribution(0, 0.005)))
            .layer(12, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .name("output")
                .nIn(1)
                .nOut(getOutLabelCount())
                .activation("softmax")
                .build())
            .backprop(true)
            .pretrain(false).build());*/
          // .cnnInputSize(100,100,DEFAULT_CHANNEL).build());
		
		
		 
		MultiLayerConfiguration conf =  new NeuralNetConfiguration.Builder()
	                .seed(getSeedValue())
	                .iterations(getIteration()) // Training iterations as above
	                .regularization(true).l2(0.0005)
	                /*
	                    Uncomment the following for learning decay and bias
	                 */
	                .learningRate(.01)//.biasLearningRate(0.02)
	                //.learningRateDecayPolicy(LearningRatePolicy.Inverse).lrPolicyDecayRate(0.001).lrPolicyPower(0.75)
	                .weightInit(WeightInit.XAVIER)
	                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
	                .updater(Updater.NESTEROVS).momentum(0.9)
	                .list()
	                .layer(0, new ConvolutionLayer.Builder(1, 5)
	                        //nIn and nOut specify depth. nIn here is the nChannels and nOut is the number of filters to be applied
	                        .nIn(getChannel())
	                        .name("layer0")
	                        .stride(1, 1)
	                        .padding(2,2)
	                        .nOut(20)
	                        .activation("identity")
	                        .build())
	                .layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
	                		.name("layer1")
	                        .kernelSize(1,2)
	                        .stride(1,1)
	                        .padding(1,1)
	                        .build())
	                .layer(2, new ConvolutionLayer.Builder(1, 5)
	                        //Note that nIn need not be specified in later layers
	                		.name("layer2")
	                		.stride(1, 1)
	                        .padding(2,2)
	                        .nIn(getChannel())
	                        .nOut(50)
	                        .activation("identity")
	                        .build())
	                .layer(3, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
	                		.name("layer3")
	                		.kernelSize(1,2)
	                        .stride(1,1)
	                        .padding(1,1)
	                        .build())
	                .layer(4, new DenseLayer.Builder().activation("relu")
	                		.name("layer4")
	                		 .nIn(getChannel())
	                        .nOut(500).build())
	                .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
	                        .name("output")	                       
	                        .nIn(getChannel())
	                        .nOut(getOutLabelCount())
	                        .activation("softmax")
	                        .build())
	                //.setInputType(InputType.convolutionalFlat(1,15359,1)) //See note below
	               // .setInputType(InputType.convolutionalFlat(1,15359,1)) //See note below
	                //.backprop(true)
	                .pretrain(false)
	                
	                .build();
				setConfiguration(conf);
       
	}
	
	/**
	 * 
	 * @return
	 */
	public int getOutLabelCount() {
		if(null!=builderProperties.get(OUT_LABEL_COUNT)){
    		return (int)builderProperties.get(OUT_LABEL_COUNT);
    	}else{
    		log.error("Out Label count is mandatory" );
    	}
		return 1;
	}

	/**
	 * 
	 * @return
	 */
	private int getIteration() {
		if(null!=builderProperties.get(ITERATION)){
    		return (int)builderProperties.get(ITERATION);
    	}else{
    		log.info("Iteration count is not properly initialized  . Going to set the default Iteration Count : "+DEFAULT_ITERATIION );
    	}
		return DEFAULT_ITERATIION;
	}


	/**
	 * 
	 * @param builderProperties
	 * @return
	 */
    public long  getSeedValue() {
    	if(null!=builderProperties.get(RANDOM_SEED)){
    		return (int)builderProperties.get(RANDOM_SEED);
    	}else{
    		log.info("Seed is not properly initialized. Going to set the default seed value: "+DEFAULT_SEED );
    	}
		return DEFAULT_SEED;
	}


	private ConvolutionLayer convInit(String name, int in, int out, int[] kernel, int[] stride, int[] pad, double bias) {
        return new ConvolutionLayer.Builder(kernel, stride, pad).name(name).nIn(3).nOut(out).biasInit(bias).build();
    }

    private ConvolutionLayer conv3x3(String name, int out, double bias) {
        return new ConvolutionLayer.Builder(new int[]{3,3}, new int[] {1,1}, new int[] {1,1}).name(name).nIn(3).nOut(out).biasInit(bias).build();
    }

    private ConvolutionLayer conv5x5(String name, int out, int[] stride, int[] pad, double bias) {
        return new ConvolutionLayer.Builder(new int[]{5,5}, stride, pad).name(name).nIn(3).nOut(out).biasInit(bias).build();
    }

    private SubsamplingLayer maxPool(String name,  int[] kernel) {
        return new SubsamplingLayer.Builder(kernel, new int[]{2,2}).name(name).build();
    }

    
    private DenseLayer fullyConnected(String name, int out, double bias, double dropOut, Distribution dist) {
        return new DenseLayer.Builder().name(name).nIn(3).nOut(out).biasInit(bias).dropOut(dropOut).dist(dist).build();
    }
    
    
	/**
	 * @return the configuration
	 */
	public MultiLayerConfiguration getConfiguration() {
		return configuration;
	}
	
	
	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(MultiLayerConfiguration configuration) {
		this.configuration = configuration;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Random getRandom() {
		if(null==builderProperties.get(RANDAM)){
			builderProperties.put(RANDAM, new Random(getSeedValue()));   		
    	}
		return (Random)builderProperties.get(RANDAM);
	}

	public int getEpochs() {
		if(null!=builderProperties.get(EPOCHS)){
	    	return (int)builderProperties.get(EPOCHS);
	    }else{
	    	log.info("Seed is not properly initialized. Going to set the default seed value: "+ 4 );
	    }
		return 4;		
	}

	public int getChannel() {
		return DEFAULT_CHANNEL;
	}

	public int getBatchSize() {
		if(builderProperties.containsKey(BATCH_SIZE)){
			return (int)builderProperties.get(BATCH_SIZE);
		}
		return 5;
	}

	public int getInputCount() {
		return (int)builderProperties.get(TOTAL_INPUT_COUNT);
		
	}

}
