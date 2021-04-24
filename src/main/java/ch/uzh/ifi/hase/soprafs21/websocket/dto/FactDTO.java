package ch.uzh.ifi.hase.soprafs21.websocket.dto;

public class FactDTO {
    private String title;
    private String subTitle;

    public FactDTO(String title, String subTitle){
        this.title = title;
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
