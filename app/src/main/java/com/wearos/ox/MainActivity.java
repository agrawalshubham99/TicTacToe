package com.wearos.ox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    // User/game selections
    private boolean isTwoPlayer = false;  // false => Single Player, true => Two Player
    private String firstMover = "P1";     // "P1", "P2" or "CPU"
    private String p1Name, p2Name;        // p1Name = user in single player, p2Name = CPU or second player
    private String p1Symbol, p2Symbol;    // "X" or "O"

    // Board & UI
    private String[] board;      // 9 slots, each "", "X", or "O"
    private Button[] buttons;    // references to the 9 board buttons
    private String currentTurn;  // "P1" or "P2"/"CPU"

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showInputScreen();
    }

    /**
     * Show the Input Screen (choose Single/Two Player, names, symbols, who goes first).
     */
    private void showInputScreen() {
        setContentView(R.layout.input_screen);

        // RadioGroup for game mode
        RadioGroup rgMode = findViewById(R.id.rg_mode);
        RadioButton rbSingle = findViewById(R.id.rb_single);
        RadioButton rbTwo = findViewById(R.id.rb_two);

        // Single player layout
        LinearLayout singleLayout = findViewById(R.id.single_player_layout);
        EditText etSingleName = findViewById(R.id.et_single_name);
        RadioGroup rgSingleSymbol = findViewById(R.id.rg_single_symbol); // choose X or O
        RadioGroup rgSingleFirst = findViewById(R.id.rg_single_first);   // who goes first: me or cpu

        // Two player layout
        LinearLayout twoLayout = findViewById(R.id.two_player_layout);
        EditText etP1Name = findViewById(R.id.et_p1_name);
        EditText etP2Name = findViewById(R.id.et_p2_name);
        RadioGroup rgP1Symbol = findViewById(R.id.rg_p1_symbol);
        RadioGroup rgTwoFirst = findViewById(R.id.rg_two_first);

        // Start button
        Button btnStart = findViewById(R.id.btn_start_game);

        // By default, show single player layout
        rbSingle.setChecked(true);
        singleLayout.setVisibility(View.VISIBLE);
        twoLayout.setVisibility(View.GONE);

        // Toggle layouts based on game mode selection
        rgMode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_single) {
                isTwoPlayer = false;
                singleLayout.setVisibility(View.VISIBLE);
                twoLayout.setVisibility(View.GONE);
            } else {
                isTwoPlayer = true;
                singleLayout.setVisibility(View.GONE);
                twoLayout.setVisibility(View.VISIBLE);
            }
        });

        // Start button logic
        btnStart.setOnClickListener(v -> {
            if (!isTwoPlayer) {
                // SINGLE PLAYER
                p1Name = etSingleName.getText().toString().trim();
                if (p1Name.isEmpty()) {
                    Toast.makeText(this, "Enter your name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Symbol choice
                int symbolChecked = rgSingleSymbol.getCheckedRadioButtonId();
                p1Symbol = (symbolChecked == R.id.rb_single_x) ? "X" : "O";
                p2Symbol = p1Symbol.equals("X") ? "O" : "X";

                // Who goes first?
                int firstChecked = rgSingleFirst.getCheckedRadioButtonId();
                if (firstChecked == R.id.rb_first_me) {
                    firstMover = "P1";  // player goes first
                } else {
                    firstMover = "CPU"; // CPU goes first
                }
                p2Name = "CPU";

            } else {
                // TWO PLAYER
                p1Name = etP1Name.getText().toString().trim();
                p2Name = etP2Name.getText().toString().trim();
                if (p1Name.isEmpty() || p2Name.isEmpty()) {
                    Toast.makeText(this, "Enter both names!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Player1 symbol
                int p1SymbolChecked = rgP1Symbol.getCheckedRadioButtonId();
                p1Symbol = (p1SymbolChecked == R.id.rb_p1_x) ? "X" : "O";
                p2Symbol = p1Symbol.equals("X") ? "O" : "X";

                // Who goes first?
                int twoFirstChecked = rgTwoFirst.getCheckedRadioButtonId();
                if (twoFirstChecked == R.id.rb_first_p1) {
                    firstMover = "P1";
                } else {
                    firstMover = "P2";
                }
            }

            showGameScreen();
        });
    }

    /**
     * Show the Game Screen with a 3×3 grid in the center.
     */
    private void showGameScreen() {
        setContentView(R.layout.game_screen);

        // Initialize empty board
        board = new String[]{"", "", "", "", "", "", "", "", ""};
        buttons = new Button[9];

        // Connect each of the 9 buttons
        for (int i = 0; i < 9; i++) {
            int resID = getResources().getIdentifier("btn_" + i, "id", getPackageName());
            buttons[i] = findViewById(resID);
            final int idx = i;
            buttons[i].setOnClickListener(v -> handleMove(idx));
        }

        // Set current turn to firstMover
        currentTurn = firstMover;

        // If single-player and CPU goes first, make the CPU move immediately
        if (!isTwoPlayer && currentTurn.equals("CPU")) {
            cpuMove();
        }
    }

    /**
     * Called when user taps a cell at index idx.
     */
    private void handleMove(int idx) {
        // If the cell is already filled, ignore
        if (!board[idx].isEmpty()) return;

        if (!isTwoPlayer) {
            // SINGLE PLAYER
            if (currentTurn.equals("P1")) {
                // Player's move
                board[idx] = p1Symbol;
                buttons[idx].setText(p1Symbol);
                // Check
                if (checkWin(board, p1Symbol)) {
                    showResult("Congrats, " + p1Name + "!");
                } else if (isDraw(board)) {
                    showResult("It's a draw, " + p1Name + ".");
                } else {
                    // Switch turn to CPU
                    currentTurn = "CPU";
                    cpuMove();
                }
            }
            // If it's CPU's turn, do nothing on user tap
        } else {
            // TWO PLAYER
            if (currentTurn.equals("P1")) {
                board[idx] = p1Symbol;
                buttons[idx].setText(p1Symbol);
                // Check
                if (checkWin(board, p1Symbol)) {
                    showResult("Congrats, " + p1Name + "!");
                } else if (isDraw(board)) {
                    showResult("It's a draw!");
                } else {
                    // Switch turn
                    currentTurn = "P2";
                }
            } else {
                board[idx] = p2Symbol;
                buttons[idx].setText(p2Symbol);
                // Check
                if (checkWin(board, p2Symbol)) {
                    showResult("Congrats, " + p2Name + "!");
                } else if (isDraw(board)) {
                    showResult("It's a draw!");
                } else {
                    currentTurn = "P1";
                }
            }
        }
    }

    /**
     * CPU move using minimax. CPU is always 'p2Symbol' in single-player.
     */
    private void cpuMove() {
        int bestIndex = findBestMove(board, p2Symbol, p1Symbol);
        if (bestIndex != -1) {
            board[bestIndex] = p2Symbol;
            buttons[bestIndex].setText(p2Symbol);

            if (checkWin(board, p2Symbol)) {
                showResult("Uh oh, " + p1Name + ", you lose.");
                return;
            }
            if (isDraw(board)) {
                showResult("It's a draw, " + p1Name + ".");
                return;
            }
        }
        // Switch turn back to player
        currentTurn = "P1";
    }

    /**
     * Minimax-based search for best CPU move.
     */
    private int findBestMove(String[] boardState, String ai, String human) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        for (int i = 0; i < 9; i++) {
            if (boardState[i].isEmpty()) {
                boardState[i] = ai;
                int score = minimax(boardState, 0, false, ai, human);
                boardState[i] = "";
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    /**
     * Minimax algorithm:
     * +10 if AI wins
     * -10 if human wins
     *  0 if draw
     */
    private int minimax(String[] boardState, int depth, boolean isMaximizing, String ai, String human) {
        // Terminal checks
        if (checkWin(boardState, ai)) {
            return 10 - depth;
        }
        if (checkWin(boardState, human)) {
            return depth - 10;
        }
        if (isDraw(boardState)) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (boardState[i].isEmpty()) {
                    boardState[i] = ai;
                    int score = minimax(boardState, depth + 1, false, ai, human);
                    boardState[i] = "";
                    bestScore = Math.max(bestScore, score);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (boardState[i].isEmpty()) {
                    boardState[i] = human;
                    int score = minimax(boardState, depth + 1, true, ai, human);
                    boardState[i] = "";
                    bestScore = Math.min(bestScore, score);
                }
            }
            return bestScore;
        }
    }

    /**
     * Check if symbol s has won on the given board.
     */
    private boolean checkWin(String[] b, String s) {
        int[][] wins = {
                {0,1,2}, {3,4,5}, {6,7,8}, // rows
                {0,3,6}, {1,4,7}, {2,5,8}, // cols
                {0,4,8}, {2,4,6}           // diagonals
        };
        for (int[] w : wins) {
            if (b[w[0]].equals(s) && b[w[1]].equals(s) && b[w[2]].equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the board is full (no empty cell => draw).
     */
    private boolean isDraw(String[] b) {
        for (String cell : b) {
            if (cell.isEmpty()) return false;
        }
        return true;
    }

    /**
     * Show result screen with "Play Again" or "Change Player" options.
     */
    private void showResult(String msg) {
        setContentView(R.layout.result_screen);

        TextView tvResult = findViewById(R.id.result_text);
        Button btnAgain = findViewById(R.id.play_again);
        Button btnChange = findViewById(R.id.change_player);

        tvResult.setText(msg);

        btnAgain.setOnClickListener(v -> showGameScreen());
        btnChange.setOnClickListener(v -> showInputScreen());
    }
}
