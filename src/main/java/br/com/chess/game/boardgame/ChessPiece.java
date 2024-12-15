package br.com.chess.game.boardgame;
import br.com.chess.game.chess.ChessPosition;
import br.com.chess.game.chess.utils.Color;
import br.com.chess.game.pieces.Rook;

public abstract class ChessPiece {

    protected  /*@ nullable */ Position position; //@ in modelPosition;
    private Board board; //@ in modelBoard;

    private final Color color; //@ in modelColor;
    private int moveCount; //@ in modelCount;

    //@ public model Color modelColor;
    //@ private represents modelColor = this.color;

    //@ public model int modelCount;
    //@ private represents modelCount = this.moveCount;

    //@ public model int maxMove;

    //@ public model /*@ nullable */ Position modelPosition;
    //@ private represents modelPosition = this.position;

    //@ public model Board modelBoard;
    //@ private represents modelBoard = this.board;

    /*@ public normal_behavior
      @     ensures modelBoard == board;
      @     ensures modelPosition == null;
      @     ensures modelColor == color;
      @     ensures modelCount == 0;
      @ pure
      @*/
    public ChessPiece(Board board, Color color) {
        this.board = board;
        position = null;
        this.color = color;
        this.moveCount = 0;
    }

    /*@ public normal_behavior
      @     ensures \result == modelBoard;
      @ pure
      @*/
    public Board getBoard() {
        return board;
    }

    /*@ public normal_behavior
      @     ensures \result == modelPosition;
      @ pure
      @*/
    public /*@ nullable */ Position getPosition() {
        return position;
    }


    /*@ public normal_behavior
      @     ensures modelPosition == position;
      @     assignable modelPosition;
      @*/
    public void setPosition(/*@ nullable*/ Position position) {
        this.position = position;
    }

    /*@ requires modelPosition != null;
      @ requires modelPosition.row >= 0 && modelPosition.row < modelBoard.rows;
      @ requires modelPosition.column >= 0 && modelPosition.column < modelBoard.columns;
      @ requires modelPosition.column + maxMove <= Integer.MAX_VALUE;
      @ requires modelPosition.row + maxMove <= Integer.MAX_VALUE;
      @ ensures \result.length == modelBoard.rows;
      @ ensures (\forall int i; 0 <= i && i < \result.length;
      @             \result[i].length == modelBoard.columns);
      @ ensures (\forall int i, j;
      @         0 <= i && i < modelBoard.rows &&
      @         0 <= j && j < modelBoard.columns;
      @         \result[i][j] == true ==>
      @             (i >= 0 && j >=0 && i < modelBoard.rows && j < modelBoard.columns) &&
      @             (modelBoard.pieces[i][j] == null ||
      @             (modelBoard.pieces[i][j]).modelColor != this.modelColor ||
      @             (modelBoard.pieces[i][j] instanceof Rook &&
      @             (modelBoard.pieces[i][j]).modelColor == this.modelColor &&
      @              (modelBoard.pieces[i][j]).modelCount == 0)));
      @ also
      @ requires modelPosition == null ||
      @ !(modelPosition.row >= 0 && modelPosition.row < modelBoard.rows && modelPosition.column >= 0 && modelPosition.column < modelBoard.columns);
      @ ensures \result.length == modelBoard.rows;
      @ ensures (\forall int i; 0 <= i && i < \result.length;
      @             \result[i].length == modelBoard.columns);
      @ ensures (\forall int i, j;
      @         0 <= i && i < modelBoard.rows &&
      @         0 <= j && j < modelBoard.columns;
      @         \result[i][j] == false);
      @ pure
      @*/
    public abstract boolean[][] possibleMoves();

