package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.Application;
import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import org.springframework.boot.SpringApplication;

import java.util.LinkedList;
import java.util.Stack;

import static java.lang.Boolean.TRUE;


public class PlayingBoardService {
    private final PlayingBoard playingBoard;




    public PlayingBoardService(PlayingBoard playingBoard) {
        this.playingBoard = playingBoard;
        LinkedList<Field> listPlayingFields = new LinkedList<>();

        StartField startFieldBlue = new StartField(5);
        startFieldBlue.setColor(Color.blue);
        listPlayingFields.add(startFieldBlue);
        for (int i = 6; i <= 20; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
        }

        StartField startFieldGreen = new StartField(21);
        startFieldBlue.setColor(Color.green);
        listPlayingFields.add(startFieldGreen);
        for (int i = 22; i <= 36; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
        }

        StartField startFieldRed = new StartField(37);
        startFieldRed.setColor(Color.red);
        listPlayingFields.add(startFieldRed);
        for (int i = 38; i <= 52; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
        }

        StartField startFieldYellow = new StartField(53);
        startFieldYellow.setColor(Color.yellow);
        listPlayingFields.add(startFieldYellow);
        for (int i = 54; i <= 68; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
        }
        playingBoard.setListPlayingFields(listPlayingFields);
        for (Color c: Color.values()) {
            LinkedList<Field> finish = new LinkedList<>();
            for (int i = 0; i <= 4; i++) {
                FinishField finishField = new FinishField(i, c);
                finish.add(finishField);
            }
            if (c == Color.blue){
                finish.add(0,startFieldBlue);
                playingBoard.setBlueField(finish);
            }
            else if (c == Color.red){
                finish.add(0, startFieldRed);
                playingBoard.setRedFields(finish);
            }
            else if (c == Color.green){
                finish.add(0,startFieldGreen);
                playingBoard.setGreenFields(finish);
            }
            else if( c == Color.yellow){
                finish.add(0,startFieldYellow);
                playingBoard.setYellowFields(finish);
            }
        }
        Stack blueHome = new Stack();
        Stack greenHome = new Stack();
        Stack yellowHome = new Stack();
        Stack redHome = new Stack();
        for(int i = 0; i < 5; i++){
            GreenMarble greenMarble = new GreenMarble(i);
            greenMarble.setHome(TRUE);
            greenHome.push(greenMarble);
            RedMarble redMarble = new RedMarble(i);
            redMarble.setHome(TRUE);
            redHome.push(redMarble);
            BlueMarble blueMarble = new BlueMarble(i);
            blueMarble.setHome(TRUE);
            blueHome.push(blueMarble);
            YellowMarble yellowMarble = new YellowMarble(i);
            yellowMarble.setHome(TRUE);
            yellowHome.push(yellowMarble);
        }
        playingBoard.setBlueHome(blueHome);
        playingBoard.setGreenHome(greenHome);
        playingBoard.setRedHome(redHome);
        playingBoard.setYellowHome(yellowHome);

    }
    public void moveMarble(Field destinationLocation, Marble marble){
        marble.getCurrentField().setMarble(null);
        marble.getCurrentField().setFieldStatus(FieldStatus.free);
        marble.setCurrentField(destinationLocation);
        destinationLocation.setMarble(marble);
        destinationLocation.setFieldStatus(FieldStatus.occupied);

    }
};

