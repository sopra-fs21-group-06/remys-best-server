package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.LinkedList;

import static java.lang.Boolean.TRUE;

public class PlayingBoard {
    private LinkedList<Field> listPlayingFields;
    private Boolean allStartFieldsFree = TRUE;

    public LinkedList<Field> getListPlayingFields() {
        return listPlayingFields;
    }

    public void setListPlayingFields(LinkedList<Field> listPlayingFields) {
        this.listPlayingFields = listPlayingFields;
    }
}
