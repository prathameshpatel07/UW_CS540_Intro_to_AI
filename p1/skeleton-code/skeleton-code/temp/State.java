import java.util.ArrayList;

/**
 * A state in the search represented by the (x,y) coordinates of the square and
 * the parent. In other words a (square,parent) pair where square is a Square,
 * parent is a State.
 * 
 * You should fill the getSuccessors(...) method of this class.
 * 
 */
public class State {

	private Square square;
	private State parent;

	// Maintain the gValue (the distance from start)
	// You may not need it for the BFS but you will
	// definitely need it for AStar
	private int gValue;

	// States are nodes in the search tree, therefore each has a depth.
	private int depth;

	/**
	 * @param square
	 *            current square
	 * @param parent
	 *            parent state
	 * @param gValue
	 *            total distance from start
	 */
	public State(Square square, State parent, int gValue, int depth) {
		this.square = square;
		this.parent = parent;
		this.gValue = gValue;
		this.depth = depth;
	}

	/**
	 * @param visited
	 *            explored[i][j] is true if (i,j) is already explored
	 * @param maze
	 *            initial maze to get find the neighbors
	 * @return all the successors of the current state
	 */
	public ArrayList<State> getSuccessors(boolean[][] explored, Maze maze, State curr_state) {
		// FILL THIS METHOD

		// TODO check all four neighbors in left, down, right, up order
		// TODO remember that each successor's depth and gValue are
		// +1 of this object.
		ArrayList<State> state = new ArrayList<State>();

		Square Curr_sq_left = new Square(0,0);
		Square Curr_sq_bottom = new Square(0,0);
		Square Curr_sq_right = new Square(0,0);
		Square Curr_sq_top = new Square(0,0);
		int x,y,gvalue, depth;;
		
		//Curr_sq = curr_state.square;
		x = curr_state.square.X;
		y = curr_state.square.Y;
		gvalue = curr_state.gValue;
		depth = curr_state.depth;

		State left = new State(Curr_sq_left, curr_state, gvalue, depth);
		State bottom = new State(Curr_sq_bottom, curr_state, gvalue, depth);
		State right = new State(Curr_sq_right, curr_state, gvalue, depth);
		State top = new State(Curr_sq_top, curr_state, gvalue, depth);
		
		//System.out.println("Entered State Loop: X=" + x + " Y=" + y);
		//System.out.println("Entered State Loop:" + x + " " + y);
		//Left Node
		if(y-1 >= 0 && !explored[x][y-1] && maze.getSquareValue(x, y-1) != '%') {
			left.square.X = x;
			left.square.Y = y-1;
			left.gValue = gvalue+1;
			left.depth = depth+1;
			left.parent = curr_state;
			state.add(left);
			//System.out.println("Left State: X=" + left.getSquare().X + " Y=" + left.getSquare().Y);
		}
		
		//Bottom Node
		if(x+1 < maze.getNoOfRows() && !explored[x+1][y] && maze.getSquareValue(x+1, y) != '%') {
			bottom.square.X = x+1;
			bottom.square.Y = y;
			bottom.gValue = gvalue+1;
			bottom.depth = depth+1;
			bottom.parent = curr_state;
			state.add(bottom);
			//System.out.println("Bottom State: X=" + bottom.getSquare().X + " Y=" + bottom.getSquare().Y);
		}
		
		//Right node
		if(y+1 < maze.getNoOfCols() && !explored[x][y+1] && maze.getSquareValue(x, y+1) != '%') {
			right.square.X = x;
			right.square.Y = y+1;
			right.gValue = gvalue+1;
			right.depth = depth+1;
			right.parent = curr_state;
			state.add(right);
			//System.out.println("Right State: X=" + right.getSquare().X + " Y=" + right.getSquare().Y);
		}
		
		//Top node
		if(x-1 >= 0 && !explored[x-1][y] && maze.getSquareValue(x-1, y) != '%') {
			top.square.X = x-1;
			top.square.Y = y;
			top.gValue = gvalue+1;
			top.depth = depth+1;
			top.parent = curr_state;
			state.add(top);
			//System.out.println("Top State: X=" + top.getSquare().X + " Y=" + top.getSquare().Y);
		}
		//System.out.println("LeftState= "+left + " bottom="+bottom);
		//System.out.println("Left State: X=" + left.getSquare().X + " Y=" + left.getSquare().Y);
		//System.out.println("Total State Element" + state.size());
		return state;
	}

	/**
	 * @return x coordinate of the current state
	 */
	public int getX() {
		return square.X;
	}

	/**
	 * @return y coordinate of the current state
	 */
	public int getY() {
		return square.Y;
	}

	/**
	 * @param maze initial maze
	 * @return true is the current state is a goal state
	 */
	public boolean isGoal(Maze maze) {
		if (square.X == maze.getGoalSquare().X
				&& square.Y == maze.getGoalSquare().Y)
			return true;

		return false;
	}

	/**
	 * @return the current state's square representation
	 */
	public Square getSquare() {
		return square;
	}

	/**
	 * @return parent of the current state
	 */
	public State getParent() {
		return parent;
	}

	/**
	 * You may not need g() value in the BFS but you will need it in A-star
	 * search.
	 * 
	 * @return g() value of the current state
	 */
	public int getGValue() {
		return gValue;
	}

	/**
	 * @return depth of the state (node)
	 */
	public int getDepth() {
		return depth;
	}
}
