package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean isXTurn = true; // Flag indicating whether 'X' or 'O' move.
    private final Button[] Buttons = new Button[9]; // The 9 board buttons of Tic-Tac-Toe.
    private TextView tv; // The label that updates every game state.
    private Button[] winningButton = new Button[3]; // Stores winning buttons for it to be styled.

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView); // Initialize text view.

        updateTextView(); // Updates text views.

        // Initialize the buttons.
        for(int i = 0; i < 9; i++){
            Buttons[i] = findViewById(getResources().getIdentifier("button" + (i + 1), "id", getPackageName()));
        }

        // Initialize a resetButton and adds click listener.
        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetGame()); // Whenever reset button is pressed, it will call the resetGame() method.
    }


    // Handles button pressed made by the user.
    public void onCellClick(View v){

        for(int i = 0; i < 9; i++){
            if(v.getId() == Buttons[i].getId() && Buttons[i].getText().equals("") && getWinner() == null){
                makeMove(Buttons[i]);
                declareWinner(getWinner());
            }
        }
    }

    // Handles the player's move, either 'X' or 'O'.
    private void makeMove(Button btn){
        btn.setText(isXTurn ? "X" : "O");// Checks winner and declare it everytime a player made a move.
        btn.setEnabled(false); // Makes the button un-clickable, when the button is already clicked.
        isXTurn = !isXTurn; // Flag indicates which player has the next move.
        updateTextView(); //

    }

    // Declares which player won.
    private void declareWinner(Character winner){

        // Declares if it's a tie game.
        if(winner == null && !hasMoveLeft()){
            tv.setText("Its a tie.");
            updateButtonText(2); // Gives status 2 because it's a tie game.
        }

        // Declares if there is a winner.
        if(winner != null){
            tv.setText(winner + " wins!");
            updateButtonText(1); // Gives status 1 because some player wins.
            return;
        }
 // update the button once it's done.
    }

    // Checks the line if it has either straight 'X' or 'O'.
    private Character getWinner(){
        //  Returns winner's character, either 'X' or 'O', and If the board don't have winner returns null.
        for(int i = 0; i < 3; i++){
            // Checks horizontal lines.
            if(checkWinner(Buttons[i * 3], Buttons[i * 3 + 1], Buttons[i * 3 + 2])) {
                // Calls to set the buttons as the winning buttons.
                setWinningButtons(Buttons[i * 3], Buttons[i * 3 + 1], Buttons[i * 3 + 2]);
                return Buttons[i * 3].getText().charAt(0);
            }
            // Checks Vertical lines.
            if(checkWinner(Buttons[i], Buttons[i + 3], Buttons[i + 6])){
                setWinningButtons(Buttons[i], Buttons[i + 3], Buttons[i + 6]);
                return Buttons[i].getText().charAt(0);
            }
        }
        // Checks left diagonal line.
        if(checkWinner(Buttons[0], Buttons[4], Buttons[8])){
            setWinningButtons(Buttons[0], Buttons[4], Buttons[8]);
            return Buttons[0].getText().charAt(0);
        }
        // Checks right diagonal line.
        if(checkWinner(Buttons[2], Buttons[4], Buttons[6])){
            setWinningButtons(Buttons[2], Buttons[4], Buttons[6]);
            return Buttons[2].getText().charAt(0);
        }

        return null;

    }

    // Send the buttons that won in winningButton[] array.
    private void setWinningButtons(Button... btn){
        for(int i = 0; i < btn.length; i++){
            winningButton[i] = btn[i];
        }
    }

    // Checks three button text to check if someone won a line.
    private boolean checkWinner(Button... btn){
        String previousButton = btn[0].getText().equals("") ? "random" : btn[0].getText().toString();

        // Compare buttons to previous buttons to see if they are identical in their text.
        for(int i = 1; i < 3; i++){
            if(!previousButton.equals(btn[i].getText().toString())){
                return false; // If the previous button doesn't match the current button, it will return false.
            }
            previousButton = btn[i].getText().toString(); // Stores current button to the previous button, for it to be compare to the next button.
        }

        return true; // If the iteration cannot find any un-identical button.
    }

    // Updates buttons text colors depends on game status
    private void updateButtonText(int status){
        // if Status value is 1 = some player win if 2 = tie game.
        // Sets the button text color, if the game is tied.
        if(status == 2){
            for(Button button : Buttons){
                button.setTextColor(Color.GREEN);
            }

            return;
        }

        // Sets the button text color. 'X' = red 'O' = blue.
        if (status == 1) {
            for(Button button : winningButton){
                button.setTextColor(winningButton[0].getText().equals("X") ? Color.RED : Color.BLUE);
            }
        }

    }

    // Method that check if there is a move left on the board.
    private boolean hasMoveLeft(){
        // Iterate through all buttons text and check if there is empty button.
        for(int i = 0; i < Buttons.length; i++){
            if(Buttons[i].getText().equals("")){
                return true;
            }
        }

        return false; // Returns false when it cannot find any empty button.
    }


    // Resets the game board.
    private void resetGame(){
        for(int i = 0; i < Buttons.length; i++) {
            Buttons[i].setText(""); // Resets buttons text.
            Buttons[i].setEnabled(true); // Re-enables all the buttons.
            Buttons[i].setTextColor(Color.WHITE);
        }
        isXTurn = true; // Makes the default first turn is player 'X'.
        updateTextView(); // Updates the text view.
    }

    // Sets text view text.
    private void updateTextView(){
        tv.setText(isXTurn ? "'X' move." : "'O' move."); // Sets the text depends on which player is turn.
    }
}