package ch.uzh.ifi.hase.soprafs21.objects;


import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;


import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameListOfCardsDTO;

import ch.uzh.ifi.hase.soprafs21.service.PlayingBoardService;


import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;



import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameListOfCardsDTO;



import java.util.ArrayList;
import java.util.List;

public class Round {
    private final CardAPIService cardAPIService;
    private Game game;
    private int nrCards;
    private PlayingBoardService playingBoardService;
    private Player currentPlayer;
    private List<Player> players;
    //private DeckService deckService;
    private Player winner = null;
    private String deckId;
    private final WebSocketService webSocketService;
    private final UserService userService;



    public Round(List<Player> players, Player startPlayer, int nrCards, Game game, CardAPIService cardAPIService, WebSocketService webSocketService, UserService userService){

                this.game = game;
                this.players = players;
                this.currentPlayer = startPlayer;
                this.nrCards = nrCards;
                this.cardAPIService = cardAPIService;
                deckId = cardAPIService.createDeck().getDeck_id();
                this.webSocketService = webSocketService;
                this.userService = userService;
                initializeRound();
    }

    public void setDeckId (String deckId){
        this.deckId = deckId;
    }



    public void initializeRound () {

        for (Player p : players) {
            if (getGame().getCardCount() == 0) {
                cardAPIService.shuffle(deckId);
                getGame().setCardCount(53);
            }
            else if (getGame().getCardCount() < nrCards) {
                String firstDraw = String.valueOf(getGame().getCardCount());
                String secondDraw = String.valueOf(nrCards - getGame().getCardCount());

                //first draw
                Hand hand = new Hand(cardAPIService.drawCards(deckId, firstDraw));
                p.setHand(hand);

                cardAPIService.shuffle(deckId);

                //second draw
                p.getHand().addCardsToHand(cardAPIService.drawCards(deckId, secondDraw));
                sendOutCardToHandDTO(p);

                getGame().setCardCount(53 - (nrCards - getGame().getCardCount()));
            }

            else {
                String str = String.valueOf(nrCards);
                Hand hand = new Hand(cardAPIService.drawCards(deckId, str));
                p.setHand(hand);

                sendOutCardToHandDTO(p);

                getGame().setCardCount(getGame().getCardCount() - nrCards);

            }
        }

    }

    public void changeCurrentPlayer () {
        int i = players.indexOf(currentPlayer);
        int inext = (i + 1) % 4;
        currentPlayer = players.get(inext);
    }
    public String getNameNextPlayer () {
        int i = players.indexOf(currentPlayer);
        int inext = (i + 1) % 4;
        return players.get(inext).getPlayerName();
    }


        public void setNrCards ( int nrCards){
            this.nrCards = nrCards;
        }



        public int getNrCards () {
            return nrCards;
        }

        public Game getGame () {
            return game;
        }

        public Player getCurrentPlayer () {
            return currentPlayer;
        }

        public void setCurrentPlayer (Player currentPlayer){
            this.currentPlayer = currentPlayer;
        }

        public void setGame (Game game){
            this.game = game;
        }

        private void sendOutCardToHandDTO(Player p){
            GameListOfCardsDTO gameListOfCardsDTO = new GameListOfCardsDTO();
            List<GameCardDTO> cardList = new ArrayList<>();
            for(Card c : p.getHand().getHandDeck()){
                cardList.add(DTOMapper.INSTANCE.convertCardtoGameCardDTO(c));
            }
            gameListOfCardsDTO.setCards(cardList);

            webSocketService.sendToPlayer(userService.getUserRepository().findByUsername(p.getPlayerName()).getSessionIdentity(), String.format("queue/game/%s/cards", game.getGameID().toString()), gameListOfCardsDTO);
        }
    }