    /*@ requires modelPosition != null;
      @ requires modelPosition.row >= 0 && modelPosition.row < modelBoard.rows;
      @ requires modelPosition.column >= 0 && modelPosition.column < modelBoard.columns;
      @ requires position.getRow() >= 0 && position.getRow() < modelBoard.rows;
      @ requires position.getColumn() >= 0 && position.getColumn() < modelBoard.columns;
      @ requires modelPosition.getColumn() + maxMove <= Integer.MAX_VALUE;
      @ requires modelPosition.getRow() + maxMove <= Integer.MAX_VALUE;
      @ also
      @ requires modelPosition == null ||
      @ !(modelPosition.row >= 0 && modelPosition.row < modelBoard.rows && modelPosition.column >= 0 && modelPosition.column < modelBoard.columns) ||
      @ !(position.getRow() >= 0 && position.getRow() < modelBoard.rows && position.getColumn() >= 0 && position.getColumn() < modelBoard.columns);
      @ ensures \result == false;
      @ pure
      @*/
    public boolean possibleMove(Position position) {
        if(this.position == null || !board.positionExists(this.position) || !board.positionExists(position)) {
            return false;
        }

        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    /*@ requires (modelPosition != null && modelPosition.row >= 0
      @ && modelPosition.row < modelBoard.rows &&
      @ modelPosition.column >= 0 && modelPosition.column < modelBoard.columns) ==>
      @ (modelPosition.column + maxMove <= Integer.MAX_VALUE &&
      @ modelPosition.row + maxMove <= Integer.MAX_VALUE);
      @ pure
      @*/
    public boolean isThereAnyPossibleMove() {
        boolean[][] mat = possibleMoves();

        /*@ maintaining 0 <= i && i <= mat.length;
          @ maintaining (\forall int k, l;
          @     0 <= k && k < i && 0 <= l && l < mat[k].length;
          @     !mat[k][l]);
          @ decreasing mat.length - i;
          @*/
        for (int i = 0; i < mat.length; i++) {
            /*@ maintaining 0 <= j && j <= mat[i].length;
              @ maintaining (\forall int l;
              @     0 <= l && l < j; !mat[i][l]);
              @ decreasing mat[i].length - j;
              @*/
            for (int j = 0; j < mat[i].length; j++) {
                if (mat[i][j]) {
                    /*@ assert (\exists int k, l;
                      @     0 <= k && k < mat.length && 0 <= l && l < mat[k].length;
                      @     mat[k][l]);
                      @*/
                    return true;
                }
            }
        }
        return false;
    }

    /*@ public normal_behavior
      @     ensures \result == modelColor;
      @ pure
      @*/
    public Color getColor() {
        return color;
    }

    /*@ public normal_behavior
      @     ensures \result == modelCount;
      @ pure
      @*/
    public int getMoveCount() {
        return moveCount;
    }

    /*@ public normal_behavior
      @     requires modelCount < Integer.MAX_VALUE;
      @     ensures modelCount == \old(modelCount) + 1;
      @ assigns modelCount;
      @*/
    public void increaseMoveCount(){
        moveCount++;
    }

    /*@ public normal_behavior
      @     requires modelCount > Integer.MIN_VALUE;
      @     ensures modelCount == \old(modelCount) - 1;
      @ assigns modelCount;
      @*/
    public void decreaseMoveCount(){
        moveCount--;
    }


    /*@ public normal_behavior
      @     requires position.getRow() >= 0 && position.getColumn() >= 0;
      @     requires position.getRow() < modelBoard.rows && position.getColumn() < modelBoard.columns;
      @     requires modelBoard.pieces[position.getRow()][position.getColumn()] == null;
      @     ensures \result == false;
      @ also
      @ public normal_behavior
      @     requires position.getRow() >= 0 && position.getColumn() >= 0;
      @     requires position.getRow() < modelBoard.rows && position.getColumn() < modelBoard.columns;
      @     requires modelBoard.pieces[position.getRow()][position.getColumn()] != null;
      @     ensures \result == ((modelBoard.pieces[position.getRow()][position.getColumn()]).modelColor != this.modelColor);
      @ pure
      @*/
    public boolean isThereOpponentPiece(Position position) {
        /*@ nullable*/ ChessPiece piece = getBoard().piece(position);
        if (piece == null) {
            return false;
        }
        return piece.getColor() != getColor();
    }

    /*@ public normal_behavior
      @     requires modelPosition != null;
      @     requires modelPosition.getRow() >= 0 && modelPosition.getRow() <= 7;
      @     requires modelPosition.getColumn() >= 0 && modelPosition.getColumn() <= 7;
      @     requires modelPosition.getColumn() <= Character.MAX_VALUE - 'a';
      @     requires 8 - modelPosition.getRow() <= Integer.MAX_VALUE;
      @     ensures \result != null;
      @     ensures \result.getRow() == 8 - modelPosition.getRow();
      @     ensures \result.getColumn() == (char) ('a' + modelPosition.getColumn());
      @     assignable \nothing;
      @ also
      @ public normal_behavior
      @     requires modelPosition == null;
      @     ensures \result == null;
      @     assignable \nothing;
      @ also
      @ public exceptional_behavior
      @     requires modelPosition != null;
      @     requires modelPosition.getRow() < 0 || modelPosition.getRow() > 7 || modelPosition.getColumn() < 0 || modelPosition.getColumn() > 7;
      @     requires modelPosition.getColumn() <= Character.MAX_VALUE - 'a';
      @     requires 'a' + modelPosition.getColumn() >= 0;
      @     requires 8 - modelPosition.getRow() <= Integer.MAX_VALUE;
      @     signals_only RuntimeException;
      @     assignable \nothing;
      @*/
    public /*@ nullable*/ ChessPosition getChessPosition(){
        return ChessPosition.fromPosition(position);
    }

    //@ pure
    public abstract String getString();
}
