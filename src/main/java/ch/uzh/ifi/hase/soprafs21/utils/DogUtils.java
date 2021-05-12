package ch.uzh.ifi.hase.soprafs21.utils;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement.RemainingSevenMovesDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.*;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.*;
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
   public static GameNotificationDTO generateGameNotificatoinDTO(String playerName, String action, String cardCode) {
       GameNotificationDTO gameNotificationDTO = new GameNotificationDTO();
       gameNotificationDTO.setPlayerName(playerName);
       gameNotificationDTO.setAction(action);
       if(cardCode != null) {
           gameNotificationDTO.setCard(Card.convertCardCodeToCardName(cardCode));
       }
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

    public static RemainingSevenMovesDTO generateRemainingSevenMovesDTO(int remainingSevenMoves){
        RemainingSevenMovesDTO remainingSevenMovesDTO = new RemainingSevenMovesDTO();
        remainingSevenMovesDTO.setRemainingSevenMoves(remainingSevenMoves);
        return remainingSevenMovesDTO;
    }

   public static List<MarbleExecuteCardDTO> generateMarbleExecutedCardDTO(ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys){
        List<MarbleExecuteCardDTO> marbleExecuteCardDTOList = new ArrayList<>();
        for(MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey : marbleIdsAndTargetFieldKeys){
            marbleExecuteCardDTOList.add(new MarbleExecuteCardDTO(marbleIdAndTargetFieldKey.getMarbleId(), marbleIdAndTargetFieldKey.getFieldKey()));
        }
        return marbleExecuteCardDTOList;
   }

   public static ArrayList<MarbleIdAndTargetFieldKey> generateMarbleIdsAndTargetFieldKeys(List<MarbleExecuteCardDTO> marbleExecuteCardDTOs) {
       ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKey = new ArrayList<>();

       for(MarbleExecuteCardDTO m: marbleExecuteCardDTOs) {
           MarbleIdAndTargetFieldKey mIdFieldKey = new MarbleIdAndTargetFieldKey(m.getMarbleId(), m.getTargetFieldKey());
           marbleIdsAndTargetFieldKey.add(mIdFieldKey);
       }

       return marbleIdsAndTargetFieldKey;
   }

    public static PossibleTargetFieldKeysListDTO generatePossibleTargetFieldKeyListDTO(List<String> targetFields){
        PossibleTargetFieldKeysListDTO possibleTargetFieldKeysListDTO = new PossibleTargetFieldKeysListDTO();
        possibleTargetFieldKeysListDTO.setTargetFieldKeys(targetFields);

        return possibleTargetFieldKeysListDTO;
    }
    public static void resetStatusTokenAndSessionIdentity(UserService userService, String username){
        User user = userService.findByUsername(username);
        user.setToken(null);
        user.setStatus(UserStatus.Offline);
        user.setSessionIdentity(null);
        userService.getUserRepository().saveAndFlush(user);

    }

    public static GameSessionInvitedUsersDTO generateGameSessionInvitedUsersDTO(List<User> invitedUsers){
        GameSessionInvitedUsersDTO gameSessionInvitedUsersDTO = new GameSessionInvitedUsersDTO();
        List<WaitingRoomUserObjDTO> invitedUsersList = new ArrayList<>();
        for(User u: invitedUsers){
            invitedUsersList.add(DTOMapper.INSTANCE.convertUsertoWaitingRoomUserObjDTO(u));
        }
        gameSessionInvitedUsersDTO.setInvitedUsers(invitedUsersList);
        return gameSessionInvitedUsersDTO;
    }

    public static RequestCountDownDTO generateRequestCountDownDTO(int currentCounter, String userName){
        RequestCountDownDTO requestCountDownDTO = new RequestCountDownDTO();
        requestCountDownDTO.setCurrentCounter(currentCounter);
        requestCountDownDTO.setUsername(userName);
        return requestCountDownDTO;
    }

    public static GameSessionHostLeftDTO generateGameSessionHostLeftDTO(String hostName){
        GameSessionHostLeftDTO gameSessionHostLeftDTO = new GameSessionHostLeftDTO();
        gameSessionHostLeftDTO.setHostName(hostName);
        return gameSessionHostLeftDTO;
    }

    public static GameSessionInviteUserDTO generateGameSessoinInviteUserDTO(UUID gameSessionIdentity, String hostName){
        GameSessionInviteUserDTO gameSessionInviteUserDTO = new GameSessionInviteUserDTO();
        gameSessionInviteUserDTO.setGameSessionId(gameSessionIdentity);
        gameSessionInviteUserDTO.setHostName(hostName);
        return gameSessionInviteUserDTO;
    }

    public static GameThrowAwayDTO generateGameThrowAwayDTO(String playerName, List<String> cardCodes){
        GameThrowAwayDTO gameThrowAwayDTO = new GameThrowAwayDTO();
        gameThrowAwayDTO.setPlayerName(playerName);
        gameThrowAwayDTO.setCardCodes(cardCodes);
        return gameThrowAwayDTO;
    }

    public static String getNextPlayerName(Player currentPlayer, List<Player> players) {
        Color currentColor = currentPlayer.getColor();
        Color nextColor = getNextColor(currentColor);

        String playerName = null;
        for (Player player: players){
            if(player.getColor().equals(nextColor)){
                playerName = player.getPlayerName();
                break;
            }
        }

        return playerName;
    }

    public static Color getNextColor(Color color) {
        if(color.equals(Color.BLUE)) {
            return Color.GREEN;
        } else if(color.equals(Color.GREEN)){
            return Color.RED;
        } else if(color.equals(Color.RED)){
            return Color.YELLOW;
        } else {
            return Color.BLUE;
        }
    }
}
