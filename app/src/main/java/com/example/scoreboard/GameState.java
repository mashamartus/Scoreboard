package com.example.scoreboard;

import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringJoiner;

public class GameState {
    public Integer scoreTeam1 = 0;
    public Integer scoreTeam2 = 0;
    public Integer playersNumber;
    public Integer startNumberField;
    public Integer nPlayersOnField;
    public int pointNumber = 0;
    public boolean initialGender = true; //true for man, false for woman

    public ArrayList<String> players = new ArrayList<>();


    public GameState (int plrsInTeam, int plrsOnField, int strtNumOnField) {
        playersNumber = plrsInTeam;
        nPlayersOnField = plrsOnField;
        startNumberField = strtNumOnField;
    }

    public String getPlayersOnField () {
        int[] playersOnField = new int[nPlayersOnField];
        for(int i=0; i<nPlayersOnField; i++){
            playersOnField[i] = (startNumberField - 1 + i ) % (playersNumber) + 1;
        }
        String str = "";
        for(int i=0;i<nPlayersOnField;i++)
        {
            str = str + Integer.toString(playersOnField[i]) + " ";
        }
        return str;
    }

    public String getPlayersOnBench () {
        int nPlayersOnBench = playersNumber - nPlayersOnField;
        int firstPlayerOnBench = (startNumberField - 1 + nPlayersOnField ) % (playersNumber) + 1;
        int[] playersOnBench = new int[nPlayersOnBench];
        for(int i=0; i<nPlayersOnBench; i++){
            playersOnBench[i] = (firstPlayerOnBench - 1 + i ) % (playersNumber) + 1;
        }
        String str = "";
        for(int i=0;i<playersOnBench.length;i++)
        {
            str = str + Integer.toString(playersOnBench[i]) + " ";
        }
        return str;
    }

    public String getScore (){
        return String.valueOf(scoreTeam1) + " - " + String.valueOf(scoreTeam2);
    }

    public void setInitialGender (boolean gender){
        initialGender = gender;
    }

    public boolean getCurrentGender(){
        int a;
        a = (scoreTeam1+scoreTeam2)%4;
        if(a==1 || a==2) {
            return !initialGender;
        }
        else
            return initialGender;
    }

    public String getCurrentGenderLetter(){
        if(getCurrentGender())
            return "M";
        else
            return "F";
    }

    public void changeState (int p1, int p2) /*throws Exception*/ {
        //if(p1!=0 || p2!=0){
        //    throw new Exception("Both team change score at the same time!");
        //}
        //if(p1==0 && p2==0){
        //    throw new Exception("None of teams score at changeState!");
        //}

        if (p1>0){scoreTeam1 = scoreTeam1 + 1;}
        pointNumber = pointNumber + 1;
        if (p1<0){scoreTeam1 = scoreTeam1 - 1;}
        pointNumber = pointNumber + 1;
        if (p2>0){scoreTeam2 = scoreTeam2 + 1;}
        pointNumber = pointNumber - 1;
        if (p2<0){scoreTeam2 = scoreTeam2 - 1;}
        pointNumber = pointNumber - 1;

        startNumberField = (startNumberField - 1 + nPlayersOnField) % playersNumber + 1;

    }



}
