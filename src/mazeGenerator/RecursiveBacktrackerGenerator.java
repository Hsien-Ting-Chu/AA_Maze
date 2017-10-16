package mazeGenerator;

import maze.Cell;
import maze.HexMaze;
import maze.Maze;

import java.util.*;

import static maze.Maze.*;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	/** The maze reference. */
	private Maze maze;

	/** The random. */
	private Random rd = new Random();

	/** The visited 2d array to record which cell is visited */
	private boolean visited[][];

	/**
	 * The a direction array for randomly get a neighbor from direction 0 - 5. Random direction
	 */
	private Integer[] dir_arr = { 0, 1, 2, 3, 4, 5 };
	private List<Integer> rand_dir = Arrays.asList(dir_arr);

	/**
	 * Generate maze.
	 * @param maze
	 *            the maze passed in by main function
	 */
	@Override
	public void generateMaze(Maze maze) {
		this.maze = maze;
		// initialize visited 2d array
		visited = new boolean[maze.sizeR][maze.sizeC];
		for (boolean[] r : visited) {
			for (boolean c : r) {
				c = false;
			}
		}

		// Get a random cell to start generating maze
		int rr = 0;
		int rc = 0;
		while (!isIn(rr, rc)) {
			rr = rd.nextInt(maze.sizeR);
			if (maze.type == HEX)
				rc = rd.nextInt(maze.sizeC + (maze.sizeR + 1) / 2);
			else
				rc = rd.nextInt(maze.sizeC);
		}

		Cell randCell = maze.map[rr][rc];
		// Call carvePassage recursively to generate a maze
		carvePassage(randCell);

	} // end of generateMaze()

	/**
	 *
	 * @param cell
	 *            the cell
	 */
	private void carvePassage(Cell cell) {

		setCellVisited(cell);
		if (cell.tunnelTo != null) {
			cell = cell.tunnelTo;
			setCellVisited(cell);
		}
		int dir = randomlyChoseNeighbor(cell);
		Cell neigh;
		while (dir != -1) {
			neigh = cell.neigh[dir];
			cell.wall[dir].present = false;
			carvePassage(neigh);
			dir = randomlyChoseNeighbor(cell);

		}
	}

	/**
	 * Randomly chose a neighbor of a cell.
	 *
	 * @param cell
	 *            the cell
	 * @return the direction of its neighbor.
	 */
	private int randomlyChoseNeighbor(Cell cell) {

		// if(!isIn(cell)) return -1;

		List<Integer> neighsDir = new ArrayList<>();
		Collections.shuffle(rand_dir);
		// Go through random order neighbors of a random cell picked in new cell
		// set
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

} // end of class RecursiveBacktrackerGenerator
