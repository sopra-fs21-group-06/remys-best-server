package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.LinkedList;

public class PlayingBoard {
    private LinkedList<Field> listPlayingFields;

    public LinkedList<Field> getListPlayingFields() {
        return listPlayingFields;
    }

    public void setListPlayingFields(LinkedList<Field> listPlayingFields) {
        this.listPlayingFields = listPlayingFields;
    }
}
