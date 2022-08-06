package com.example.scoreboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;


public class MainActivity extends AppCompatActivity {

    private TextView fieldPlayersText;
    private TextView benchPlayersText;
    private TextView scoreTextView;
    private TextView gameTimeTitle;
    private Button buttonTeam1;
    private Button buttonTeam2;
    private Button startStop;


    private Menu menu;


    private Chronometer chronometer;
    private Chronometer gameTime;
    private boolean cronRunning = false;
    private boolean gameTimeRunning = false;
    private long pauseOffsetCron;
    private long pauseOffsetGameTime;
    private boolean beginningOfGame = true;

    private boolean[] layout = new boolean[6];

    private String sTeam1Name;
    private String sTeam2Name;

    private Button team1color;


    GameState game = new GameState(8,5,1);

    private void updateScreen(TextView fieldPlayersText, TextView benchPlayersText, TextView scoreTextView, GameState game){
        if(game.getCurrentGender()){
            fieldPlayersText.setBackgroundColor(getResources().getColor(R.color.blue));
        }else{
            fieldPlayersText.setBackgroundColor(getResources().getColor(R.color.pink));
        }
        fieldPlayersText.setText(game.getPlayersOnField());
        benchPlayersText.setText(game.getPlayersOnBench());
        scoreTextView.setText(game.getScore());
    }

    private void updateMenu(GameState game, Menu menu){

        menu.findItem(R.id.playersInTeam).setTitle(getResources().getString(R.string.plrsInTeam) +
                " (" + game.playersNumber + ")");
        menu.findItem(R.id.playersOnField).setTitle(getResources().getString(R.string.plrsOnFeild) +
                " (" + game.nPlayersOnField + ")");
        menu.findItem(R.id.startNumber).setTitle(getResources().getString(R.string.startNumber) +
                " (" + game.startNumberField + ")");

    }

    private void choosePlrsOnField(TextView fieldPlayersText, TextView benchPlayersText,
                                   TextView scoreTextView, GameState game, Menu menu, int i, String id) {

        if (game.playersNumber >= i) {
            game.nPlayersOnField = i;
            updateScreen(fieldPlayersText, benchPlayersText, scoreTextView, game);
            updateMenu(game, menu);
            menu.findItem(getResources().getIdentifier(id,"id", this.getPackageName())).setChecked(true);
        } else
            Toast.makeText(this, "Players in the team is not enought", Toast.LENGTH_SHORT).show();
    }

    private void choosePlrsAmount(TextView fieldPlayersText, TextView benchPlayersText,
                                  TextView scoreTextView, GameState game, Menu menu, int i, String id) {

        if (game.nPlayersOnField <= i) {
            game.playersNumber = i;
            updateScreen(fieldPlayersText, benchPlayersText, scoreTextView, game);
            updateMenu(game, menu);
            menu.findItem(getResources().getIdentifier(id,"id", this.getPackageName())).setChecked(true);
        } else
            Toast.makeText(this, "Players in the team is not enought", Toast.LENGTH_SHORT).show();
    }

