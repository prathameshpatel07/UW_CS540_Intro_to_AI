import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	//private ArrayList<State> getSuccessors(boolean[][] explored, Maze maze) {
		// TODO Auto-generated method stub
	//	return null;
	//}
	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD

		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// ...

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();
		ArrayList<State> curr_array;
		State start = new State(maze.getPlayerSquare(), null, 0, 0);
		int node_expanded = 0;
		int frontier_max = 0;
		int depth = 0;
		//temp = parent.getSuccessors(explored, maze);
		//System.out.println("Maze X: " + maze.getPlayerSquare().X + "Y:" + maze.getPlayerSquare().Y);
		queue.add(start);
		while (!queue.isEmpty()) {
			// TODO return true if find a solution
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found
			
			//Check Frontier Size
			frontier_max = Math.max(queue.size(), frontier_max);
			node_expanded++;
			
			State head;
			head = queue.removeFirst();
			//System.out.println("Main Loop: " + start + " Head:" + head);
			int x,y;
			x = head.getSquare().X;
			y = head.getSquare().Y;
			//System.out.println("head x" + x + " y:" + y);

			if(maze.getSquareValue(x, y) == 'G') {
				depth = head.getDepth();
				//System.out.println("Goal Node found");
				//System.out.println("depth" + head.getDepth());
				//System.out.println("Frontier Max: " + frontier_max);
				//System.out.println("Node Expabded: " + node_expanded);
				
				cost = depth;
				noOfNodesExpanded = node_expanded;
				maxDepthSearched = depth;
				maxSizeOfFrontier = frontier_max;
				
				State tracer = head.getParent();
				while(true) {
					if((maze.getPlayerSquare().X == tracer.getSquare().X && maze.getPlayerSquare().Y == tracer.getSquare().Y))
						break;
					
					maze.setOneSquare(tracer.getSquare(), '.');
					tracer = tracer.getParent();
				}
				return true;
			}
			else {
				Square headsq = head.getSquare();
//				maze.setOneSquare(headsq, '.');
				explored[x][y] = true; 
				curr_array = head.getSuccessors(explored, maze, head);
				while(!curr_array.isEmpty())
				{	int i=0;
					int j=0;
					boolean flag = false;
					while(j < queue.size()) {
						if((queue.get(j).getSquare().X == curr_array.get(i).getSquare().X && queue.get(j).getSquare().Y == curr_array.get(i).getSquare().Y)) 
							flag = true;
						j++;
					}
					if(!flag) queue.addLast(curr_array.remove(i));
					else curr_array.remove(i);
					i++;
				}
			}
		//	System.out.println("Program complete");
			// use queue.pop() to pop the queue.
			// use queue.add(...) to add elements to queue
		}

		// TODO return false if no solution
		return false;
	}
}