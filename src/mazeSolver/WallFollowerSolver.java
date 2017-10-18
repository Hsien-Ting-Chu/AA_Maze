package mazeSolver;

import maze.Cell;
import maze.Maze;

/**
 * Compute brute force - wall follower traversal on a graph.
 * 
 *  @author Ying-Chieh Huang s3598781
 */

public class WallFollowerSolver implements MazeSolver {
	
	/** The maze. */
	Maze maze;
	
	/** Is the maze solved. */
	boolean isSolved = false;
	
	/** The number of explored cells. */
	int cellsCount = 0;
	
	/** To record the cells were visited. */
	boolean[][] cVisited;
	
	/** Turn the direction back to the specific side after each movement
	 *  in rectangular maze	turn = 1;
	 *  in hexagon maze	turn = 2;
	 * */
	int turn = 1;
	
	
	/** 
     * Brute force - wall follower traversal of input graph maze from entrance to exit.
     * 
     * ******************************************************************************************
     * 
     * ALGORITHM Wall Follower ( Maze )
     * Perform a wall follower traversal of a graph.
     * Input: Maze maze.
     * OUTPUT : Maze maze with its cells that marked after they were visited,
     *          and displays on a graph user interface.
     * 
     * 1: cVisited = [][]
     * // mark all cells unvisited
     * for i = 0 to sizeR do
     *   for j = 0 to sizeC do
     *     cVisited[ i ][ j ] = false
     * end for
     * // initiate cell from the entrance of maze 
     * 2: begin from the entrance cell
     * while currCell != exit
     *    check the direction of east = 0
     *    move to the neighbour's cell if there's no wall in between
     *    if there's a wall, then turn left = 1
     * 
     * ******************************************************************************************
     * 
     * @param maze Input maze.
     */
	@Override
	public void solveMaze(Maze maze) {
		
		this.maze = maze;
		// initialise the value of 2D-array with false
		cVisited = new boolean[maze.sizeR][maze.sizeC];
		for(int i=0; i<maze.sizeR; i++) {
			for(int j=0 ;j<maze.sizeC; j++) {
				cVisited[i][j] = false;
			}
		}
		// if the maze type is Hexgon, then assign value: 2 to turn
		if(maze.type == Maze.HEX) turn = 2;
		// started from entrance
		Cell currCell = maze.entrance;
		setVisited(currCell);
		// initial direction from east = 0
		int dir = 0;
		// to avoid the null value that rectangular cell will contain
		while( currCell.wall[dir] == null) {
			dir++;
		}
		
		
		while( currCell != maze.exit ) {
			// if it's approached to a tunnel, move the current cell to the other side of tunnel
			if( currCell.tunnelTo != null ) {
				currCell = currCell.tunnelTo;
				setVisited(currCell);
			}
			// if there's no wall, move to the neighbour's cell
			if( !currCell.wall[dir].present ){
				currCell = currCell.neigh[dir];
				setVisited(currCell);
				// Turn the direction back to the specific side after each movement
				dir = dir - turn;
				if(dir < 0) dir = dir + Maze.NUM_DIR;
				// to avoid the null value that rectangular cell will contain
				while ( currCell.wall[dir] == null ) {
					dir = dir - 1;
					if(dir < 0) dir = dir + Maze.NUM_DIR;
				}
			// turn left if there's wall in between
			} else {
				do {
					dir = (dir + 1)%Maze.NUM_DIR;
				// to avoid the null value that rectangular cell will contain
				} while ( currCell.wall[dir] == null );
			}
		}
		// when cell move to the exit of maze
		isSolved = true;
        
	} // end of solveMaze()
    
    
	/** 
     * Return the value isSolved of the maze.
     * 
     */
	@Override
	public boolean isSolved() {
		return isSolved;
	} // end if isSolved()
    
    
	/** 
     * Return the value how many cells were explored.
     * 
     */
	@Override
	public int cellsExplored() {
		return cellsCount;
	} // end of cellsExplored()
	
	
	/** 
     * Mark the cell was visited and add up the number of cellsCount
     * call the drawFtPrt() to display on the GUI.
     * 
     * @param cell Input a cell.
     */
	public void setVisited(Cell cell) {
		int r = cell.r;
		int c = cell.c;
		if(maze.type == Maze.HEX) {
			c = c - (r + 1) / 2;
		}
		if( !cVisited[r][c] ) {
			cVisited[r][c] = true;
			cellsCount++;
		}
		maze.drawFtPrt(cell);
	} // end of setVisited()

} // end of class WallFollowerSolver
