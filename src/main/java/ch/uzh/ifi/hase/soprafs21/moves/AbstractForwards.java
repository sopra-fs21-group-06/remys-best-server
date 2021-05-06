package ch.uzh.ifi.hase.soprafs21.moves;

public abstract class AbstractForwards implements IMove {

    public String getName(String number) {
        return number + " Forwards";
    }
}
