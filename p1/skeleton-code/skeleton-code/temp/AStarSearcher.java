import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();
		
		State start = new State(maze.getPlayerSquare(), null, 0, 0);
		ArrayList<State> curr_array;
		StateFValuePair fpairstart = new StateFValuePair(start, 0.0);
		int node_expanded = 0;
		int frontier_max = 0;
		int cost_value = 0, depth = 0;
		int GX =0, GY = 0;
		// TODO initialize the root state and add
		// to frontier list
		// ...
		GX = maze.getGoalSquare().X;
		GY = maze.getGoalSquare().Y;
		frontier.add(fpairstart);

		while (!frontier.isEmpty()) {
			// TODO return true if a solution has been found
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found
			
			//Check Frontier Size
			frontier_max = Math.max(frontier.size(), frontier_max);
			node_expanded++;
			
			StateFValuePair fhead;
			fhead = frontier.poll();
			
			//System.out.println("Main Loop: " + start + " Head:" + head);
			int x,y;
			x = fhead.getState().getSquare().X;
			y = fhead.getState().getSquare().Y;
			//System.out.println("head x" + x + " y:" + y);

			if(maze.getSquareValue(x, y) == 'G') {
				cost_value = fhead.getState().getDepth();
				//System.out.println("Goal Node found");
				//System.out.println("depth" + head.getDepth());
				//System.out.println("Frontier Max: " + frontier_max);
				//System.out.println("Node Expabded: " + node_expanded);
				
				cost = cost_value;
				noOfNodesExpanded = node_expanded;
				maxDepthSearched = depth;
				maxSizeOfFrontier = frontier_max;
				
				State tracer = fhead.getState().getParent();
				while(true) {
					if((maze.getPlayerSquare().X == tracer.getSquare().X && maze.getPlayerSquare().Y == tracer.getSquare().Y))
						break;
					
					maze.setOneSquare(tracer.getSquare(), '.');
					tracer = tracer.getParent();
				}
				return true;
			}
			else {	
					double h = 0;
					double f = 0;
					//StateFValuePair ftemp1;
					StateFValuePair ftemp2;
					explored[x][y] = true; 
					curr_array = fhead.getState().getSuccessors(explored, maze, fhead.getState());
					int xtemp = 0, ytemp = 0;
					while(!curr_array.isEmpty())
					{	int i=0;
						//int flag = 0;
						//int j=0;
					//depth Check
						depth = Math.max(depth, curr_array.get(i).getDepth());
						boolean flag = false;
						xtemp = curr_array.get(i).getSquare().X;
						ytemp = curr_array.get(i).getSquare().Y;
						h = Math.sqrt(Math.pow((xtemp-GX),2) + Math.pow((ytemp-GY),2));
						f = curr_array.get(i).getGValue() + h;
						
						Iterator<StateFValuePair> itr1 = frontier.iterator();
						//Iterator<StateFValuePair> itr2 = frontier.iterator();
						while(itr1.hasNext()) {
							StateFValuePair itrfpair = itr1.next();
							State itrstate = itrfpair.getState();
							double itrfvalue = itrfpair.getFValue();
							//System.out.printf("Iterator loop X=%d Y=%d",itrstate.getX(),itrstate.getY());
							if(itrstate.getSquare().X == xtemp && itrstate.getSquare().Y == ytemp) {
								//ftemp1 = new StateFValuePair(curr_array.get(i), f);
								//flag = itrfpair.compareTo(ftemp1);
								//if(flag == 1) itr1.remove();
								//System.out.println("Loop Error detected X:" + xtemp + " Y:" + ytemp);
								if(itrstate.getGValue() <= curr_array.get(i).getGValue())
									flag = true;
								else 
									itr1.remove();
									//System.out.println("Error detected: ============================");
							}	
						}
						/*if(!frontier.isEmpty()) {
						if(frontier.peek().getState().getX() == xtemp && frontier.peek().getState().getY()==ytemp) {
							//System.out.println("Frontier Error detected: ============================");
							//System.out.println("X: " + xtemp + " Y:" + ytemp);
							if(frontier.peek().getState().getGValue() <= curr_array.get(i).getGValue()) {
								flag = true;
								//System.out.println("Gvalue Exceeded");
							}
							else ;
								//System.out.println("Mega Error detected: ============================");
						}
						}*/
						if(!flag) {
							ftemp2 = new StateFValuePair(curr_array.remove(i), f);
							frontier.add(ftemp2);
						}
						else curr_array.remove(i);
						i++;
					}
				}
			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
		}

		// TODO return false if no solution
		return false;
	}
}
