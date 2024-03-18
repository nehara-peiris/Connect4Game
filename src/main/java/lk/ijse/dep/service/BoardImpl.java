package lk.ijse.dep.service;

public class BoardImpl implements Board{
    private  Piece [][] pieces = new Piece[NUM_OF_COLS][NUM_OF_ROWS];

    private BoardUI boardUI ;

    public Piece[][] getPieces() {
        return pieces;
    }


    public void setPieces(Piece[][] pieces) {
        this.pieces = pieces;
    }

    public void setBoardUI(BoardUI boardUI) {
        this.boardUI = boardUI;
    }


    public BoardImpl(BoardUI boardUI) {
        System.out.println("check");
        this.boardUI = boardUI;

        for (int i=0;i<NUM_OF_COLS;i++){
            for (int j=0;j<NUM_OF_ROWS;j++){
                pieces[i][j]=Piece.EMPTY;
            }
        }
    }

    public BoardImpl(Piece[][] copiedPieces) {
        Piece[][] pieces1;
        pieces1 = pieces;
        pieces1 =copiedPieces;
        this.pieces = pieces1;
    }

    @Override
    public BoardUI getBoardUI() {
        return boardUI;
    }

    @Override
    public int findNextAvailableSpot(int col) {
        for(int i=0;i<pieces[col].length;i++) {
            if (pieces[col][i].equals(Piece.EMPTY)) {
                return i;
            }
        }
        return -1;

    }

    @Override
    public boolean isLegalMove(int col) {
        return findNextAvailableSpot(col) != -1;

    }

    @Override
    public boolean existLegalMove() {
        for (int i=0;i<NUM_OF_COLS;i++){
            for (int j=0;j<NUM_OF_ROWS;j++){
                if(pieces[i][j].equals(Piece.EMPTY)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateMove(int col, Piece move) {
        for(int i=0;i<pieces[col].length;i++){
            if(pieces[col][i].equals(Piece.EMPTY)){
                pieces[col][i]=move;
                break;
            }
        }

    }
    @Override
    public void updateMove(int col, int row, Piece move) {
        // Validate if the move is legal (empty row within chosen column)
        if (isRowEmpty(col, row)) {
            pieces[col][row] = move;
        } else {
            // Handle illegal move (optional: throw exception, log error, etc.)
            System.out.println("Illegal move! Row " + (row + 1) + " in column " + (col + 1) + " is full.");
        }
    }

    private boolean isRowEmpty(int col, int row) {
        // Check if the specified row in the column is empty
        return pieces[col][row].equals(Piece.EMPTY);
    }


    @Override
    public Winner findWinner() {
        int humanCount, aiCount;

        for (int i=0;i<NUM_OF_COLS;i++){

            humanCount=0;
            aiCount=0;

            for (int j=0;j<NUM_OF_ROWS;j++){
                if(pieces[i][j].equals(Piece.BLUE)){
                    humanCount++;
                    aiCount=0;
                } else if (pieces[i][j].equals(Piece.GREEN)) {
                    aiCount++;
                    humanCount=0;
                }

                if(humanCount==4){
                    return new Winner(Piece.BLUE,i,(j-3),i,j);
                } else if (aiCount==4) {
                    return new Winner(Piece.GREEN,i,(j-3),i,j);
                }

            }

        }

        for (int i=0;i<NUM_OF_ROWS;i++){

            humanCount=0;
            aiCount=0;

            for (int j=0;j<NUM_OF_COLS;j++){

                if(pieces[j][i].equals(Piece.BLUE)){
                    humanCount++;
                    aiCount=0;
                } else if (pieces[j][i].equals(Piece.GREEN)) {
                    aiCount++;
                    humanCount=0;
                }else {
                    aiCount=0;
                    humanCount=0;
                }

                if(humanCount==4){
                    return new Winner(Piece.BLUE,(j-3),i,j,i);
                } else if (aiCount==4) {
                    return new Winner(Piece.GREEN,(j-3),i,j,i);
                }

            }
        }
        return new Winner(Piece.EMPTY);
    }


}
