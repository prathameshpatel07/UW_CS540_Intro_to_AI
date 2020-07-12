import java.util.ArrayList;
import java.util.List;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0) this.numAttr = trainDataSet.get(0).size() - 1;
		this.root = buildTree(trainDataSet, mDepth);
	}
	
	private DecTreeNode buildTree(List<List<Integer>> trainDataSet, int mDepth) {
		// TODO: add code here
		double bestthres = Double.MAX_VALUE;
		double bestthresall = Double.MAX_VALUE;
		int[] bestthresinattr = new int[9];
		int bestattr = -1;
		int bestattrsplit = -1;

		//Leaf Condition
		if(mDepth == 0 || trainDataSet.size() <= maxPerLeaf) {
			int resultlabel = calculateresult(trainDataSet);
			return new DecTreeNode(resultlabel, -1, -1);
		}
		//Non-lead condition
		
		//Found the best attr with split and best entropy
		for(int i = 0; i <= 8; i++) {
			bestthres = Double.MAX_VALUE;
			for(int j = 1; j <= 9; j++) {
				double condprob_result = 0.0;
				condprob_result = calculate_condprob(trainDataSet, i, j);
				if(bestthres > condprob_result) {
				bestthres = condprob_result;
				bestthresinattr[i] = j;
				}
			}
			if(bestthresall > bestthres) {
				bestthresall = bestthres;
				bestattr = i;
				bestattrsplit = bestthresinattr[i];
			}
		}
		
		if((calculateparententropy(trainDataSet) - bestthresall) == 0) {
			int resultlabel = calculateresult(trainDataSet);
			return new DecTreeNode(resultlabel, -1, -1);
		}
		
		//Else Cut Dataset
		DecTreeNode nonleafnode = new DecTreeNode(-1, bestattr, bestattrsplit);
		
		List<List<Integer>> leftlist = new ArrayList<List<Integer>>();
		List<List<Integer>> rightlist = new ArrayList<List<Integer>>();
		for(List<Integer> list : trainDataSet) {
			if(list.get(bestattr) <= bestattrsplit)
				leftlist.add(list);
			else
				rightlist.add(list);
		}
		nonleafnode.left = buildTree(leftlist, mDepth-1);
		nonleafnode.right = buildTree(rightlist, mDepth-1);
		
		return nonleafnode;
	}
	
	public int calculateresult(List<List<Integer>> trainDataSet) {
		int n0 = 0;
		int n1 = 0;
		for(List<Integer> l : trainDataSet) {
			if(l.get(l.size()-1) == 0)
				n0++;
			else
				n1++;
		}
		if(n0 == n1)
			return 1;
		else if (n0 > n1)
			return 0;
		else
			return 1;
	}
	
	public double calculateparententropy(List<List<Integer>> trainDataSet) {
		int n0 = 0;
		int n1 = 0;
		for(List<Integer> l : trainDataSet) {
			if(l.get(l.size()-1) == 0)
				n0++;
			else
				n1++;
		}
		return hfunc((1.0*n0)/trainDataSet.size(), (1.0*n1)/trainDataSet.size());
	}
	
	public double log2(double d) {
		if(d == 0)
			return 0.0;
		return Math.log(d)/Math.log(2.0);
	}
	
	public double hfunc(double a, double b) {
		return (-(a*log2(a) + b*log2(b)));
	}
	
	public double calculate_condprob(List<List<Integer>> trainDataSet, int attr, int threshold) {
		int total = trainDataSet.size();
		int lessi = 0;
		int greati = 0;
		int lessi0 = 0;
		int lessi1 = 0;
		int greati0 = 0;
		int greati1 = 0;
		
		double hless = 0.0;
		double hgreat = 0.0;
		
		for(List<Integer> l : trainDataSet) {
			if(l.get(attr) <= threshold) {
				lessi++;
				if(l.get(l.size()-1) == 0)
					lessi0++;
				else
					lessi1++;
			}
			else {
				greati++;
				if(l.get(l.size()-1) == 0)
					greati0++;
				else
					greati1++;
			}
		}
		
		hless = (1.0*lessi)/total * hfunc((1.0*lessi0)/lessi, (1.0*lessi1)/lessi);
		hgreat = (1.0*greati)/total * hfunc((1.0*greati0)/greati, (1.0*greati1)/greati);
		return (hless + hgreat);
	}
	
	public int classify(List<Integer> instance) {
		// TODO: add code here
		// Note that the last element of the array is the label.
		DecTreeNode rnode = root;
		
		while(!rnode.isLeaf()) {
			if(instance.get(rnode.attribute) <= rnode.threshold)
				rnode = rnode.left;
			else
				rnode = rnode.right;
		}
		return rnode.classLabel;
	}
	
	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if(node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if(node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}
	
	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i ++)
		{
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual*100.0 / (double)numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}
