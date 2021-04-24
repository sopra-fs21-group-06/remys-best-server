package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.Player;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.ChooseColorPlayerDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.FactDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.WaitingRoomEnterDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * - Handles general WebSocket stuff (chat, reconnect)
 * - Provides utility functions for sending packets
 */
@Service
@Transactional
public class WebSocketService {

    @Autowired
    public SimpMessagingTemplate simp;

    private void sendToPlayer(String identity, String path, Object dto) {
        this.simp.convertAndSendToUser(identity, "/queue" + path, dto);
    }

    private void sendToTopic(String path, Object dto){
        this.simp.convertAndSend("/topic" + path, dto);
    }

    public void sendStartGameMessage(UUID gameId) {
        WaitingRoomEnterDTO startGameObj = new WaitingRoomEnterDTO();
        startGameObj.setToken("Edouard ish de Geilst");

        String path = "/game/%s/startGame";
        sendToTopic(String.format(path, gameId.toString()), startGameObj);
    }

    public void sendExchangeFactsMessage(String roundBeginner, UUID gameId) {
        List<FactDTO> factList = new ArrayList<>();
        factList.add(new FactDTO(roundBeginner, "Round Beginner"));
        factList.add(new FactDTO("Card Exchange", "Click on card to exchange"));
        sendFactsMessage(factList, gameId);

        sendNotificationMessage( "Card Exchange", gameId);
    }

    public void sendCurrentTurnFactsMessage(int roundCount, String playerName, int nextCardAmount, String nextPlayerName, UUID gameId) {
        List<FactDTO> factList = new ArrayList<>();
        factList.add(new FactDTO(String.valueOf(roundCount), "Round"));
        factList.add(new FactDTO(playerName, "Active Player"));
        factList.add(new FactDTO(String.valueOf(nextCardAmount), "Next Round Card Amount"));
        factList.add(new FactDTO(nextPlayerName, "Next Round Beginner"));

        sendFactsMessage(factList, gameId);
    }

    public void sendFactsMessage(List<FactDTO> factList, UUID gameId) {
        GameFactsDTO gameFactsDTO = new GameFactsDTO();
        gameFactsDTO.setFacts(factList);

        String pathFacts = "/game/%s/facts";
        sendToTopic(String.format(pathFacts, gameId.toString()), gameFactsDTO);
    }

    public void sendNotificationMessage(String action, UUID gameId) {
        sendNotificationMessage(null, action, null, gameId);
    }

    public void sendNotificationMessage(String playerName, String action, String card, UUID gameId) {
        GameNotificationDTO gameNotificationDTO = new GameNotificationDTO();
        gameNotificationDTO.setPlayerName(playerName);
        gameNotificationDTO.setAction(action);
        gameNotificationDTO.setCard(card);

        String pathNotifications = "/game/%s/notification";
        sendToTopic(String.format(pathNotifications, gameId.toString()), gameNotificationDTO);
    }

    public void sendCurrentTurnMessage(String playerName, UUID gameId) {
        RoundCurrentPlayerDTO roundCurrentPlayerDTO = new RoundCurrentPlayerDTO();
        roundCurrentPlayerDTO.setPlayerName(playerName);

        String path = "/game/%s/turn";
        sendToTopic(String.format(path, gameId.toString()), roundCurrentPlayerDTO);
    }

    public void sendCardsToPlayer(String sessionIdentity, List<Card> cards, UUID gameId) {
        sendCardsToPlayer(sessionIdentity, cards, -1, gameId);
    }

    public void sendCardsToPlayer(String userSessionIdentity, List<Card> cards, int idx, UUID gameId) {
        GameListOfCardsDTO gameListOfCardsDTO = new GameListOfCardsDTO();
        List<GameCardDTO> cardList = new ArrayList<>();
        for(Card c : cards) {
            GameCardDTO gameCardDTO = DTOMapper.INSTANCE.convertCardtoGameCardDTO(c);
            if(idx > -1) {
                gameCardDTO.setIdx(idx);
            }
            cardList.add(gameCardDTO);
        }
        gameListOfCardsDTO.setCards(cardList);

        String path = "/game/%s/cards";
        sendToPlayer(userSessionIdentity, String.format(path, gameId.toString()), gameListOfCardsDTO);
    }

    public void sendGameAssignmentMessage(String userSessionIdentity, List<Player> players, UUID gameId) {
        WaitingRoomChooseColorDTO waitingRoomChooseColorDTO = DogUtils.convertPlayerListToWaitingRoomChoosecolorDTO(players, gameId);
        String path = "/waiting-room";
        sendToPlayer(userSessionIdentity, path, waitingRoomChooseColorDTO);
    }
}
