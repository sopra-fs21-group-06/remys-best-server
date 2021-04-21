package ch.uzh.ifi.hase.soprafs21.objects;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "code",
        "image",
        "images",
        "value",
        "suit"
})
@Generated("jsonschema2pojo")
public class CardAPICardJson {

    @JsonProperty("code")
    private String code;
    @JsonProperty("image")
    private String image;
    @JsonProperty("images")
    private CardAPIImages images;
    @JsonProperty("value")
    private String value;
    @JsonProperty("suit")
    private String suit;

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("images")
    public CardAPIImages getImages() {
        return images;
    }

    @JsonProperty("images")
    public void setImages(CardAPIImages images) {
        this.images = images;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("suit")
    public String getSuit() {
        return suit;
    }

    @JsonProperty("suit")
    public void setSuit(String suit) {
        this.suit = suit;
    }

}