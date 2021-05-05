package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.objects.Deck;
import ch.uzh.ifi.hase.soprafs21.objects.CardAPIDeckResponseObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface cardAPIMapper {


    cardAPIMapper INSTANCE = Mappers.getMapper(cardAPIMapper.class);

    @Mapping(source = "deck_id", target = "deckID")
    Deck convertCardDeckAPIResponseObjectToDeck(CardAPIDeckResponseObject cardAPIDeckResponseObject);



}
