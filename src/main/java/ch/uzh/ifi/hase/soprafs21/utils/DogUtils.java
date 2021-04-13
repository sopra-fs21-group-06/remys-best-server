package ch.uzh.ifi.hase.soprafs21.utils;


import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.security.Principal;

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
}
