import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifier implements Classifier {

	public int totalv = 0;
	public Map<String, Integer> dict_pos = new HashMap<String, Integer>();
	public Map<String, Integer> dict_neg = new HashMap<String, Integer>();
	Map<Label, Integer> doc_count = new HashMap<Label, Integer>();
	Map<Label, Integer> word_count = new HashMap<Label, Integer>();
	
    /**
     * Trains the classifier with the provided training data and vocabulary size
     */
    @Override
    public void train(List<Instance> trainData, int v) {
        // TODO : Implement
        // Hint: First, calculate the documents and words counts per label and store them. 
        // Then, for all the words in the documents of each label, count the number of occurrences of each word.
        // Save these information as you will need them to calculate the log probabilities later.
        //
        // e.g.
        // Assume m_map is the map that stores the occurrences per word for positive documents
        // m_map.get("catch") should return the number of "catch" es, in the documents labeled positive
        // m_map.get("asdasd") would return null, when the word has not appeared before.
        // Use m_map.put(word,1) to put the first count in.
        // Use m_map.replace(word, count+1) to update the value
    	totalv = v;
    	this.getDocumentsCountPerLabel(trainData);
    	this.getWordsCountPerLabel(trainData);
    	
    	for(Instance i: trainData) {
    		for(String w: i.words) {
    			if(i.label.equals(Label.POSITIVE))
    				if(dict_pos.containsKey(w)) {
    					dict_pos.replace(w, dict_pos.get(w)+1);
    				}
    				else
    					dict_pos.put(w, 1);
    			else {
    				if(dict_neg.containsKey(w)) {
    					dict_neg.replace(w, dict_neg.get(w)+1);
    				}
    				else
    					dict_neg.put(w, 1);
    			}
    		}
    	}
    	//System.out.println("Dict Size" + dict.size());//For loop ends
    }

    /*
     * Counts the number of words for each label
     */
    @Override
    public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
    	int count_word_pos = 0;
    	int count_word_neg = 0;   	
  
    	for(Instance i: trainData) {	
			if(i.label.equals(Label.POSITIVE)) 
    			count_word_pos += i.words.size();
			else
				count_word_neg += i.words.size();
    	}
    	word_count.put(Label.POSITIVE, count_word_pos);
    	word_count.put(Label.NEGATIVE, count_word_neg);
        return word_count;
    }



    /*
     * Counts the total number of documents for each label
     */
    @Override
    public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
    	int count_doc_pos = 0;
    	int count_doc_neg = 0;   	
  
    	for(Instance i: trainData) {	
			if(i.label.equals(Label.POSITIVE)) 
    			count_doc_pos++;
			else
				count_doc_neg++;
    	}
    	doc_count.put(Label.POSITIVE, count_doc_pos);
    	doc_count.put(Label.NEGATIVE, count_doc_neg);
        return doc_count;
    }


    /**
     * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or P(NEGATIVE)
     */
/*    private double p_l(Label label) {
        // TODO : Implement
        // Calculate the probability for the label. No smoothing here.
        // Just the number of label counts divided by the number of documents.
        return null;
    }
*/
    /**
     * Returns the smoothed conditional probability of the word given the label, i.e. P(word|POSITIVE) or
     * P(word|NEGATIVE)
     */
    private double p_w_given_l(String word, Label label) {
        // TODO : Implement
        // Calculate the probability with Laplace smoothing for word in class(label)
    	double pwl = 0;
    	//double num = dict.getOrDefault(word, 0) + 1;
    	double num = (label.equals(Label.POSITIVE))? 
    			dict_pos.getOrDefault(word, 0) + 1 : dict_neg.getOrDefault(word, 0) + 1;
    	double denom = totalv + word_count.get(label);
    	pwl = num/denom;
        return pwl;
    }

    /**
     * Classifies an array of words as either POSITIVE or NEGATIVE.
     */
    @Override
    public ClassifyResult classify(List<String> words) {
        // TODO : Implement
        // Sum up the log probabilities for each word in the input data, and the probability of the label
        // Set the label to the class with larger log probability
    	ClassifyResult classifyres = new ClassifyResult();
    	classifyres.logProbPerLabel = new HashMap<Label, Double>();
    	double totalprob_pos = 0;
    	double totalprob_neg = 0;
    	double prob_pos = 0;
    	double prob_neg = 0;
    	for(String w: words) {
    		prob_pos = Math.log(p_w_given_l(w, Label.POSITIVE));
    		prob_neg = Math.log(p_w_given_l(w, Label.NEGATIVE));
    		totalprob_pos += prob_pos;
    		totalprob_neg += prob_neg;
    	}
    	
    	double p_pos = 0;
    	double p_neg = 0;
    	int countpos_int = doc_count.get(Label.POSITIVE);
    	int countneg_int = doc_count.get(Label.NEGATIVE);
    	int counttotal = countpos_int + countneg_int;
    	double posa = (1.0*countpos_int)/counttotal;
    	double nega = (1.0*countneg_int)/counttotal;
    	p_pos = Math.log(posa);
    	p_neg = Math.log(nega);
    	
    	double logprobpos = p_pos+totalprob_pos;
    	double logprobneg = p_neg+totalprob_neg;
    	classifyres.logProbPerLabel.put(Label.POSITIVE, logprobpos);
    	classifyres.logProbPerLabel.put(Label.NEGATIVE, logprobneg);
    	
    	if(logprobpos < logprobneg)
    		classifyres.label = Label.NEGATIVE;
    	else
    		classifyres.label = Label.POSITIVE;
    	
        return classifyres;
    }


}
