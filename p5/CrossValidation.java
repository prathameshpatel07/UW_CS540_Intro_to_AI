import java.util.ArrayList;
import java.util.List;

public class CrossValidation {
    /*
     * Returns the k-fold cross validation score of classifier clf on training data.
     */
    public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
        // TODO : Implement
    	//List<Instance> test;
    	double sumaccscore = 0;
    	//double[] s = new double[5];
    	int totalright = 0;
    	for (int t = 0; t < k; t++) {
    		List<Instance> trainD = new ArrayList<Instance>();
    		List<Instance> testD = new ArrayList<Instance>();
    		int setnum = trainData.size()/k;
    		for(int i = 0; i < trainData.size(); i++) {
    			if(i>=setnum*t && i<setnum*(t+1)) //Change here for splitting style
    				testD.add(trainData.get(i));
    			else
    				trainD.add(trainData.get(i));
    		}
    		Classifier cls = new NaiveBayesClassifier();
    		cls.train(trainD, v);
    		int rightpred = 0;
    		for(Instance inst: testD) {
    			ClassifyResult clsres = cls.classify(inst.words);
    			if(clsres.label.equals(inst.label)) {
    				rightpred++;
    				totalright++;
    			}
    		}
    		double accscore = ((double) rightpred)/(double)testD.size();
    		sumaccscore += accscore;
    	}
    	double totalaccscore = sumaccscore/k;
        return totalaccscore;
    }
}
