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

	/** The dir_arr.Random direction */
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

		/**Pick a random cell and add it to setZ */
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
		// Random pick up a cell from cellSetZ and remove it
		Cell randCell = cellSetZ.remove(0);
		// Add the cell picked up from cellSetZ to cellSetF 
		cellSetF.add(randCell);

		do {

			Collections.shuffle(cellSetF);
			// Pick a random cell in cell setF
			boolean found = false;
			// Indicator for finding a neighbor not in cell setF
			for (Cell temp : cellSetF) {
				// Shuffle direction array
				Collections.shuffle(rand_dir);
				// Go through random order neighbors of a random cell picked in cell setF
				for (int dir : rand_dir) {
					Cell nei = temp.neigh[dir];
					// If this neighbor can be found in cell setZ
					if (cellSetZ.contains(nei)) {
						// remove it from cellSetZ then add it to cell setF
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
