package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "success",
        "deck_id",
        "cards",
        "remaining",
        "error"
})
@Generated("jsonschema2pojo")
public class CardAPICardResponseObject {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("deck_id")
    private String deckId;
    @JsonProperty("cards")
    private List<CardAPICardJson> cards = null;
    @JsonProperty("remaining")
    private Integer remaining;
    @JsonProperty("error")
    private String error;

    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @JsonProperty("deck_id")
    public String getDeckId() {
        return deckId;
    }

    @JsonProperty("deck_id")
    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    @JsonProperty("cards")
    public List<CardAPICardJson> getCards() {
        return cards;
    }

    @JsonProperty("cards")
    public void setCards(List<CardAPICardJson> cards) {
        this.cards = cards;
    }

    @JsonProperty("remaining")
    public Integer getRemaining() {
        return remaining;
    }

    @JsonProperty("remaining")
    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    @JsonProperty("error")
    public String getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(String error) {
        this.error = error;
    }

}
