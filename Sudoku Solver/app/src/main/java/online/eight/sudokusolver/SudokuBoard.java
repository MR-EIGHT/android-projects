package online.eight.sudokusolver;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SudokuBoard extends View {
    private final int boardColor;
    private final int cellFillColor;
    private final int cellHighlightColor;
    private final int letterColor;
    private final int letterColorSolve;
    private final Paint boardColorPaint = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellHighlightColorPaint = new Paint();
    private final Paint letterPaint = new Paint();
    private final Rect letterPaintBounds = new Rect();
    private int cellSize;
    public final SudokuSolver solver = new SudokuSolver();

    private void colorCell(Canvas canvas, int row, int column) {
        canvas.drawRect((column - 1) * cellSize, 0, column * cellSize, cellSize * 9, cellHighlightColorPaint);
        canvas.drawRect(0, (row - 1) * cellSize, cellSize * 9, row * cellSize, cellHighlightColorPaint);
        canvas.drawRect((column - 1) * cellSize, (row - 1) * cellSize, column * cellSize, row * cellSize, cellHighlightColorPaint);

        invalidate();
    }


    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
//        solver.getSelectedColumn();
//        solver.setSelected_row();

        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SudokuBoard, 0, 0);
        try {
            boardColor = a.getInteger(R.styleable.SudokuBoard_boardColor, 0);
            cellFillColor = a.getInteger(R.styleable.SudokuBoard_cellFillColor, 0);
            cellHighlightColor = a.getInteger(R.styleable.SudokuBoard_cellHighlightColor, 0);

            letterColor = a.getInteger(R.styleable.SudokuBoard_letterColor, 0);
            letterColorSolve = a.getInteger(R.styleable.SudokuBoard_letterColorSolve, 0);


        } finally {
            a.recycle();
        }

    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);

        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
        cellSize = dimension / 9;
        setMeasuredDimension(dimension, dimension);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(16);
        boardColorPaint.setColor(boardColor);
        boardColorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL);
        cellFillColorPaint.setAntiAlias(true);
        cellFillColorPaint.setColor(cellFillColor);


        cellHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellHighlightColorPaint.setAntiAlias(true);
        cellHighlightColorPaint.setColor(cellHighlightColor);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(letterColor);


        colorCell(canvas, solver.getSelectedRow(), solver.getSelectedColumn());
        canvas.drawRect(0, 0, getWidth(), getHeight(), boardColorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);
    }

    private void drawThickLine() {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(10);
        boardColorPaint.setColor(boardColor);
    }


    private void drawThinLine() {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(4);
        boardColorPaint.setColor(boardColor);
    }

    private void drawBoard(Canvas canvas) {
        for (int c = 0; c < 10; c++) {
            if (c % 3 == 0) {
                drawThickLine();
            } else {
                drawThinLine();
            }
            canvas.drawLine(cellSize * c, 0, cellSize * c, getWidth(), boardColorPaint);
        }


        for (int r = 0; r < 10; r++) {
            if (r % 3 == 0) {
                drawThickLine();
            } else {
                drawThinLine();
            }
            canvas.drawLine(0, cellSize * r, getWidth(), cellSize * r, boardColorPaint);

        }


    }


    private void drawNumbers(Canvas canvas) {
        letterPaint.setTextSize(cellSize);
        letterPaint.setColor(letterColor);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (solver.getBoard()[r][c] != 0) {
                    String text = Integer.toString(solver.getBoard()[r][c]);
                    float width, height;
                    letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
                    width = letterPaint.measureText(text);
                    height = letterPaintBounds.height();
                    canvas.drawText(text, (c * cellSize) + (cellSize - width) / 2, (r * cellSize + cellSize) - (cellSize - height) / 2, letterPaint);

                }
            }
        }
        letterPaint.setColor(letterColorSolve);

        for (ArrayList<Object> letter : solver.getEmptyBoxIndex()) {
            int r = (int) letter.get(0);
            int c = (int) letter.get(1);
            if (solver.getBoard()[r][c] == 0)
                continue;
            String text = Integer.toString(solver.getBoard()[r][c]);
            float width, height;
            letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
            width = letterPaint.measureText(text);
            height = letterPaintBounds.height();
            canvas.drawText(text, (c * cellSize) + (cellSize - width) / 2, (r * cellSize + cellSize) - (cellSize - height) / 2, letterPaint);


        }
    }

    public SudokuSolver getSolver() {
        return this.solver;
    }

}
