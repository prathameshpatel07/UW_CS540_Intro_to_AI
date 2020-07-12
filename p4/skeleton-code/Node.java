import java.util.*;

/**
 * Class for internal organization of a Neural Network.
 * There are 5 types of nodes. Check the type attribute of the node for details.
 * Feel free to modify the provided function signatures to fit your own implementation
 */

public class Node {
    private int type = 0; //0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
    public ArrayList<NodeWeightPair> parents = null; //Array List that will contain the parents (including the bias node) with weights if applicable
    public ArrayList<NodeWeightPair> child = null;
    private double inputValue = 0.0;
    private double outputValue = 0.0;
//    private double outputGradient = 0.0;
    public double delta = 0.0; //input gradient
    public double outputactvalue = 1.0;

    //Create a node with a specific type
    Node(int type) {
        if (type > 4 || type < 0) {
            System.out.println("Incorrect value for node type");
            System.exit(1);

        } else {
            this.type = type;
        }
        
        if (type == 2) {
        	parents = new ArrayList<>();
        	child = new ArrayList<>();
        }
        
        if (type == 4) {
        	parents = new ArrayList<>();
        }
    }

    //For an input node sets the input value which will be the value of a particular attribute
    public void setInput(double inputValue) {
        if (type == 0) {    //If input node
            this.outputactvalue = inputValue;
            this.inputValue = inputValue;     
        }
    }

    /**
     * Calculate the output of a node.
     * You can get this value by using getOutput()
     */
    public void calculateOutput() {
        if (type == 2 || type == 4) {   //Not an input or bias node
            // TODO: add code here
        	double sum = 0;
        	for(int i=0;i<parents.size();i++)
        	{	
        		double parentout = parents.get(i).node.getOutput();
        		double parentweight = parents.get(i).weight;
        		sum += (type == 4)? parentweight * Math.max(0, parentout) : parentout * parentweight;
        	}
        	outputValue = sum;
        	outputactvalue = (type == 4) ? sum : Math.max(0, sum);
        }
    }
  
    //Gets the output value
    public double getOutput() {
        if (type == 0) {    //Input node
            return inputValue;
        } else if (type == 1 || type == 3) {    //Bias node
            return 1.00;
        } else {
            return outputValue;
        }
    }

    public void softmaxfunc(double softmaxsum) {
    	outputactvalue = Math.exp(outputactvalue)/softmaxsum;
    }
  
    //Calculate the delta value
    public void calculateDelta(double tk) {
    	delta = 0;
    	if(type == 4) {
    		double ok = 0.0;
    		ok = outputactvalue;
    		delta = tk - ok; 
    	}
    	else if(type == 2) {
        	double product = 0.0;
        	double preReluValue = 0.0;
        	for(NodeWeightPair itr : parents) {
        		product = itr.weight * itr.node.getOutput();
        		preReluValue += product;
        	}
        	//g' checked here
        	if(preReluValue > 0) {
        		for(NodeWeightPair succoutput : child) {
        			delta += succoutput.weight*succoutput.node.delta;
        		}
        	}
    	}
    }

    //Update the weights between parents node and current node
    public void updateWeight(double learningRate) {
        if (type == 2 || type == 4) {
        	for(NodeWeightPair currnode : parents) {
        		currnode.weight += learningRate * currnode.node.outputactvalue * delta;
        	}
        }
    }
}


