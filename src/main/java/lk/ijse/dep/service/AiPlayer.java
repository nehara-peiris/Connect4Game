package lk.ijse.dep.service;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class AiPlayer extends Player {
    private static final int NUM_SIMULATIONS = 1000;
    private final Random random;

    public AiPlayer(Board board) {
        super(board);
        random = new Random();
    }


    @Override
    public void movePiece(int col) {
        col = monteCarloTreeSearch();
        board.updateMove(col, Piece.GREEN);
        board.getBoardUI().update(col, false);
        Winner winner = board.findWinner();

        if (winner.getWinningPiece() != Piece.EMPTY) {
            board.getBoardUI().notifyWinner(winner);
        } else if (!board.existLegalMove()) {
            board.getBoardUI().notifyWinner(new Winner(Piece.EMPTY));
        }
    }


    private int monteCarloTreeSearch() {
        int[] scores = new int[Board.NUM_OF_COLS];
        int maxScore = Integer.MIN_VALUE;
        List<Integer> bestColumns = new ArrayList<>();

        for (int col = 0; col < Board.NUM_OF_COLS; col++) {
            if (board.isLegalMove(col)) {
                int row = board.findNextAvailableSpot(col);
                scores[col] = simulate(col);
                if (scores[col] > maxScore) {
                    maxScore = scores[col];
                    bestColumns.clear(); // Clear previous best columns
                    bestColumns.add(col);
                } else if (scores[col] == maxScore) {
                    bestColumns.add(col);
                }
                board.updateMove(col, row, Piece.EMPTY); // Undo the move
            }
        }

        return bestColumns.get(random.nextInt(bestColumns.size()));
    }

    private int simulate(int col) {
        int wins = 0;
        for (int i = 0; i < AiPlayer.NUM_SIMULATIONS; i++) {
            BoardImpl copyBoard = copyBoard();
            int row = copyBoard.findNextAvailableSpot(col);
            copyBoard.updateMove(col, row, Piece.GREEN);

            while (true) {
                Winner winner = copyBoard.findWinner();
                if (winner.getWinningPiece() == Piece.GREEN) {
                    wins++;
                    break;
                } else if (winner.getWinningPiece() == Piece.BLUE || !copyBoard.existLegalMove()) {
                    break;
                }

                int randomCol;
                do {
                    randomCol = random.nextInt(Board.NUM_OF_COLS);
                } while (!copyBoard.isLegalMove(randomCol));

                int randomRow = copyBoard.findNextAvailableSpot(randomCol);
                copyBoard.updateMove(randomCol, randomRow, Piece.BLUE);
            }
        }
        return wins;
    }

    private BoardImpl copyBoard() {
        int numRows = Board.NUM_OF_ROWS;
        int numCols = Board.NUM_OF_COLS;
        Piece[][] copiedPieces = new Piece[numCols][numRows];

        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                copiedPieces[i][j] = board.getPieces()[i][j];
            }
        }

        return new BoardImpl(copiedPieces);
    }


}