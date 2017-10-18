package mazeGenerator;

import java.util.*;
import maze.Maze;
import maze.Cell;
import static maze.Maze.HEX;

public class GrowingTreeGenerator implements MazeGenerator {
	// Growing tree maze generator. As it is very general, here we implement as "usually pick the most recent cell, but occasionally pick a random cell"
	
	/** The maze reference. */
	private Maze maze;

	/** The random. */
	private Random rd = new Random();

	
	/** The cell set Z. */
	private List<Cell> cellSetZ;
	
	/**
	 * The a direction array for randomly get a neighbor from direction 0 - 5. Random direction
	 */
	private Integer[] dir_arr = { 0, 1, 2, 3, 4, 5 };
	private List<Integer> rand_dir = Arrays.asList(dir_arr);
	
	/** The visited 2d array to record which cell is visited */	
	private boolean visited[][];

	double threshold = 0.1;
	
	/** 
     * This generator is based on Prim's algorithm for computing minimum spanning tree. We used the
     * modified version of it. Starting with a maze where all walls are present, i.e., between every cell is
     *  a wall, it uses the following procedure to generate a maze:
     * 
     * ******************************************************************************************
     * 
     * ALGORITHM Prim's ( Maze )
     * Perform a Prim's traversal of a graph.
     * Input: Maze maze.
     * OUTPUT : Maze maze with its cellsetZ that includes every cell,
     *          and displays on a graph user interface.
     * 
     * 1: Visited = [][]
     * // mark all cells in both 2D-arrays unvisited
     * for (boolean[] r : visited) {
	 *	for (boolean c : r) {
	 *		c = false;
	 * 		}
	 * 	}
     * end for
     * 2: Add every cell to cellSetZ then make it random by shuffling it. 
     * 3: Pick up a cell from cellSetZ and remove it until cellSetZ is empty.
     * 4: Carve a path between the cells and its neighbour recursively. 
     * 5: Continue this process until we have visited all its neighbours.
     * 
     * ******************************************************************************************
     * 
     * @param maze Input maze.
     */	
	@Override
	public void generateMaze(Maze maze) {
		cellSetZ = new ArrayList<>();
		this.maze = maze;
		
		// initialize visited 2d array
		visited = new boolean[maze.sizeR][maze.sizeC];
		for (boolean[] r : visited) {
			for (boolean c : r) {
				c = false;
			}
		}
		
		//Pick a random cell and add it to setZ
		for (int i = 0; i < maze.sizeR; i++) {
			int initC = 0;
			int sizeC = maze.sizeC;
			
			if (maze.type == HEX) {
				initC = (i + 1) / 2;
				sizeC = sizeC + (i + 1) / 2;
			}
			for (int j = initC; j < sizeC; j++) {
				Cell current = maze.map[i][j];
				cellSetZ.add(current);
			}
		}
		
		//shuffle the cellSetZ 
		Collections.shuffle(cellSetZ);
		// Random pick up a cell from cellSetZ and remove it from cellsetZ
		Cell randCell = cellSetZ.remove(0);

		// Call carvePath recursively to generate a maze
		carvePath(randCell);

	} // end of generateMaze()
	
	private void carvePath(Cell cell) {

		setCellVisited(cell);
		if (cell.tunnelTo != null) {
			cell = cell.tunnelTo;
			setCellVisited(cell);
		}
		int dir = randomlyChoseNeighbour(cell);
		Cell neigh;
		while (dir != -1) {
			neigh = cell.neigh[dir];
			cell.wall[dir].present = false;
			carvePath(neigh);
			dir = randomlyChoseNeighbour(cell);

		}
	}

	/**
	 * Randomly chose a neighbour of a cell.
	 *
	 * @param cell
	 *            the cell
	 * @return the direction of its neighbor.
	 */
	private int randomlyChoseNeighbour(Cell cell) {

		// if(!isIn(cell)) return -1;

		List<Integer> neighsDir = new ArrayList<>();
		Collections.shuffle(rand_dir);
		// Go through random order neighbours of a random cell picked in new cell set
		for (int dir : rand_dir) {
			Cell nei = cell.neigh[dir];
			if (nei != null && !isCellVisited(nei)) {
				neighsDir.add(dir);
			}
		}
		int neighDir = -1;
		if (neighsDir.size() > 0)
			neighDir = neighsDir.get(rd.nextInt(neighsDir.size()));

		return neighDir;
	}

	/**
	 * Checks if coordinates is in the maze
	 *
	 * @param r
	 *            the row index
	 * @param c
	 *            the column index
	 * @return true, if is in
	 */
	protected boolean isIn(int r, int c) {
		boolean result = false;
		if (r >= 0 && r < maze.sizeR && c >= 0 && c < maze.sizeC) {
			result = true;
		}
		if (maze.type == HEX) {
			if (r >= 0 && r < maze.sizeR && c >= (r + 1) / 2 && c < maze.sizeC + (r + 1) / 2)
				;
			result = true;
		}
		return result;
	} // end of isIn()

	/**
	 * Check whether the cell is in the maze.
	 * 
	 * @param cell
	 *            The cell being checked.
	 * @return True if in the maze. Otherwise false.
	 */
	protected boolean isIn(Cell cell) {
		if (cell == null)
			return false;
		return isIn(cell.r, cell.c);
	} // end of isIn()

	/**
	 * Checks if is cell visited.
	 *
	 * @param cell
	 *            the cell
	 * @return true, if is cell visited
	 */
	private boolean isCellVisited(Cell cell) {
		boolean result;
		// Return true to skip it if cell is null
		if (!isIn(cell))
			return true;

		int r = cell.r;
		int c = cell.c;
		if (maze.type == HEX) {
			c = c - (r + 1) / 2;
		}
		try {
			result = visited[r][c];
		} catch (Exception e) {
			result = true;
		}

		return result;
	}

	/**
	 * Sets the cell visited.
	 *
	 * @param cell
	 *            the new cell visited
	 */
	private void setCellVisited(Cell cell) {

		if (!isIn(cell))
			return;

		int r = cell.r;
		int c = cell.c;
		if (maze.type == HEX) {
			c = c - (r + 1) / 2;
		}
		try {
			visited[r][c] = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

} // end of class GrowingTreeGenerator

	
	
	


