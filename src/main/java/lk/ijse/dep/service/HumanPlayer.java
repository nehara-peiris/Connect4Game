package lk.ijse.dep.service;

public class HumanPlayer extends Player{
    boolean legalMove;
    public HumanPlayer(Board board) {
        super(board);
    }

    @Override
    public void movePiece(int col){
        // checks the human players move is legal or not
        legalMove = board.isLegalMove(col);

        // if it is legal update move and board
        // after the move check for a winner by calling findWinner();
        if (legalMove){
            board.updateMove(col,Piece.BLUE);
            board.getBoardUI().update(col,legalMove);
            Winner winner = board.findWinner();

            // if there is a winner notifyWinner pass it to winner object
            if (winner.getWinningPiece() != Piece.EMPTY){
                board.getBoardUI().notifyWinner(winner);
            }else {
                // if not check for legal moves
                // if board out exists legal moves then call to the winner obj through notifyWinner();
                if (!board.existLegalMoves()){
                    board.getBoardUI().notifyWinner(new Winner(Piece.EMPTY));
                }
            }
        }
    }
}