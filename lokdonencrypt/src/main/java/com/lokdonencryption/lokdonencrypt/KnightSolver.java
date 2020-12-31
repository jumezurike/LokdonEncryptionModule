package com.lokdonencryption.lokdonencrypt;

import java.util.ArrayList;

public class KnightSolver {

    private int width, height;
    private int startX, startY;
    private int knightMoveArray[];
    // Calculation limits
    private int calculatedSolutions;
    private int requestedSolutions;
    private float prevPercentage;

    private boolean runForever;

    private KnightCell board[];

    private KnightCell startCell;
    private KnightCell currentCell;

    public KnightSolver(int w, int h) {

        // Init the height and width to the given
        this.width = w;
        this.height = h;
        prevPercentage = 0.0f;
        runForever = false;
        knightMoveArray = new int[w * h];
        // Reserve memory for the cell objects
        board = new KnightCell[width * height];
        for (int i = 0; i < board.length; i++) {
            board[i] = new KnightCell();
        }
        // Init cell states
        for (int i = 0; i < w * h; i++) {
            KnightCell c = (board[i]);

            c.setIndexNumber(i, width, height);
            c.setBoardHandle(board);
        }
    }

    // Set the initial position of the solver
    public void setPosition(int xx, int yy) {
        this.startX = xx;
        this.startY = yy;
        // Init the cells
        startCell = (board[startX + startY * height]);
        currentCell = startCell;
        startCell.setSelectionStep(0);
        // loadPositionData();
    }

    // Loads the old state of the board if one exists so that we dont get the same paths
    void loadPositionData() {
        int currentCellIndex = 0;
        currentCell = board[currentCellIndex];

        // Read selection steps and dead ends
        for (int i = 0; i < width * height; i++) {
            int selStep;
            KnightCell c = board[i];
            c.setSelectionStep(i);
        }

        // Fix the next-previous relations of the cells after loading since the cells are just initialized to the memory and contain carbage
        KnightCell cellsInOrder = startCell;
        int cellsGoneThrough = 1;

        while (cellsGoneThrough <= currentCell.getSelectionStep()) {
            for (int i = 0; i < width * height; i++) {
                KnightCell c = board[i];// + i;

                // This is the next cell
                if (c.getSelectionStep() == cellsGoneThrough) {
                    cellsInOrder.setNext(c);
                    c.setPrevious(cellsInOrder);

                    cellsInOrder = c;
                    cellsGoneThrough++;
                }
            }
        }

    }

    // The actual calculations loop
    public int[] calculate(int numPaths) {

// Pravesh
        calculatedSolutions = 0;
        //requestedSolutions = numPaths;

        // Open the file for writing of the paths before we start anything ( this way we dont have to open and close the file )
//        std::stringstream ss;
//        ss << "./paths/" << startX + startY * width << ".txt";
//        globalFileHandle.open(ss.str().c_str(), std::ifstream::out  | std::fstream::app);
        // Calculate the paths
        return loopSolver();

        // Close the file
        // globalFileHandle.close();
//
//        std::cout << "-----" << std::endl;
//        std::cout << calculatedSolutions << " new paths calculated to file '" << ss.str() << "'" << std::endl;
//        std::cout << "Done!" << std::endl;
    }

    public int[] loopSolver() {
        while (true) {
            boolean jobDone = nextStep();
            if (jobDone) {
                return knightMoveArray;
            }
        }
    }

