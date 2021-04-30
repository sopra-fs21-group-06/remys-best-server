package ch.uzh.ifi.hase.soprafs21.utils;

import ch.uzh.ifi.hase.soprafs21.objects.CardMove;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.Player;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.*;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.*;
import org.springframework.data.util.Pair;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Utility functions that are used throughout the game
 */
public class DogUtils {

    public static String getIdentity(SimpMessageHeaderAccessor sha) {
        Principal p = sha.getUser();
        if (p != null) {
            return p.getName();
        }
        else {
            return null;
        }
    }

    public static String convertTokenToUsername(String token, UserService userService){
        return userService.getUserRepository().findByToken(token).getUsername();
    }

    public static String convertSessionIdentityToUserName(String sessionIdentity, UserService userService){
        return userService.getUserRepository().findBySessionIdentity(sessionIdentity).getUsername();
    }

    public static WaitingRoomChooseColorDTO convertPlayersToWaitingRoomChooseColorDTO(List<Player> players) {
        return convertPlayersToWaitingRoomChooseColorDTO(players, null);
    }

    public static WaitingRoomChooseColorDTO convertPlayersToWaitingRoomChooseColorDTO(List<Player> players, UUID gameId){
        WaitingRoomChooseColorDTO waitingRoomChooseColorDTO = new WaitingRoomChooseColorDTO();
        waitingRoomChooseColorDTO.setGameId(gameId);

        List<ChooseColorPlayerDTO> chooseColorPlayers = new ArrayList<>();
        boolean areAllColorsAssigned = true;
        for(Player p: players) {
            if (p.getColor() == null) {
                areAllColorsAssigned = false;
            }
            chooseColorPlayers.add(DTOMapper.INSTANCE.convertPlayertoChooseColorPlayerDTO(p));
        }
        waitingRoomChooseColorDTO.setPlayers(chooseColorPlayers);
        waitingRoomChooseColorDTO.setStartGame(areAllColorsAssigned);

        return waitingRoomChooseColorDTO;
    }

    public static List<FactDTO> convertRoundBeginnerToFactList(String roundBeginner){
        List<FactDTO> factList = new ArrayList<>();
        factList.add(new FactDTO(roundBeginner, "Round Beginner"));
        factList.add(new FactDTO("Card Exchange", "Click on card to exchange"));
        return factList;
    }

    public static List<FactDTO> generateCurrentTurnFactList(int roundCount, String playerName, int nextCardAmount, String nextPlayerName){
        List<FactDTO> factList = new ArrayList<>();
        factList.add(new FactDTO(String.valueOf(roundCount), "Round"));
        factList.add(new FactDTO(playerName, "Active Player"));
        factList.add(new FactDTO(String.valueOf(nextCardAmount), "Next Round Card Amount"));
        factList.add(new FactDTO(nextPlayerName, "Next Round Beginner"));

        return factList;
    }
   public static GameNotificationDTO generateGameNotificatoinDTO(String playerName, String action, String card) {
       GameNotificationDTO gameNotificationDTO = new GameNotificationDTO();
       gameNotificationDTO.setPlayerName(playerName);
       gameNotificationDTO.setAction(action);
       gameNotificationDTO.setCard(card);
       return gameNotificationDTO;
   }

   public static RoundMoveListDTO generateRoundMoveListDTO(List<CardMove> moveList){
       RoundMoveListDTO roundMoveListDTO = new RoundMoveListDTO();
       List<MoveDTO> moveListDTO = new ArrayList<>();

       for(CardMove cM: moveList){
           moveListDTO.add(DTOMapper.INSTANCE.convertCardMovetoMoveDTO(cM));
       }
       roundMoveListDTO.setMoves(moveListDTO);
       return roundMoveListDTO;
   }

   public static RoundMarbleListDTO generateRoundMarblesListDTO(List<Marble> marbleList){
       RoundMarbleListDTO roundMarbleListDTO = new RoundMarbleListDTO();
       List<MarbleDTO> marbleDTOList = new ArrayList<>();

       for(Marble m: marbleList){
           marbleDTOList.add(DTOMapper.INSTANCE.convertMarbletoMarbleDTO(m));
       }
       roundMarbleListDTO.setMarbles(marbleDTOList);
       return roundMarbleListDTO;
   }

   public static ExecutedCardDTO generateExecutedCardDTO(String playerName, String cardCode, List<MarbleExecuteCardDTO> marbleExecuteCardDTOList){
       ExecutedCardDTO executredCardDTO = new ExecutedCardDTO();

       GameCardDTO gameCardDTO = new GameCardDTO();
       gameCardDTO.setCode(cardCode);

       executredCardDTO.setPlayerName(playerName);
       executredCardDTO.setCard(gameCardDTO);
       executredCardDTO.setMarbles(marbleExecuteCardDTOList);

       return executredCardDTO;
   }

   public static List<MarbleExecuteCardDTO> generateMarbleExecutreCardDTO(List<Pair<Integer, String>> tupleList){
        List<MarbleExecuteCardDTO> marbleExecuteCardDTOList = new ArrayList<>();
        for(Pair<Integer, String> p : tupleList){
            marbleExecuteCardDTOList.add(new MarbleExecuteCardDTO(p.getFirst(), p.getSecond()));
        }
        return marbleExecuteCardDTOList;
   }

    public static PossibleTargetFieldKeysListDTO generatePossibleTargetFieldKeyListDTO(List<String> targetFields){
        PossibleTargetFieldKeysListDTO possibleTargetFieldKeysListDTO = new PossibleTargetFieldKeysListDTO();
        possibleTargetFieldKeysListDTO.setTargetFieldKeys(targetFields);

        return possibleTargetFieldKeysListDTO;
    }
}
