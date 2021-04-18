package ch.uzh.ifi.hase.soprafs21.websocket;

import java.security.Principal;

class StompPrincipal implements Principal {
    String name;

    StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
