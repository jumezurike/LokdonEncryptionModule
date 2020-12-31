package com.lokdonencryption.lokdonencrypt;

import java.util.ArrayList;

public class KnightCell {

    private int x, y;
    private int boardWidth, boardHeight;
    private int indexNumber;
    private int selectionStep;

    private KnightCell board[];
    private KnightCell next;
    private KnightCell prev;
    private ArrayList<KnightCell> neighbours;
    private ArrayList<Integer> deadEnds;

    public KnightCell() {
        neighbours = new ArrayList<KnightCell>();
        deadEnds = new ArrayList<Integer>();
        selectionStep = -1; // not assigned
        next = null;
        prev = null;
    }

    private void initNeighbours() {
        // Top
        addNeighbour(+1, -2);
        addNeighbour(-1, -2);

        // Bottom
        addNeighbour(+1, +2);
        addNeighbour(-1, +2);

        // Left
        addNeighbour(-2, +1);
        addNeighbour(-2, -1);

        // Right
        addNeighbour(+2, +1);
        addNeighbour(+2, -1);
    }

    private void addNeighbour(int dx, int dy) {
        int xx = x + dx;
        int yy = y + dy;

        if (xx >= 0 && xx < boardWidth && yy >= 0 && yy < boardHeight) {
            //  DebugUtil.printLog(indexNumber+ "  board Neighbour Index "+(xx + boardHeight * yy));
            neighbours.add(board[xx + boardHeight * yy]);// need to check
        }
    }

    private boolean isDeadEnd(KnightCell c) {
        for (int i = 0; i < deadEnds.size(); i++) {
            if (c.getIndexNumber() == deadEnds.get(i)) {
                return true;
            }
        }

        return false;
    }

    public void setIndexNumber(int i, int w, int h) {
        indexNumber = i;
        boardWidth = w;
        boardHeight = h;

        x = indexNumber % h;
        y = indexNumber / w;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setBoardHandle(KnightCell[] b) {
        board = b;

        initNeighbours();
    }

    public void setSelectionStep(int step) {
        selectionStep = step;
    }

    public int getSelectionStep() {
        return selectionStep;
    }

    public void setNext(KnightCell c) {
        next = c;
    }

    public KnightCell getNext() {
        return next;
    }

    public void setPrevious(KnightCell c) {
        prev = c;
    }

    public KnightCell getPrevious() {
        return prev;
    }

    public void addDeadEnd(KnightCell c) {
        deadEnds.add(c.getIndexNumber());
    }

    public ArrayList<Integer> getDeadEnds() {
        return deadEnds;
    }

    public void resetState() {
        selectionStep = -1;
        neighbours.clear();
        deadEnds.clear();
        next = null;
        prev = null;
    }

    public ArrayList<KnightCell> getPossibleMoves() {
        ArrayList<KnightCell> moves = new ArrayList<KnightCell>();

        for (int i = 0; i < neighbours.size(); i++) {
            KnightCell c = neighbours.get(i);
            if (c.getSelectionStep() < 0 && !isDeadEnd(c)) {
                moves.add(c);
            }
        }
        return moves;
    }
}
