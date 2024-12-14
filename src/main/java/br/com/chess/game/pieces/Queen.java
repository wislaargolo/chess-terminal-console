package br.com.chess.game.pieces;

import br.com.chess.game.boardgame.Board;
import br.com.chess.game.boardgame.ChessPiece;
import br.com.chess.game.boardgame.Position;
import br.com.chess.game.chess.utils.Color;

public class Queen extends ChessPiece {

    public Queen(Board board, Color color) {
        super(board, color);
    }

    /*@ also
      @ public normal_behavior
      @     ensures \result.equals("A");
      @ pure
      @*/
    @Override
    public String getString() {
        return "A";
    }

    @Override
    public boolean[][] possibleMoves() {

        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // straight directions (rook-like)
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // diagonal directions (bishop-like)
        };


        /*@ maintaining 0 <= i && i <= directions.length;
          @ maintaining (\forall int x, y;
          @     0 <= x && x < mat.length && 0 <= y && y < mat[x].length;
          @     mat[x][y] ==> (
          @         getBoard().positionExistsBasic(x, y) &&
          @         (getBoard().pieces[x][y] == null ||
          @         (getBoard().pieces[x][y] instanceof ChessPiece &&
          @          getBoard().pieces[x][y].getColor() != this.getColor()))));
          @ decreasing directions.length - i;
          @*/
        for (int i = 0; i < directions.length; i++) {
            int dx = directions[i][0];
            int dy = directions[i][1];

            p.setValues(position.getRow(), position.getColumn());

            /*@ maintaining getBoard().positionExistsBasic(p.getRow(), p.getColumn());
              @ maintaining 0 <= p.getRow() && p.getRow() < getBoard().getRows();
              @ maintaining 0 <= p.getColumn() && p.getColumn() < getBoard().getColumns();
              @ maintaining (\forall int x, y;
              @     0 <= x && x < mat.length && 0 <= y && y < mat[x].length;
              @     mat[x][y] ==> (
              @         getBoard().positionExistsBasic(x, y) &&
              @         (getBoard().pieces[x][y] == null ||
              @         (getBoard().pieces[x][y] instanceof ChessPiece &&
              @          getBoard().pieces[x][y].getColor() != this.getColor()))));
              @ decreasing dx != 0 ? (dx > 0 ? getBoard().getRows() - p.getRow() : p.getRow()) : (dy > 0 ? getBoard().getColumns() - p.getColumn() : p.getColumn());
              @*/
            while (getBoard().positionExists(new Position(p.getRow() + dx, p.getColumn() + dy))) {
                p.setValues(p.getRow() + dx, p.getColumn() + dy);

                if (!getBoard().thereIsAPiece(p)) {
                    mat[p.getRow()][p.getColumn()] = true;
                } else {
                    mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
                    break;
                }
            }
        }

        //@ assert mat.length == getBoard().getRows();
        //@ assert (\forall int i; 0 <= i && i < mat.length;
        //@         mat[i] != null && mat[i].length == getBoard().getColumns());
        //@ assert (\forall int x, y;
        //@         0 <= x && x < mat.length && 0 <= y && y < mat[x].length;
        //@         mat[x][y] ==> (getBoard().positionExistsBasic(x, y) &&
        //@                        (getBoard().pieces[x][y] == null ||
        //@                         getBoard().pieces[x][y].getColor() != this.getColor())));
        return mat;
    }
}
