package mazeGenerator;

import maze.Cell;
import maze.Maze;
import java.util.*;
import static maze.Maze.HEX;
/**
 *  @author Hsien-Ting Chu s3592460
 */
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
     * 1: cellSetZ = new ArrayList<>();
	 *	  cellSetF = new ArrayList<>();
     * // initialize cellSetZ and cellSetF
     * 2: Add every cell to cellSetZ then make it random by shuffling it. 
     * 3: Pick up a cell from cellSetZ and remove it until cellSetZ is empty.
     * 4: Add the cell picked up from cellSetZ to cellSetF.
     * 5: Pick a random cell in cell setF
     * 6: Go through random order neighbors of a random cell picked in cell setF
     * 7: If this neighbor can be found in cell setZ, then remove it from cellSetZ then add it to cell setF
     * 
     * ******************************************************************************************
     * 
     * @param maze Input maze.
     */
	@Override
	public void generateMaze(Maze maze) {
		cellSetZ = new ArrayList<>();
		cellSetF = new ArrayList<>();

		/**Add every cell to cellSetZ */
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
		//Pick up a cell from cellSetZ and remove it
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
