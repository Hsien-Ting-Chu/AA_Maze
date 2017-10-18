package mazeSolver;

import java.util.*;
import maze.*;

/**
 * Implements the BiDirectional recursive backtracking maze solving algorithm.
 */
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {
	
	Maze maze;
	Cell hCell;
	Cell tCell;
	boolean isSolved = false;
	int count = 0;
	ArrayList<Cell> hPath = new ArrayList<>();
	ArrayList<Cell> tPath = new ArrayList<>();
	Random rand = new Random();
	boolean[][] hVisited;
	boolean[][] tVisited;
	boolean isHead = true;
	
	@Override
	public void solveMaze(Maze maze) {
		
		this.maze = maze;
		hCell = maze.entrance;
		tCell = maze.exit;
		hPath.add(hCell);
		tPath.add(tCell);
		
		hVisited = new boolean[maze.sizeR][maze.sizeC];
		tVisited = new boolean[maze.sizeR][maze.sizeC];
		for(int i=0; i<maze.sizeR; i++) {
			for(int j=0 ;j<maze.sizeC; j++) {
				hVisited[i][j] = false;
				tVisited[i][j] = false;
			}
		}
		int dir;
		Cell nCell;
		
		while( !isSolved ) {
			if( hPath.size() <= tPath.size() ) {
				isHead = true;
				dir = getNeib(hCell, isHead);
				if( dir != -1 ) {
					while( dir != -1 ) {
						nCell = hCell.neigh[dir];
						setVisited(nCell, isHead);
						hPath.add(nCell);
						if( nCell.tunnelTo != null  ) {
							nCell = nCell.tunnelTo;
							setVisited(nCell, isHead);
							hPath.add(nCell);
						}
						hCell = nCell;
						if( hCell == tCell || isVisited(hCell, isHead)) {
							isSolved = true;
							break;
						}
						if( hPath.size() > tPath.size() ) {
							break;
						}
						dir = getNeib(hCell, isHead);
					}
				} else {
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
				dir = getNeib(tCell, isHead);
				if( dir != -1 ) {
					while( dir != -1 ) {
						nCell = tCell.neigh[dir];
						setVisited(nCell, isHead);
						tPath.add(nCell);
						if( nCell.tunnelTo != null  ) {
							nCell = nCell.tunnelTo;
							setVisited(nCell, isHead);
							tPath.add(nCell);
						}
						tCell = nCell;
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
	
	
	@Override
	public boolean isSolved() {
		return isSolved;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		return count;
	} // end of cellsExplored()
	
	
	public int getNeib(Cell cell, boolean isHead) {
		
		ArrayList<Integer> neibDirs = new ArrayList<>();
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
		int dir = -1;
		if( neibDirs.size() > 0) {
			dir = neibDirs.get( rand.nextInt(neibDirs.size()) );
		}
		return dir;
	}
	
	
	public void setVisited(Cell cell, boolean isHead) {
		int r = cell.r;
		int c = cell.c;
		if(maze.type == Maze.HEX) {
			c = c - (r + 1) / 2;
		}
		if( isHead ) {
			if( !hVisited[r][c] ) {
				hVisited[r][c] = true;
				count++;
			}
		} else {
			if( !tVisited[r][c] ) {
				tVisited[r][c] = true;
				count++;
			}
		}
		maze.drawFtPrt(cell);
	}
	
	
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
