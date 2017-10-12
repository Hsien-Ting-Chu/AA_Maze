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
	ArrayList<Cell> hPath = new ArrayList<>();
	ArrayList<Cell> tPath = new ArrayList<>();
	Random rand = new Random();
	boolean[][] cVisited;
	
	@Override
	public void solveMaze(Maze maze) {
		
		this.maze = maze;
		hCell = maze.entrance;
		tCell = maze.exit;
		hPath.add(hCell);
		tPath.add(tCell);
		
		cVisited = new boolean[maze.sizeR][maze.sizeC];
		for(int i=0; i<maze.sizeR; i++) {
			for(int j=0 ;j<maze.sizeC; j++) {
				cVisited[i][j] = false;
			}
		}
		int dir;
		Cell nCell;
		
		while( !isSolved ) {
			if( hPath.size() <= tPath.size() ) {
				dir = getNeib(hCell);
				if( dir != -1 ) {
					while( dir != -1 ) {
						nCell = hCell.neigh[dir];
						cVisited[nCell.r][nCell.c] = true;
						hPath.add(nCell);
						if( nCell.tunnelTo != null  ) {
							nCell = nCell.tunnelTo;
							cVisited[nCell.r][nCell.c] = true;
							hPath.add(nCell);
						}
						hCell = nCell;
						if( hCell == tCell ) {
							isSolved = true;
							break;
						}
						if( hPath.size() > tPath.size() ) {
							break;
						}
						dir = getNeib(hCell);
					}
				} else {
					hPath.remove(hPath.size()-1);
					hCell = hPath.get(hPath.size()-1);
					if( hCell.tunnelTo != null && getNeib(hCell) == -1 ) {
						hPath.remove(hPath.size()-1);
						hPath.remove(hPath.size()-1);
						hCell = hPath.get(hPath.size()-1);
					}
				}
			} else {
				dir = getNeib(tCell);
				if( dir != -1 ) {
					while( dir != -1 ) {
						nCell = tCell.neigh[dir];
						cVisited[nCell.r][nCell.c] = true;
						tPath.add(nCell);
						if( nCell.tunnelTo != null  ) {
							nCell = nCell.tunnelTo;
							cVisited[nCell.r][nCell.c] = true;
							tPath.add(nCell);
						}
						tCell = nCell;
						if( hCell == tCell ) {
							isSolved = true;
							break;
						}
						if( hPath.size() < tPath.size() ) {
							break;
						}
						dir = getNeib(tCell);
					}
				} else {
					tPath.remove(tPath.size()-1);
					tCell = tPath.get(tPath.size()-1);
					if( tCell.tunnelTo != null && getNeib(tCell) == -1 ) {
						tPath.remove(tPath.size()-1);
						tPath.remove(tPath.size()-1);
						tCell = tPath.get(tPath.size()-1);
					}
				}
			}
		}
		
//		hDFS(hCell);

	} // end of solveMaze()
	
	/*public void hDFS(Cell cell) {
		
		cVisited[cell.r][cell.c] = true;
		
		if( cell.tunnelTo != null ) {
			cell = cell.tunnelTo;
			cVisited[cell.r][cell.c] = true;
		}
		
		int dir = getNeib(cell);
		Cell nCell;
		
		while( dir != -1 ) {
			nCell = cell.neigh[dir];
			hPath.add(nCell);
			hCell = nCell;
			if( hCell == tCell ) {
				isSolved = true;
				return;
			}
			tDFS(tCell);
			if(isSolved) return;
			dir = getNeib(cell);
		}

	}*/
	
	/*public void tDFS(Cell cell) {
		
		cVisited[cell.r][cell.c] = true;
		
		if( cell.tunnelTo != null ) {
			cell = cell.tunnelTo;
			cVisited[cell.r][cell.c] = true;
		}
		
		int dir = getNeib(cell);
		Cell nCell;
		
		while( dir != -1 ) {
			nCell = cell.neigh[dir];
			tPath.add(nCell);
			tCell = nCell;
			if( hCell == tCell ) {
				isSolved = true;
				return;
			}
			hDFS(hCell);
			if(isSolved) return;
			dir = getNeib(cell);
		}

	}*/
	
	public int getNeib(Cell cell) {
		
		ArrayList<Integer> neibDirs = new ArrayList<>();
		Cell nCell;
		for(int i=0; i<Maze.NUM_DIR; i++) {
			nCell = cell.neigh[i];
			if( nCell != null && !cVisited[nCell.r][nCell.c] && !cell.wall[i].present ) {
				neibDirs.add(i);
			}
		}
		int dir = -1;
		if( neibDirs.size() > 0) {
			dir = neibDirs.get( rand.nextInt(neibDirs.size()) );
		}
		return dir;
	}

	@Override
	public boolean isSolved() {
		return isSolved;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		// TODO Auto-generated method stub
		return 0;
	} // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
