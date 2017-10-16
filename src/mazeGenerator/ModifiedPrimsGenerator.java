package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.*;

import static maze.Maze.HEX;

public class ModifiedPrimsGenerator implements MazeGenerator {

	/** The cell set Z. */
	private List<Cell> cellSetZ;

	/** The cell set F. */
	private List<Cell> cellSetF;

	/** The dir_arr. */
	// Random direction
	private Integer[] dir_arr = { 0, 1, 2, 3, 4, 5 };

	/** The rand_dir. */
	private List<Integer> rand_dir = Arrays.asList(dir_arr);

	/**
	 * Generate maze.
	 *
	 * @param maze
	 *            the maze
	 */
	@Override
	public void generateMaze(Maze maze) {
		cellSetZ = new ArrayList<>();
		cellSetF = new ArrayList<>();

		for (int i = 0; i < maze.sizeR; i++) {
			int initC = 0, sizeC = maze.sizeC;
			if (maze.type == HEX) {
				initC = (i + 1) / 2;
				sizeC = sizeC + (i + 1) / 2;
			}
			for (int j = initC; j < sizeC; j++) {
				Cell current = maze.map[i][j];
				cellSetZ.add(current);
			}
		}
		Collections.shuffle(cellSetZ);
		// Initial a cell in new cell set
		Cell randCell = cellSetZ.remove(0);
		cellSetF.add(randCell);

		do {

			Collections.shuffle(cellSetF);
			// Pick a random cell in new cell set
			boolean found = false;
			// Indicator for finding a neighbor not in original cell set
			for (Cell temp : cellSetF) {
				// Shuffle direction array
				Collections.shuffle(rand_dir);
				// Go through random order neighbors of a random cell picked in
				// new cell set
				for (int dir : rand_dir) {
					Cell nei = temp.neigh[dir];
					// If this neighbor can be found in cellset
					if (cellSetZ.contains(nei)) {
						// remove it from cellSet then add it to new cell set
						cellSetZ.remove(nei);
						cellSetF.add(nei);
						temp.wall[dir].present = false;
						found = true;
						break;
					}
				}
				//
				if (found)
					break;
			}

		} while (cellSetZ.size() != 0);
	} // end of generateMaze()
} // end of class ModifiedPrimsGenerator
