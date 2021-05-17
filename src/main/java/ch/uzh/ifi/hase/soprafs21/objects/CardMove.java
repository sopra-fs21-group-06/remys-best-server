package ch.uzh.ifi.hase.soprafs21.objects;

public class CardMove {
    private String moveName;

    public CardMove(String moveName) {
        this.moveName = moveName;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }
}
