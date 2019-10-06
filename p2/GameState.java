import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private int size;            // The number of stones
    public boolean[] stones;    // Game state: true for available stones, false for taken ones
    private int lastMove;        // The last move
    public int MinMove;
    public int MaxMove;
    public double minValue;
    public double maxValue;

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size) {

        this.size = size;

        //  For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[this.size + 1];
        this.stones[0] = false;

        // Set default state of stones to available
        for (int i = 1; i <= this.size; ++i) {
            this.stones[i] = true;
        }

        // Set the last move be -1
        this.lastMove = -1;
    }

    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;
    }


    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
        // TODO Add your code here
    	List<Integer> list = new ArrayList<Integer>();
    	int parentnum = this.lastMove;
    	if (parentnum == 0) {
    		int k = 1;
    		double kd;
    		for (k = 1; k <= (this.getSize()/2); k++) {
    			//double rootchild;
    			kd = Double.valueOf(k);
    			if (k % 2 != 0) { 
    				if (k == 1 || !(Math.ceil(this.getSize()/2) - kd == 0))
    				list.add(k);
    			}
    		}
    	}
    	else {
    	for (int i = 1; i < parentnum; i++) {
    		if(parentnum % i == 0 && this.getStone(i)) list.add(i);
    	}
    	for (int j = 1; j <= this.getSize(); j++) {
    		if(parentnum*j <= this.getSize() && this.getStone(parentnum*j)) 
    			list.add(parentnum*j);
    	}
    	}
    		//System.out.println("Number added: " + list.contains(i));
    	
        return list;
    }


    /**
     * This method is used to generate a list of successors
     * using the getMoves() method
     *
     * @return This is the list of state's successors
     */
    public List<GameState> getSuccessors() {
        return this.getMoves().stream().map(move -> {
            var state = new GameState(this);
            state.removeStone(move);
            return state;
        }).collect(Collectors.toList());
    }


    /**
     * This method is used to evaluate a game state based on
     * the given heuristic function
     *
     * @return int This is the static score of given state
     */
    public double evaluate() {
        // TODO Add your code here
    	int lastmove = 0;
    	double lastsbe = 0;
    	GameState thiscopy = new GameState(this);
    	lastmove = thiscopy.getLastMove();
    	
    	if(thiscopy.getStone(1))
    		lastsbe = 0.0;
    	else if(lastmove == 1)
    		lastsbe =  (thiscopy.getMoves().size() % 2 == 0) ? -0.5 : 0.5;
    	else if(Helper.isPrime(lastmove))
    		lastsbe = (thiscopy.getMoves().size() % 2 == 0) ? -0.7 : 0.7;
    	else {
    		int largestprime = 1;
    		largestprime = Helper.getLargestPrimeFactor(lastmove);
    		thiscopy.setLastMove(largestprime);
    		lastsbe = (thiscopy.getMoves().size() % 2 == 0) ? -0.6 : 0.6;
    	}
        return lastsbe;
    }

    /**
     * This method is used to take a stone out
     *
     * @param idx Index of the taken stone
     */
    public void removeStone(int idx) {
        this.stones[idx] = false;
        this.lastMove = idx;
    }

    /**
     * These are get/set methods for a stone
     *
     * @param idx Index of the taken stone
     */
    public void setStone(int idx) {
        this.stones[idx] = true;
    }

    public boolean getStone(int idx) {
        return this.stones[idx];
    }

    /**
     * These are get/set methods for lastMove variable
     *
     * @param move Index of the taken stone
     */
    public void setLastMove(int move) {
        this.lastMove = move;
    }

    public int getLastMove() {
        return this.lastMove;
    }

    /**
     * This is get method for game size
     *
     * @return int the number of stones
     */
    public int getSize() {
        return this.size;
    }

}	
