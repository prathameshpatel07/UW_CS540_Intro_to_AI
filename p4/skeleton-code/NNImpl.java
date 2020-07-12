import java.util.*;

/**
 * The main class that handles the entire network
 * Has multiple attributes each with its own use
 */

public class NNImpl {
    private ArrayList<Node> inputNodes; //list of the output layer nodes.
    private ArrayList<Node> hiddenNodes;    //list of the hidden layer nodes
    private ArrayList<Node> outputNodes;    // list of the output layer nodes

    private ArrayList<Instance> trainingSet;    //the training set
    
    public int hiddenNodeCount;
    public int inputNodeCount;
    public int outputNodeCount;
    private double learningRate;    // variable to store the learning rate
    private int maxEpoch;   // variable to store the maximum number of epochs
    private Random random;  // random number generator to shuffle the training set

    /**
     * This constructor creates the nodes necessary for the neural network
     * Also connects the nodes of different layers
     * After calling the constructor the last node of both inputNodes and
     * hiddenNodes will be bias nodes.
     */

    NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount, Double learningRate, int maxEpoch, Random random, Double[][] hiddenWeights, Double[][] outputWeights) {
        this.trainingSet = trainingSet;
        this.learningRate = learningRate;
        this.maxEpoch = maxEpoch;
        this.random = random;
        this.inputNodeCount = trainingSet.get(0).attributes.size();
        this.hiddenNodeCount = hiddenNodeCount;
        this.outputNodeCount = trainingSet.get(0).classValues.size();
        //input layer nodes
        inputNodes = new ArrayList<>();

        for (int i = 0; i < inputNodeCount; i++) {
            Node node = new Node(0);
            inputNodes.add(node);
        }

        //bias node from input layer to hidden
        Node biasToHidden = new Node(1);
        inputNodes.add(biasToHidden);

        //hidden layer nodes
        hiddenNodes = new ArrayList<>();
        for (int i = 0; i < hiddenNodeCount; i++) {
            Node node = new Node(2);
            //Connecting hidden layer nodes with input layer nodes
            for (int j = 0; j < inputNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(inputNodes.get(j), hiddenWeights[i][j]);
                node.parents.add(nwp);
            }
            hiddenNodes.add(node);
        }

        //bias node from hidden layer to output
        Node biasToOutput = new Node(3);
        hiddenNodes.add(biasToOutput);

        //Output node layer
        outputNodes = new ArrayList<>();
        for (int i = 0; i < outputNodeCount; i++) {
            Node node = new Node(4);
            //Connecting output layer nodes with hidden layer nodes
            for (int j = 0; j < hiddenNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(hiddenNodes.get(j), outputWeights[i][j]);
                node.parents.add(nwp);
            }
            outputNodes.add(node);
        }
       
        for (int i = 0; i < hiddenNodeCount; i++) {           
            for (int j = 0; j < outputNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(outputNodes.get(j), outputWeights[j][i]);
                hiddenNodes.get(i).child.add(nwp);
            }
        }        
        
    }

    /**
     * Get the prediction from the neural network for a single instance
     * Return the idx with highest output values. For example if the outputs
     * of the outputNodes are [0.1, 0.5, 0.2], it should return 1.
     * The parameter is a single instance
     */
    
    public int predict(Instance instance) {
    	//Forward Pass
		int outputindex = 0;
		double maxvalue = Double.NEGATIVE_INFINITY;
		for(int i=0;i<inputNodeCount;i++)
		{
			inputNodes.get(i).setInput(instance.attributes.get(i));
		}
		
		for(Node hnode : hiddenNodes)
		{
			hnode.calculateOutput();
		}
		
		double softmaxsum = 0;
		for(Node onode: outputNodes)
		{
			onode.calculateOutput();
			softmaxsum += Math.exp(onode.getOutput());    				
		}
		
		for(Node softmaxonode: outputNodes)
		{
			softmaxonode.softmaxfunc(softmaxsum);
		}
		
		for(int i=0;i<outputNodeCount;i++)
		{
			if(outputNodes.get(i).outputactvalue > maxvalue)
				{
					outputindex = i;	
					maxvalue = outputNodes.get(i).outputactvalue;
				}
		}
		return outputindex;
    }


    /**
     * Train the neural networks with the given parameters
     * <p>
     * The parameters are stored as attributes of this class
     */

    public void train() {
        // TODO: add code here
    	for(int epochcount = 0; epochcount<maxEpoch; epochcount++)
    	{
    		Collections.shuffle(trainingSet, random);
    		for(int i=0;i<trainingSet.size();i++)
    		{
    			Instance instance = trainingSet.get(i);
    			
    			predict(instance);
    			int idx = 0;
    			idx = 0;
    			for(Node onode1 : outputNodes)
    			{
    				onode1.calculateDelta(instance.classValues.get(idx));
    				idx++;
    			}
    			for(Node hnode1 : hiddenNodes)
    			{
    				hnode1.calculateDelta(0.0);
    			}   
    			for(Node onode2 : outputNodes)
    			{
    				onode2.updateWeight(learningRate);
    			}
    			for(Node hnode2 : hiddenNodes)
    			{
    				hnode2.updateWeight(learningRate);
    			}
    			for(int m=0; m<hiddenNodeCount; m++)
    			{
    				hiddenNodes.get(m).child.clear();
    			}
    			for(int j=0;j<this.outputNodeCount;j++)
    			{
    				ArrayList<NodeWeightPair> parents = outputNodes.get(j).parents;
    				for(int k=0;k<parents.size()-1;k++)
    				{
    					NodeWeightPair nwp = new NodeWeightPair(outputNodes.get(j), parents.get(k).weight);
    					parents.get(k).node.child.add(nwp);
    				}
    			}
    		}
    		
    		double loss=0;
    		for(Instance itrinstance : trainingSet)
    		{
    			loss+=loss(itrinstance);    			    		
    		}
    		loss /= trainingSet.size();
    		System.out.println("Epoch: "+epochcount+", Loss: "+String.format("%.3e", loss));
    	}
    }

    /**
     * Calculate the cross entropy loss from the neural network for
     * a single instance.
     * The parameter is a single instance
     */
    private double loss(Instance instance) {
		double lossvalue = 0;
    	double label = 0;
    	predict(instance);

		for(int i=0; i<outputNodeCount; i++)
		{	
			label = instance.classValues.get(i);
			lossvalue -= label*Math.log(outputNodes.get(i).outputactvalue);
		}		
        return lossvalue;
    }
}
