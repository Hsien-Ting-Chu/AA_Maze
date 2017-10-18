package mazeSolver;

import maze.Cell;
import maze.Maze;

/**
 * Implements WallFollowerSolver
 */

public class WallFollowerSolver implements MazeSolver {
	
	Maze maze;
	boolean isSolved = false;
	int count = 0;
	boolean[][] cVisited;
	int turn = 1;
	
	@Override
	public void solveMaze(Maze maze) {
		
		this.maze = maze;
		cVisited = new boolean[maze.sizeR][maze.sizeC];
		for(int i=0; i<maze.sizeR; i++) {
			for(int j=0 ;j<maze.sizeC; j++) {
				cVisited[i][j] = false;
			}
		}
		
		if(maze.type == Maze.HEX) {
			turn = Maze.HEX;
		}
		
		Cell currCell = maze.entrance;
		setVisited(currCell);
		int dir = 0;
		if( currCell.wall[dir] == null) {
			dir++;
		}
		while( currCell != maze.exit ) {
			if( currCell.tunnelTo != null ) {
				currCell = currCell.tunnelTo;
				setVisited(currCell);
			}
			if( !currCell.wall[dir].present ){
				currCell = currCell.neigh[dir];
				setVisited(currCell);
				dir = dir - turn;
				if(dir < 0) dir = dir + Maze.NUM_DIR;
				while ( currCell.wall[dir] == null ) {
					dir = dir - 1;
					if(dir < 0) dir = dir + Maze.NUM_DIR;
				}
			} else {
				do {
					dir = (dir + 1)%Maze.NUM_DIR;
				} while ( currCell.wall[dir] == null );
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
		return count;
	} // end of cellsExplored()
	
	
	public void setVisited(Cell cell) {
		int r = cell.r;
		int c = cell.c;
		if(maze.type == Maze.HEX) {
			c = c - (r + 1) / 2;
		}
		if( !cVisited[r][c] ) {
			cVisited[r][c] = true;
			count++;
		}
		maze.drawFtPrt(cell);
	}

} // end of class WallFollowerSolver
