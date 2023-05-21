package online.eight.sudokusolver;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Time;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class
MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SudokuBoard gameBoard;
    private SudokuSolver gameBoardSolver;
    private Thread solverThread;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameBoard = findViewById(R.id.sudokuBoard);
        gameBoardSolver = gameBoard.getSolver();


    }


    private String readFileContent(InputStream inputStream) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString();
    }


    private final ActivityResultLauncher<String[]> mReadFileLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(), result -> {
                if (result == null) {
                    return;
                }

                try {
                    InputStream inputStream = getContentResolver().openInputStream(result);
                    String fileContent = readFileContent(inputStream);
                    setBoardValues(fileContent);
                } catch (IOException e) {
                    Log.e(TAG, "Error reading file: " + e.getMessage());
                }
            });


    private void setBoardValues(String s) {
        int row = 0;
        int column = 0;
        for (int i = 0; i < s.length(); i++) {
            Character ch = s.charAt(i);
            if (ch.equals(',')) {
                continue;
            }
            if (ch.equals('*')) {
                gameBoardSolver.board[row][column] = 0;
                column += 1;
                continue;
            }

            if (ch.equals('\n')) {
                row += 1;
                column = 0;
                continue;
            }

            gameBoardSolver.board[row][column] = Character.getNumericValue(ch);
            column += 1;

//            gameBoardSolver.setSelected_row(row+1);
//            gameBoardSolver.setSelected_column(column+1);
            Log.i(TAG, "setBoardValues: " + Character.getNumericValue(ch));


        }

        gameBoard.invalidate();

    }


    public void load_click(View view) {
        mReadFileLauncher.launch(new String[]{"text/plain"});

    }


    public void solve_click(View view) {
        solve_timer();
    }


    public void backtrack_click(View view) {
        gameBoardSolver.getEmptyBoxIndexes();
        SolveBoardThread solveBoardThread = new SolveBoardThread();
        solverThread = new Thread(solveBoardThread);
        solverThread.start();
        gameBoard.invalidate();
    }


    public void solve_timer() {
        int[][] baseBoard = new int[gameBoardSolver.getBoard().length][];

        for (int i = 0; i < gameBoardSolver.getBoard().length; i++) {
            baseBoard[i] = Arrays.copyOf(gameBoardSolver.getBoard()[i], gameBoardSolver.getBoard()[i].length);
        }
        gameBoardSolver.getEmptyBoxIndexes();
        gameBoardSolver.solve(gameBoard);
        int[][] solvedBoard = gameBoardSolver.getBoard().clone();
        gameBoardSolver.board = baseBoard.clone();
        gameBoard.invalidate();

        timer = new Timer();
        final int[] r = {8};
        final int[] c = {8};

        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                if (gameBoardSolver.board[r[0]][c[0]] == 0) {
                    gameBoardSolver.board[r[0]][c[0]] = solvedBoard[r[0]][c[0]];
                } else {
                    if (c[0] == 0 && r[0] == 0) {
                        stopTimer();
                    }
                    Log.i(TAG, "run: done");
                    if (c[0] > -1) {
                        c[0]--;
                    }
                    if (c[0] == -1) {
                        c[0] = 8;
                        r[0]--;
                    }
                }
            }

            public void stopTimer() {
                timer.cancel();
            }
        };

        timer.schedule(updateTask, 0, 300);
    }


    class SolveBoardThread implements Runnable {
        @Override
        public void run() {
            gameBoardSolver.solve_delay(gameBoard, 50);

        }
    }

    public void stop_click(View view) {
        if (solverThread != null)
            solverThread.interrupt();
        if (timer != null)
            timer.cancel();
        gameBoardSolver.resetBoard();
        gameBoard.invalidate();
    }


}