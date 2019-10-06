import java.util.List;

public class AlphaBetaPruning {
public int move = 0;
public double Value = 0.0;
public int visited = 0;
public int evaluated = 0;
public int maxdepth = 0;
public double avg_bfactor = 0.0;

    public AlphaBetaPruning() {
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
        // TODO Add your code here
    	System.out.println("Move: " + this.move);
    	System.out.println("Value: " + Value);
    	System.out.println("Number of Nodes Visited: " + this.visited);
    	System.out.println("Number of Nodes Evaluated: " + this.evaluated);
       	System.out.println("Max Depth Reached: " + this.maxdepth);
    	avg_bfactor = (this.visited - 1.0)/(this.visited-this.evaluated);
    	System.out.printf("Avg Effective Branching Factor: %.1f\n", avg_bfactor);
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth, int nTaken) {
        // TODO Add your code here
    	boolean maxPlayer;
    	double alpha = Double.NEGATIVE_INFINITY;
    	double beta = Double.POSITIVE_INFINITY;
    	
    	this.maxdepth = depth;
    	
    	if(nTaken % 2 == 0) maxPlayer = true;
    	else maxPlayer = false;
    	
    	if (nTaken == 0) {
    		state.setLastMove(0);
    		Value = alphabeta_max(state, depth, alpha, beta, maxPlayer);
    	} else {
    		Value = maxPlayer? alphabeta_max(state, depth, alpha, beta, maxPlayer):
    						   alphabeta_min(state, depth, alpha, beta, maxPlayer);
    	}
		if(maxPlayer) move = state.MaxMove; 
		else move = state.MinMove;
		this.maxdepth = depth - this.maxdepth;
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta_max(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
        // TODO Add your code here
    	this.visited++;
    	List<GameState> Successors;
    	double v, vin;
    	Successors = state.getSuccessors();
    	//Algo
    	v = Double.NEGATIVE_INFINITY;
    	vin = Double.NEGATIVE_INFINITY;
    	if(depth < this.maxdepth) this.maxdepth = depth;
    	if (Successors.isEmpty() || depth==0) {
    		double sbe = 0;
    		this.evaluated++;
    		if (Successors.isEmpty()) {
        		sbe = maxPlayer ? -1.0 : 1.0;
    		}
    		else if(depth == 0) {
    			sbe = maxPlayer? state.evaluate() : -1*state.evaluate();
    			if(Math.abs(sbe) == 0.0) { 
    				sbe = Math.abs(sbe);
    			}
    		}    		
    		return sbe;
    	}
    	else {
    		state.minValue = Double.POSITIVE_INFINITY;
    		state.maxValue = Double.NEGATIVE_INFINITY;
    		for(GameState itr:Successors) {
    			
    			vin = alphabeta_min(itr, depth-1, alpha, beta, !maxPlayer);
    			v = Math.max(v, vin);
    			if(v < state.minValue) {
    				state.minValue = v;
    				state.MinMove = itr.getLastMove();
    			}
    			if(v > state.maxValue) {
    				state.maxValue = v;
    				state.MaxMove = itr.getLastMove();
    			}
    			if (v >= beta) {
    				return v;
    			}
    			alpha = Math.max(alpha, v);
    		}
    	}
        return v;
    }
    
    public double alphabeta_min(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
    	this.visited++;
    	List<GameState> Successors;
    	double v, vin;
    	Successors = state.getSuccessors();
    	//Algo
    	v = Double.POSITIVE_INFINITY;
    	vin = Double.POSITIVE_INFINITY;
    	if(depth < this.maxdepth) this.maxdepth = depth;
    	if (Successors.isEmpty() || depth==0) {
    		double sbe = 0;
    		this.evaluated++;
    		if (Successors.isEmpty()) {
        		sbe = maxPlayer ? -1.0 : 1.0;
    		}
    		else if(depth == 0) {
    			sbe = maxPlayer? state.evaluate() : -1*state.evaluate();
    			if(Math.abs(sbe) == 0.0) { 
    			//System.out.println("Before Min Abs faced " + sbe);
    			sbe = Math.abs(sbe);
    			//System.out.println("After Min Abs faced " + sbe);
    			}
    			}
    		
    		return sbe;
    	}
    	else {
    		state.minValue = Double.POSITIVE_INFINITY;
    		state.maxValue = Double.NEGATIVE_INFINITY;
    		for(GameState itr:Successors) {
    			vin = alphabeta_max(itr, depth-1, alpha, beta, !maxPlayer);
    			v = Math.min(v, vin);
    			if(v < state.minValue) {
    				state.minValue = v;
    				state.MinMove = itr.getLastMove();
    			}
    			if(v > state.maxValue) {
    				state.maxValue = v;
    				state.MaxMove = itr.getLastMove();
    			}
    			if (v <= alpha) {
    				return v;
    			}
    			
    			 beta = Math.min(beta, v);
    		}
    	}
        return v;
    }
}
