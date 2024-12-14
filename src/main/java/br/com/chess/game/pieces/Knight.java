package br.com.chess.game.pieces;


import br.com.chess.game.boardgame.Board;
import br.com.chess.game.boardgame.ChessPiece;
import br.com.chess.game.boardgame.Position;
import br.com.chess.game.chess.utils.Color;

public class Knight extends ChessPiece {

    //@ public represents maxMove = 2;

    public Knight(Board board, Color color) {
        super(board, color);
    }

    /*@ also
      @ public normal_behavior
      @     ensures \result.equals("C");
      @ pure
      @*/
    @Override
    public String getString(){
        return "C";
    }


    /*@ requires getBoard().positionExists(position);
      @ ensures \result == (getBoard().pieces[position.getRow()][position.getColumn()] == null ||
      @     (getBoard().pieces[position.getRow()][position.getColumn()] instanceof ChessPiece &&
      @     (getBoard().pieces[position.getRow()][position.getColumn()]).getColor() != this.getColor()));
      @ pure helper
      @*/
    private boolean canMove(Position position) {
        /*@ nullable */ ChessPiece p = getBoard().piece(position);
        if (p == null) {
            return true;
        }
        return p.getColor() != getColor();
    }


    @Override
    public boolean[][] possibleMoves() {

        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        //@ assert (\forall int i, j;
        //@         0 <= i && i < mat.length &&
        //@         0 <= j && j < mat[i].length;
        //@         mat[i][j] == false);

        int[][] directions = {
                {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2},
                {1, 2}, {2, 1}, {2, -1}, {1, -2}
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

            p.setValues(position.getRow() + dx, position.getColumn() + dy);
            if (getBoard().positionExists(p)) {
                //@ assert getBoard().positionExists(p);
                mat[p.getRow()][p.getColumn()] = canMove(p);
            }
        }

        return mat;
    }


}

