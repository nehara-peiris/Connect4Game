package lk.ijse.dep.service;

import java.util.ArrayList;
import java.util.List;

public class BoardImpl implements Board {

    private final Piece [][] pieces;

    private final BoardUI boardUI;

    public Piece piece = Piece.BLUE;

    public int cols;

    public BoardImpl(BoardUI boardUI) {
        this.boardUI = boardUI;
        this.pieces = new Piece[6][5];
        // make sure the board is empty
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                pieces[i][j] = Piece.EMPTY;
            }
        }
    }

    public BoardImpl(Piece[][] pieces, BoardUI boardUI) {
        // initialize the board with pieces and board UI
        this.pieces = new Piece[6][5];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                this.pieces[i][j] = pieces[i][j];
            }
        }
        this.boardUI = boardUI;
    }

    public Piece[][] getPieces() {
        return pieces;
    }
    public BoardUI getBoardUI() {
        return this.boardUI;
    }

    @Override
    public int findNextAvailableSpot(int col) {
        // find next available spot to place a ball
        // if the cell is filled then return -1;
        for (int i = 0; i < pieces[col].length; i++) {
            if (pieces[col][i] == Piece.EMPTY){
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isLegalMove(int col) {
        // see whether the placing piece is available
        int index = findNextAvailableSpot(col);
        return index != -1;
    }

    @Override
    public boolean existLegalMoves() {
        // see whether there are any legal moves in the column
        for (int i = 0; i < pieces.length; i++) {
            if (isLegalMove(i)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateMove(int col, Piece move) {
        // update the latest move
        this.cols = col;
        this.piece = move;

        // find next available spot
        int index = findNextAvailableSpot(col);
        pieces[col][index] = move;
    }

    @Override
    public void updateMove(int col, int row, Piece move) {
        // update the latest move with specified column and row
        pieces[col][row]=move;
    }

    @Override
    public Winner findWinner() {
        // checking horizontally
        int count = 0;
        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length-1; j++){
                if (pieces[i][j] == pieces[i][j+1]){
                    count++;
                    if (count == 3 && pieces[i][j] != Piece.EMPTY){
                        return new Winner(pieces[i][j], i, (j-2), i, (j+1));
                    }
                }
                else{
                    count = 0;
                }
            }
            count = 0;
        }

        // checking vertically
        count = 0;
        for (int i = 0; i < pieces[0].length; i++){
            for (int j = 0; j < pieces.length-1; j++){
                if (pieces[j][i] == pieces[j+1][i]){
                    count++;
                    if (count == 3 && pieces[j][i] != Piece.EMPTY){
                        return  new Winner(pieces[j][i], (j-2), i, (j+1), i);
                    }
                }
                else{
                    count = 0;
                }
            }
            count = 0;
        }
        return new Winner(Piece.EMPTY);
    }

    @Override
    public BoardImpl getBoardImpl() {
        return this;
    }


    public List<BoardImpl> getAllLegalNextMoves() {
        // get the next color
        Piece nextPiece = piece == Piece.BLUE ? Piece.GREEN : Piece.BLUE;

        // create arraylist and store next moves
        List<BoardImpl> nextMoves = new ArrayList<>();

        // iterate the columns and find available spots in column
        for (int i = 0; i < 6; i++) {
            int raw = findNextAvailableSpot(i);

            // if there is available spot then it creates board which is similar to the current board
            if (raw != -1){
                BoardImpl legalMove = new BoardImpl(this.pieces, this.boardUI);
                // update the move and the board
                legalMove.updateMove(i,nextPiece);
                nextMoves.add(legalMove);
            }
        }
        return  nextMoves;
    }

    public BoardImpl getRandomLegalNextMove() {
        // get all legal moves
        final List<BoardImpl> legalMoves = getAllLegalNextMoves();
        // if there is no legal moves return null
        if (legalMoves.isEmpty()) {
            return null;
        }

        // generate random move with legal moves
        final int random = RANDOM_GENERATOR.nextInt(legalMoves.size());
        return legalMoves.get(random);
    }


    public boolean getStatus(){
        // if the game out of legal moves retrn false
        if (!existLegalMoves()){
            return false;
        }

        Winner winner = findWinner();

        // when there is a winner return false
        if (winner.getWinningPiece() != Piece.EMPTY){
            return false;
        }
        return true;
    }
}