    private void chooseFirstPlayerOnF(TextView fieldPlayersText, TextView benchPlayersText,
                                      TextView scoreTextView, GameState game, Menu menu, int i, String id) {

        if (game.playersNumber >= i) {
            game.startNumberField = i;
            updateScreen(fieldPlayersText, benchPlayersText, scoreTextView, game);
            updateMenu(game, menu);
            menu.findItem(getResources().getIdentifier(id,"id", this.getPackageName())).setChecked(true);
        } else
            Toast.makeText(this, "Number is too big", Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonTeam1 = findViewById(R.id.buttonTeam1);
        buttonTeam2 = findViewById(R.id.buttonTeam2);
        scoreTextView = findViewById(R.id.scoreTextView);
        fieldPlayersText = findViewById(R.id.fieldPlayersText);
        benchPlayersText = findViewById(R.id.benchPlayersText);
        gameTimeTitle = findViewById(R.id.gameTimeTitle);
        chronometer = findViewById(R.id.chronometer);
        gameTime = findViewById(R.id.gameTime);
        startStop = findViewById(R.id.startStop);

        for(int i=0; i<6; i++){layout[i]=true;}

        updateScreen(fieldPlayersText, benchPlayersText, scoreTextView, game);

        buttonTeam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.changeState(1,0);
                updateScreen(fieldPlayersText, benchPlayersText, scoreTextView, game);
                updateMenu(game, menu);

                if(!cronRunning){
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    cronRunning = true;
                }
                else {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                }
                if(beginningOfGame){
                    gameTime.setBase(SystemClock.elapsedRealtime());
                    gameTime.start();
                    gameTimeRunning = true;
                    beginningOfGame = false;
                    startStop.setText("Stop");
                }

            }
        });

        buttonTeam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.changeState(0,1);
                updateScreen(fieldPlayersText, benchPlayersText, scoreTextView, game);
                updateMenu(game, menu);

                if(!cronRunning){
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    cronRunning = true;
                }
                else {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                }
            }
        });

        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(beginningOfGame){
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    cronRunning = true;
                    gameTime.setBase(SystemClock.elapsedRealtime());
                    gameTime.start();
                    gameTimeRunning = true;
                    beginningOfGame = false;
                    startStop.setText("Stop");
                }
                else{
                    if(!gameTimeRunning){
                        gameTime.start();
                        gameTimeRunning = true;
                        startStop.setText("Start");
                    }
                    else{
                        gameTime.stop();
                        gameTimeRunning = false;
                        startStop.setText("Stop");
                    }
                    if(cronRunning){
                        chronometer.stop();
                        cronRunning = false;
                    }
                }


            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        this.menu = menu;
        menu.findItem(R.id.flipGender).setTitle(getResources().getString(R.string.startingGender) + " (M)");
        updateScreen(fieldPlayersText, benchPlayersText, scoreTextView, game);
        updateMenu(game, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
//            case R.id.testButton:
//                Toast.makeText(this, "nPlayersOnField = " + game.nPlayersOnField.toString() +
//                        "playersNumber" + game.playersNumber, Toast.LENGTH_LONG).show();
            case R.id.restartGame:
                game.scoreTeam1 = 0;
                game.scoreTeam2 = 0;
                scoreTextView.setText(game.getScore());
                chronometer.setBase(SystemClock.elapsedRealtime());
                gameTime.setBase(SystemClock.elapsedRealtime());
                updateScreen(fieldPlayersText, benchPlayersText, scoreTextView, game);
                updateMenu(game, menu);
                return true;

            case R.id.teams:
                openTeamsDialog();
            case R.id.startNumber:
                updateMenu(game, menu);
                return true;
            case R.id.plrsF3:
                choosePlrsOnField(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 3, "plrsF3");
                return true;
            case R.id.plrsF4:
                choosePlrsOnField(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 4, "plrsF4");
                return true;
            case R.id.plrsF5:
                choosePlrsOnField(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 5, "plrsF5");
                return true;
            case R.id.plrsF6:
                choosePlrsOnField(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 6, "plrsF6");
                return true;
            case R.id.plrsF7:
                choosePlrsOnField(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 7, "plrsF7");
                return true;
            case R.id.plrsF8:
                choosePlrsOnField(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 8, "plrsF8");
                return true;
            case R.id.plrsF9:
                choosePlrsOnField(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 9, "plrsF9");
                return true;
            case R.id.plrsF10:
                choosePlrsOnField(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 10, "plrsF10");
                return true;
            case R.id.plrsF11:
                choosePlrsOnField(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 11, "plrsF11");
                return true;
            case R.id.plrsF12:
                choosePlrsOnField(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 12, "plrsF12");
                return true;


            case R.id.plrsTeam3:
                choosePlrsAmount(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 3, "plrsTeam3");
                return true;
            case R.id.plrsTeam4:
                choosePlrsAmount(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 4, "plrsTeam4");
                return true;
            case R.id.plrsTeam5:
                choosePlrsAmount(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 5, "plrsTeam5");
                return true;
            case R.id.plrsTeam6:
                choosePlrsAmount(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 6, "plrsTeam6");
                return true;
            case R.id.plrsTeam7:
                choosePlrsAmount(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 7, "plrsTeam7");
                return true;
            case R.id.plrsTeam8:
                choosePlrsAmount(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 8, "plrsTeam8");
                return true;
            case R.id.plrsTeam9:
                choosePlrsAmount(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 9, "plrsTeam9");
                return true;
            case R.id.plrsTeam10:
                choosePlrsAmount(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 10, "plrsTeam10");
                return true;
            case R.id.plrsTeam11:
                choosePlrsAmount(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 11, "plrsTeam11");
                return true;
            case R.id.plrsTeam12:
                choosePlrsAmount(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 12, "plrsTeam12");
                return true;



            case R.id.plrsStrt1:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 1, "plrsStrt1");
                return true;
            case R.id.plrsStrt2:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 2, "plrsStrt2");
                return true;
            case R.id.plrsStrt3:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 3, "plrsStrt3");
                return true;
            case R.id.plrsStrt4:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 4, "plrsStrt4");
                return true;
            case R.id.plrsStrt5:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 5, "plrsStrt5");
                return true;
            case R.id.plrsStrt6:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 6, "plrsStrt6");
                return true;
            case R.id.plrsStrt7:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 7, "plrsStrt7");
                return true;
            case R.id.plrsStrt8:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 8, "plrsStrt8");
                return true;
            case R.id.plrsStrt9:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 9, "plrsStrt9");
                return true;
            case R.id.plrsStrt10:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 10, "plrsStrt10");
                return true;
            case R.id.plrsStrt11:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 11, "plrsStrt11");
                return true;
            case R.id.plrsStrt12:
                chooseFirstPlayerOnF(fieldPlayersText, benchPlayersText, scoreTextView, game, menu, 12, "plrsStrt12");
                return true;

            case R.id.flipGender:
                game.initialGender = !game.initialGender;
                updateScreen(fieldPlayersText, benchPlayersText, scoreTextView, game);
                menu.findItem(R.id.flipGender).setTitle(getResources().getString(R.string.startingGender) + " (" + game.getCurrentGenderLetter() + ")");
                return true;

            case R.id.layoutGameTimer:
                layout[5] = !layout[5];
                menu.findItem(R.id.layoutGameTimer).setChecked(layout[5]);
                gameTime.setVisibility(layout[5] ? View.VISIBLE : View.INVISIBLE);
                gameTimeTitle.setVisibility(layout[5] ? View.VISIBLE : View.INVISIBLE);

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void openTeamsDialog(){
        Dialog teamsDialog = new Dialog(this);
        teamsDialog.setContentView(R.layout.teams_dialog_layout);

        EditText team1Name = teamsDialog.findViewById(R.id.team1Name);
        EditText team2Name = teamsDialog.findViewById(R.id.team2Name);
        team1color = teamsDialog.findViewById(R.id.team1color);
        Button okTeamsButton = teamsDialog.findViewById(R.id.okTeamsButton);

        team1color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseColorDialog();

            }
        });


        okTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTeam1Name = team1Name.getText().toString();
                sTeam2Name = team2Name.getText().toString();
                if(!sTeam1Name.equals("")) {
                    buttonTeam1.setText(sTeam1Name);
                }
                if(!sTeam2Name.equals("")) {
                    buttonTeam2.setText(sTeam2Name);
                }
                teamsDialog.dismiss();
            }
        });

        teamsDialog.show();

    }


    public void chooseColorDialog (){

        Dialog colorDialog = new Dialog(this);
        colorDialog.setContentView(R.layout.color_picker);

        ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
//        SVBar svBar = (SVBar) findViewById(R.id.svbar);
//        OpacityBar opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
//        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
//        ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);

//        picker.addSVBar(svBar);
//        picker.addOpacityBar(opacityBar);
//        picker.addSaturationBar(saturationBar);
//        picker.addValueBar(valueBar);

//To get the color
        picker.getColor();

//To set the old selected color u can do it like this
        picker.setOldCenterColor(picker.getColor());

////to turn of showing the old color
//        picker.setShowOldCenterColor(false);

//// adds listener to the colorpicker which is implemented
////in the activity
//        picker.setOnColorChangedListener(this);

////adding onChangeListeners to bars
//        opacitybar.setOnOpacityChangeListener(new OnOpacityChangeListener …)
//        valuebar.setOnValueChangeListener(new OnValueChangeListener …)
//        saturationBar.setOnSaturationChangeListener(new OnSaturationChangeListener …)
    }

}