    // Returns whether the job is done or not
    boolean nextStep() {

        ArrayList<KnightCell> nextCells = currentCell.getPossibleMoves();

        if (nextCells.size() > 0) {
            // We got potential next moves from this cell

            KnightCell bestCell = nextCells.get(0);
            // DebugUtil.printLog("Cell Index "+bestCell.getIndexNumber());
            int movesFromBest = bestCell.getPossibleMoves().size();

            // Check all the possible moves with an heuristic that finds the cell with the least possible moves from it
            for (int i = 1; i < nextCells.size(); i++) {
                KnightCell c = nextCells.get(i);

                int movesFromNew = c.getPossibleMoves().size();

                if (movesFromNew < movesFromBest) {
                    // We found a new best cell
                    bestCell = c;
                    movesFromBest = movesFromNew;
                }

            }

            // Go to the new cell with the solver
            KnightCell previousCell = currentCell;
            previousCell.setNext(bestCell);
            previousCell.addDeadEnd(bestCell); // so that we wont visit the new best cell from this cell anymore in the future

            currentCell = bestCell;
            currentCell.setPrevious(previousCell);
            currentCell.setSelectionStep(previousCell.getSelectionStep() + 1);

        } else {
            // It's a dead end or we are at the end of the path

            if (currentCell.getSelectionStep() == ((width * height) - 1)) { // at 255 or equivalent
                // == > Successful path
                if (isOpenPath()) {
                    writeSolution();
                    calculatedSolutions++;


                    /*
                float newPercentage = ((float)calculatedSolutions / requestedSolutions);
				if( newPercentage >= (prevPercentage + 0.1)) 	{
					prevPercentage = newPercentage;
					std::cout << calculatedSolutions << " of " << requestedSolutions << " paths calculated (" << newPercentage * 100 << "%)" << std::endl;
				}
                     */
                    // Save the path permutation data every 1000 paths so we can resume from where we left
                    if (calculatedSolutions % 1000 == 0) {
                        saveState();
                    }

                }
            }

            // Not a complete path
            // Backtrack in the path so we can make more solutions
            KnightCell bc = currentCell.getPrevious();
            currentCell.resetState();
            currentCell = bc;

            /*
        if(calculatedSolutions >= requestedSolutions)
			return true;
             */
            // Check if we have ran past the time limit
            if (System.currentTimeMillis() + endTime > System.currentTimeMillis()) {
                return true;

            }
        }

        return false;
    }

    long endTime = 30 * 1000;

    // resets the solver
    public void reset() {
        for (int i = 0; i < width * height; i++) {
            KnightCell c = board[i];// + i;
            c.resetState();
        }

        startCell.setSelectionStep(0);
        currentCell = startCell;

    }

    // Check if the start cell can be accessed from the end cell
    boolean isOpenPath() {
        ArrayList<KnightCell> nextCells = currentCell.getPossibleMoves();

        for (int i = 0; i < nextCells.size(); i++) {
            if (startCell == nextCells.get(i)) {
                return false;
            }
        }

        return true;
    }

    // write path to file
    void writeSolution() {

        // Pravesh
        //globalFileHandle << std::endl;
        // Go through the cells
        // KnightCell c = startCell;
        int n = 0;
        // std::stringstream output;

        // Sort by table cells
        for (int i = 0; i < width * height; i++) {
            KnightCell c = board[i];// + i;
            knightMoveArray[i] = c.getSelectionStep();
//            System.out.print(+c.getSelectionStep() + "");
////            output << c.getSelectionStep();
////
//            n++;
//            // Formatting
//            if (n == width) {
//                DebugUtil.printLog("   ");
//                n = 0;
//            } else {
//                System.out.print(" ");
//            }

        }


        /*
	// Sort by order of moves
	while(c != NULL)	{
		output << c->getIndexNumber();
		n++;

		c = c->getNext();

		// Formatting
		if( n == width )	{
			output << std::endl << std::endl;
			n = 0;
		} else {
		 	output << " ";
		}

	}
         */
        //globalFileHandle << output.str();
        //globalFileHandle.flush();
//
    }

    void saveState() {
        // Pravesh
//        std::stringstream ss;
//        ss << "./rundata/" << startX + startY * width << ".dat";
//
//        std::fstream fileHandle;
//        fileHandle.open(ss.str().c_str(), std::ifstream::out);
//
//        if(fileHandle.is_open())	{
//            // Write the cell index where we left
//            fileHandle << currentCell->getIndexNumber() << std::endl;
//
//            for(int i = 0; i < width * height; i++)	{
//                KnightCell c  = board + i;
//
//                // Write the selection steps of the cells
//                fileHandle << c->getSelectionStep() << " ";
//
//
//                // Write the dead ends
//                std::vector<int> deadEnds = c->getDeadEnds();
//                unsigned int size = deadEnds.size();
//                fileHandle << size << " ";
//
//                for(unsigned int n = 0; n < size; n++)	{
//                    fileHandle << deadEnds[n] << " ";
//                }
//
//
//                fileHandle << std::endl;
//
//            }
//
//
//
//        }
    }

    void cleanUp() {

        saveState();

        board = null;
    }
}
