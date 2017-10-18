package mazeSolver;

import java.util.*;
import maze.*;

/**
 * Compute depth first search traversal on a graph.
 * 
 *  @author Ying-Chieh Huang s3598781
 */
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {
	
	/** The maze. */
	Maze maze;
	
	/** Is the maze solved. */
	boolean isSolved = false;
	
	/** The number of explored cells. */
	int cellsCount = 0;
	
	/** The beginning cell in the head path. */
	Cell hCell;
	
	/** The beginning cell in the tail path. */
	Cell tCell;
	
	/** The list to store cells that head cell has been visited. */
	ArrayList<Cell> hPath = new ArrayList<>();
	
	/** The list to store cells that tail cell has been visited. */
	ArrayList<Cell> tPath = new ArrayList<>();
	
	/** To record the cells in the head path that were visited. */
	boolean[][] hVisited;
	
	/** To record the cells in the tail path that were visited. */
	boolean[][] tVisited;
	
	/** To generate a random number. */
	Random rand = new Random();
	
	/** For dedicating the value from which head or tail path */
	boolean isHead = true;
	
	
	/** 
     * Depth first search traversal of input graph maze from both entrance and exit to each other.
     * 
     * ******************************************************************************************
     * 
     * ALGORITHM DFS ( Maze )
     * Perform a depth first search traversal of a graph.
     * Input: Maze maze.
     * OUTPUT : Maze maze with its cells that marked after they were visited,
     *          and displays on a graph user interface.
     * 
     * 1: cVisited = [][]
     * // mark all cells in both 2D-arrays unvisited
     * for i = 0 to sizeR do
     *   for j = 0 to sizeC do
     *     hVisited[ i ][ j ] = false
     *     tVisited[ i ][ j ] = false
     * end for
     * // initiate cells from the entrance and the exit of maze 
     * 2: begin from the entrance cell / exit cell
     * while it's not solved
     * 3: Begin from the path which is shorter than another.
     * 4: Move to the available neighbour's cell if there's no wall in between.
     * 5: If it reached to a dead end, return to the previous cell which has other available
     *    neighbour's cell to visit.
     * 6: if one of the fronts meet each other or other's visited cells
     * end while
     * 
     * ******************************************************************************************
     * 
     * @param maze Input maze.
     */
	@Override
	public void solveMaze(Maze maze) {
		
		this.maze = maze;
		hCell = maze.entrance;
		tCell = maze.exit;
		hPath.add(hCell);
		tPath.add(tCell);
		
		// initialise the value of both 2D-arrays with false
		hVisited = new boolean[maze.sizeR][maze.sizeC];
		tVisited = new boolean[maze.sizeR][maze.sizeC];
		for(int i=0; i<maze.sizeR; i++) {
			for(int j=0 ;j<maze.sizeC; j++) {
				hVisited[i][j] = false;
				tVisited[i][j] = false;
			}
		}
		// to store the direction
		int dir;
		// to store the neighbour's cell
		Cell nCell;
		
		while( !isSolved ) {
			// in order to move the DFS fronts in a equal speed
			if( hPath.size() <= tPath.size() ) {
				isHead = true;
				// get the direction of available neighbour's cell
				dir = getNeib(hCell, isHead);
				// if it's -1 means it has to backtrack to the previous cell
				if( dir != -1 ) {
					while( dir != -1 ) {
						nCell = hCell.neigh[dir];
						setVisited(nCell, isHead);
						hPath.add(nCell);
						// if it's approached to a tunnel, move the current cell to the other side of tunnel
						if( nCell.tunnelTo != null  ) {
							nCell = nCell.tunnelTo;
							setVisited(nCell, isHead);
							hPath.add(nCell);
						}
						hCell = nCell;
						// check if the DFS fronts meet each other or not
						if( hCell == tCell || isVisited(hCell, isHead)) {
							isSolved = true;
							break;
						}
						if( hPath.size() > tPath.size() ) {
							break;
						}
						// keep go through all the possible available neighbour's cell
						dir = getNeib(hCell, isHead);
					}
				} else {
					// delete the stored cell in the path during backtrack
					hPath.remove(hPath.size()-1);
					hCell = hPath.get(hPath.size()-1);
					if( hCell.tunnelTo != null && getNeib(hCell, isHead) == -1 ) {
						hPath.remove(hPath.size()-1);
						hPath.remove(hPath.size()-1);
						hCell = hPath.get(hPath.size()-1);
					}
				}
			} else {
				isHead = false;
				// get the direction of available neighbour's cell
				dir = getNeib(tCell, isHead);
				// if it's -1 means it has to backtrack to the previous cell
				if( dir != -1 ) {
					while( dir != -1 ) {
						nCell = tCell.neigh[dir];
						setVisited(nCell, isHead);
						tPath.add(nCell);
						// if it's approached to a tunnel, move the current cell to the other side of tunnel
						if( nCell.tunnelTo != null  ) {
							nCell = nCell.tunnelTo;
							setVisited(nCell, isHead);
							tPath.add(nCell);
						}
						tCell = nCell;
						// check if the DFS fronts meet each other or not
						if( tCell == hCell || isVisited(tCell, isHead) ) {
							isSolved = true;
							break;
						}
						if( hPath.size() < tPath.size() ) {
							break;
						}
						dir = getNeib(tCell, isHead);
					}
				} else {
					// delete the stored cell in the path during backtrack
					tPath.remove(tPath.size()-1);
					tCell = tPath.get(tPath.size()-1);
					if( tCell.tunnelTo != null && getNeib(tCell, isHead) == -1 ) {
						tPath.remove(tPath.size()-1);
						tPath.remove(tPath.size()-1);
						tCell = tPath.get(tPath.size()-1);
					}
				}
			}
		}
		
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
     * Check if there's a available neighbour's cell to visit,
     * if there is, return the direction of a random available neighbour's cell
     * 
     * @param cell Input a cell.
     * @param cell Input a value dedicates that it's from the head path or not.
     * @return dir, the direction of that neighbour's cell
     */
	public int getNeib(Cell cell, boolean isHead) {
		
		// For storing available neighbour's cell
		ArrayList<Integer> neibDirs = new ArrayList<>();
		// To store the neighbour's cell
		Cell nCell;
		for(int i=0; i<Maze.NUM_DIR; i++) {
			if( cell.neigh[i] != null ) {
				nCell = cell.neigh[i];
				int r = nCell.r;
				int c = nCell.c;
				if(maze.type == Maze.HEX) {
					c = c - (r + 1) / 2;
				}
				if( isHead ) {
					if( !hVisited[r][c] && !cell.wall[i].present ) {
						neibDirs.add(i);
					}
				} else {
					if( !tVisited[r][c] && !cell.wall[i].present ) {
						neibDirs.add(i);
					}
				}
			}
		}
		// initailise the number -1, means no available neighbour's cell
		int dir = -1;
		if( neibDirs.size() > 0) {
			// randomly choose an available neighbour's cell
			dir = neibDirs.get( rand.nextInt(neibDirs.size()) );
		}
		return dir;
	}
	
	
	/** 
     * Mark the cell was visited and add up the number of cellsCount
     * call the drawFtPrt() to display on the GUI.
     * 
     * @param cell Input a cell.
     * @param isHead Input a value dedicates that it's from the head path or not.
     */
	public void setVisited(Cell cell, boolean isHead) {
		int r = cell.r;
		int c = cell.c;
		if(maze.type == Maze.HEX) {
			c = c - (r + 1) / 2;
		}
		// if it's from the head, mark the cell in head 2D-array
		if( isHead ) {
			if( !hVisited[r][c] ) {
				hVisited[r][c] = true;
				cellsCount++;
			}
		} else {
			if( !tVisited[r][c] ) {
				tVisited[r][c] = true;
				cellsCount++;
			}
		}
		maze.drawFtPrt(cell);
	}
	
	
	/** 
     * Check the cell is visited in the opposite 2D-array or not
     * 
     * @param cell Input a cell.
     * @param cell Input a value dedicates that it's from the head path or not.
     * @return true, if the cell has been visited
     */
	public boolean isVisited(Cell cell, boolean isHead) {
		int r = cell.r;
		int c = cell.c;
		if(maze.type == Maze.HEX) {
			c = c - (r + 1) / 2;
		}
		if( isHead ) {
			return tVisited[r][c];
		} else {
			return hVisited[r][c];
		}
	}

} // end of class BiDirectionalRecursiveBackTrackerSolver
