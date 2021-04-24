package ch.uzh.ifi.hase.soprafs21.utils;


import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.objects.Player;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.ChooseColorPlayerDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomChooseColorDTO;
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

    public static WaitingRoomChooseColorDTO convertPlayerListToWaitingRoomChoosecolorDTO(List<Player> playerList) {
        return convertPlayerListToWaitingRoomChoosecolorDTO(null, playerList);
    }

    public static WaitingRoomChooseColorDTO convertPlayerListToWaitingRoomChoosecolorDTO(UUID gameID, List<Player> playerList){
        WaitingRoomChooseColorDTO waitingRoomChooseColorDTO = new WaitingRoomChooseColorDTO();
        waitingRoomChooseColorDTO.setGameId(gameID);
        List<ChooseColorPlayerDTO> chooseColorPlayers = new ArrayList<>();
        for(Player p: playerList) {
            chooseColorPlayers.add(DTOMapper.INSTANCE.convertPlayertoChooseColorPlayerDTO(p));
        }
        waitingRoomChooseColorDTO.setPlayers(chooseColorPlayers);
        return waitingRoomChooseColorDTO;
    }
}
