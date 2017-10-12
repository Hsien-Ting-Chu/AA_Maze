package mazeSolver;

import maze.Cell;
import maze.Maze;

/**
 * Implements WallFollowerSolver
 */

public class WallFollowerSolver implements MazeSolver {
	
	private boolean isSolved = false;
	
	@Override
	public void solveMaze(Maze maze) {
		
		Cell currCell = maze.entrance;
		int dir = 0;
		while( currCell != maze.exit ) {
			if( currCell.tunnelTo != null ) {
				currCell = currCell.tunnelTo;
			}
			if( !currCell.wall[dir].present ){
//				currCell = maze.map[ currCell.r + Maze.deltaR[dir] ][ currCell.c + Maze.deltaC[dir] ];
				currCell = currCell.neigh[dir];
				dir--;
			} else {
				dir++;
			}
		}
		isSolved = true;
        
	} // end of solveMaze()
    
    
	@Override
	public boolean isSolved() {
		return isSolved;
	} // end if isSolved()
    
    
	@Override
	public int cellsExplored() {
		// TODO Auto-generated method stub
		return 0;
	} // end of cellsExplored()

} // end of class WallFollowerSolver
