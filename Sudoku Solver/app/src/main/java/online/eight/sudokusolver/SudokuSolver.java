package online.eight.sudokusolver;

import android.util.Log;

import java.util.ArrayList;

public class SudokuSolver {

    int[][] board;
    ArrayList<ArrayList<Object>> emptyBoxIndex;

    int selected_row;
    int selected_column;

    SudokuSolver() {
        selected_row = -1;
        selected_column = -1;
        board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = 0;
            }
        }
        emptyBoxIndex = new ArrayList<>();

    }


    public void getEmptyBoxIndexes() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (this.board[r][c] == 0) {
                    this.emptyBoxIndex.add(new ArrayList<>());
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size() - 1).add(r);
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size() - 1).add(c);

                }
            }
        }
    }

    void setNumberPos(int num) {
        this.board[this.selected_row - 1][this.selected_column - 1] = num;
    }

    public ArrayList<ArrayList<Object>> getEmptyBoxIndex() {
        return this.emptyBoxIndex;
    }


    public int[][] getBoard() {
        return this.board;
    }

    public int getSelectedRow() {
        return selected_row;
    }

    public int getSelectedColumn() {
        return selected_column;
    }

    public void setSelected_row(int selected_row) {
        this.selected_row = selected_row;
    }

    public void setSelected_column(int selected_column) {
        this.selected_column = selected_column;
    }


    private boolean check(int row, int col) {

        if (this.board[row][col] > 0) {
            for (int i = 0; i < 9; i++) {
                if (this.board[i][col] == this.board[row][col] && i != row) {
                    return false;
                }
                if (this.board[row][i] == this.board[row][col] && i != col) {
                    return false;
                }
            }
            int boxRow = row / 3;
            int boxCol = col / 3;

            for (int r = boxRow * 3; r < boxRow * 3 + 3; r++) {
                for (int c = boxCol * 3; c < boxCol * 3 + 3; c++) {
                    if (this.board[r][c] == this.board[row][col] && row != r && col != c) {
                        return false;
                    }
                }

            }

        }
        return true;

    }


    public boolean solve(SudokuBoard display) {

        int row = -1;
        int col = -1;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (Thread.currentThread().isInterrupted()) {
                    return false;
                }


                if (this.board[r][c] == 0) {
                    row = r;
                    col = c;
                    break;
                }
            }
        }
        if (row == -1 || col == -1) {
            return true;
        }
        for (int i = 1; i < 10; i++) {

            if (Thread.interrupted()) {
                return false;
            }

            this.board[row][col] = i;


            if (check(row, col)) {
                if (solve(display)) {
                    return true;
                }

            }
            this.board[row][col] = 0;
        }
        return false;
    }


    public boolean solve_delay(SudokuBoard display, int sleep) {

        int row = -1;
        int col = -1;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (Thread.currentThread().isInterrupted()) {
                    return false;
                }


                if (this.board[r][c] == 0) {
                    row = r;
                    col = c;
                    break;
                }
            }
        }
        if (row == -1 || col == -1) {
            return true;
        }
        for (int i = 1; i < 10; i++) {

            try {
                Thread.sleep(sleep);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (Thread.interrupted()) {
                return false;
            }


            this.board[row][col] = i;
            display.invalidate();

            if (check(row, col)) {

                if (solve_delay(display, sleep)) {

                    return true;
                }

            }
            this.board[row][col] = 0;
        }
        return false;
    }


    public void resetBoard() {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                this.board[r][c] = 0;
        this.emptyBoxIndex = new ArrayList<>();

    }


}
