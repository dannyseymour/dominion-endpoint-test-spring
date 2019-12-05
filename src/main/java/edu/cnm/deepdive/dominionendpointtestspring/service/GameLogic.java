package edu.cnm.deepdive.dominionendpointtestspring.service;


import edu.cnm.deepdive.dominionendpointtestspring.model.dao.CardRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.GamePlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.GameRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.PlayRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.PlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.TurnRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card.Location;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card.Type;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Play;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player.PlayerStateInGame;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Transactional
@WithStateMachine
public class GameLogic {


  private final GameRepository gameRepository;


  private final TurnRepository turnRepository;


  private final PlayRepository playRepository;


  private final PlayerRepository playerRepository;


  private final CardRepository cardRepository;

  private final GamePlayerRepository gamePlayerRepository;

  @Autowired
  public GameLogic(
      GameRepository gameRepository,
      TurnRepository turnRepository,
      PlayRepository playRepository,
      PlayerRepository playerRepository,
      CardRepository cardRepository,
      GamePlayerRepository gamePlayerRepository) {
    this.gameRepository = gameRepository;
    this.turnRepository = turnRepository;
    this.playRepository = playRepository;
    this.playerRepository = playerRepository;
    this.cardRepository = cardRepository;
    this.gamePlayerRepository = gamePlayerRepository;
  }

  /**
   * This method gets the current turn. It is a helper method used in several
   * gameLogic functions.
   * @param game
   * @return
   */
  public Turn getCurrentTurn(Game game) {
    return turnRepository.getAllByGameOrderByIdDesc(game).get()
        .get(turnRepository.getAllByOrderByIdDesc().get().size() - 1);
  }
  /**
   * This method gets the previous turn. It is a helper method used in several
   * gameLogic functions.
   * @param game
   * @return
   */
  public Turn getPreviousTurn(Game game) {
    return turnRepository.getAllByGameOrderByIdDesc(game).get()
        .get(turnRepository.getAllByOrderByIdDesc().get().size() - 2);
  }

  /**
   * This method plays a card. It calls the helper methods to calculate the change in player,
   * turn, and game state, and returns a Game object to the player.
   * @param cardType
   * @param game
   * @param player
   * @param cardStrings
   * @return
   */
  public Game playCard(String cardType, Game game, Player player,
      ArrayList<String> cardStrings) {
    Turn turn = getCurrentTurn(game);
    boolean hasSilver = false;
    ArrayList<Card> currentHand = (ArrayList<Card>) cardRepository
        .getAllByPlayerAndLocation(player, Location.HAND).get();
    for (Card card: currentHand) {
      if (card.getType().equals(Type.SILVER)) {
        hasSilver = true;
      }
    }
    if (cardRepository
        .getByLocationAndPlayerAndType(Location.HAND, player,
            Type.valueOf(cardType.toUpperCase()))
        .isPresent()) {
      Card playingCard = cardRepository
          .getByLocationAndPlayerAndType(Location.HAND, player, Type.valueOf(cardType)).get();
      playCardProcessing(game, turn, player, playingCard, hasSilver, cardStrings);
      playingCard.setLocation(Location.DISCARD);

      if (player.getActionsRemaining() == 0) {
        game.setCurrentState(GameState.BUYING);
      }
      return game;
    } else {
      return game;
    }
  }

  /**
   * This is an overload of the other playCard method which functions if no additional
   * cards have been passed in as arguments.
   * @param cardType
   * @param game
   * @param player
   * @return
   */
  public Game playCard(String cardType, Game game, Player player) {
    Turn turn = getCurrentTurn(game);
    boolean hasSilver = false;
    ArrayList<Card> currentHand = (ArrayList<Card>) cardRepository
        .getAllByPlayerAndLocation(player, Location.HAND).get();
    for (Card card: currentHand) {
      if (card.getType().equals(Type.SILVER)) {
        hasSilver = true;
      }
    }
    if (cardRepository
        .getByLocationAndPlayerAndType(Location.HAND, player,
            Type.valueOf(cardType.toUpperCase()))
        .isPresent()) {
      Card playingCard = cardRepository
          .getByLocationAndPlayerAndType(Location.HAND, player, Type.valueOf(cardType)).get();
      playCardProcessing(game, turn, player, playingCard, hasSilver);
      playingCard.setLocation(Location.DISCARD);

      if (player.getActionsRemaining() == 0) {
        game.setCurrentState(GameState.BUYING);
      }
      return game;
    } else {
      return game;
    }
  }

  /**
   * This method performs actions to buy a card.
   * @param cardType
   * @param game
   * @param player
   * @return
   */
  public Game buyCard(String cardType, Game game, Player player) {
    Turn turn = getCurrentTurn(game);
    int buyingPower = turn.getBuyingPower();
    Card card = cardRepository.getByType(Type.valueOf(cardType)).get();
    int stackCount = cardRepository.countAllByTypeAndLocation(card.getType(), Location.STACK).get();
    if (stackCount ==0){
      return game;
    }
    if (card.getType().getCost()<=buyingPower) {
      card.setLocation(Location.DISCARD);
      buyingPower-=card.getType().getCost();
      turn.setBuyingPower(buyingPower);
      return game;
    }
    else{
      return game;
    }

  }

  /**
   * This method ends the current phase, called from GameController.
   * @param game
   * @param player
   * @return
   */
  public Game endPhase(Game game, Player player) {
    Player activePlayer = whoIsActive();
    Player inactivePlayer = whoIsPassive();
    Turn turn = getCurrentTurn(game);

    if (!testForGameEnd(game)) {

      switch (game.getCurrentState()) {
        case DISCARDING:
          game.setCurrentState(GameState.ACTION);
          break;
        case ACTION:
          game.setCurrentState(GameState.BUYING);
          break;
        case BUYING:
          activePlayer.setPlayerStateInGame(PlayerStateInGame.PASSIVE);
          discard(activePlayer, cardRepository.getAllByPlayerAndLocation(activePlayer,Location.HAND).get());
          for (int i = 0; i< 5; i++){
            draw(activePlayer);
          }

          initTurn(game, inactivePlayer, false);
          break;
      }

    } else {
      game.setCurrentState(GameState.END_GAME);
      game.setWhoWins(whoWins(game, activePlayer, inactivePlayer));
    }
    return game;
  }


  /**
   * This card performs the discarding functions when a player is attacked.
   * @param game
   * @param player
   * @param cardStrings
   * @return
   */
  public Game discardForMilitia(Game game, Player player, List<String> cardStrings) {
    ArrayList<Card> cards = new ArrayList<>();
    for (String s: cardStrings){
      cards.add(cardRepository.getByType(Type.valueOf(s)).get());
    }
    discard(player, cards);
    if (cardRepository.countAllByPlayerAndLocation(player, Location.HAND).get()<=3){
      game.setCurrentState(GameState.ACTION);
    }
    return game;
  }

  /**
   * This is a helper method to discard cards from a player's hand.
   * @param player
   * @param cards
   */
  private void discard(Player player, List<Card> cards){
    for (Card card: cards){
      card.setLocation(Location.DISCARD);
    }
  }

  /**
   * This method sets up the initial conditions for a game.
   * @param game
   * @param player1
   * @param player2
   * @return
   */
  public Game initializeGame(Game game, Player player1, Player player2) {
    setupStacks(game);
    deal(game, player1);
    deal(game, player2);
  // game.setCurrentGamePlayer(
    player1.setPlayerStateInGame(PlayerStateInGame.ACTIVE);
    player2.setPlayerStateInGame(PlayerStateInGame.PASSIVE);
    game = initTurn(game, player1, true);
    return game;
  }

  /**
   * This is a helper method that sets up stacks of cards for the game.
   * @param game
   */

  private void setupStacks(Game game) {
    LinkedList<Card> initialCards = new LinkedList<>();
    for (Card.Type type : Card.Type.values()) {
      for (int i = 0; i < type.getInitialTotalCount(); i++) {
        Card card = new Card(type);
        card.setLocation(Location.STACK);
        card.setGame(game);
        initialCards.add(card);
      }
    }
    cardRepository.saveAll(initialCards);
  }

  /**
   * This is a helper method that deals cards at the beginning of the game.
   * @param game
   * @param player
   */
  private void deal(Game game, Player player) {
    LinkedList<Card> initialCardsForPlayer = new LinkedList<>();
    for (Card.Type type : Card.Type.values()) {
      for (int i = 0; i < type.getInitialPlayerCount(); i++) {
        Card card = new Card(type);
        card.setLocation(Location.DRAW_PILE);
        card.setGame(game);
        card.setPlayer(player);
        initialCardsForPlayer.add(card);
        card.setOrderInLocation(initialCardsForPlayer.indexOf(card));
      }
    }
    shuffle(player);
    for (int i = 0; i< 5; i++){
      draw(player);
    }
    cardRepository.saveAll(initialCardsForPlayer);
  }

  /**
   * This is a helper method to shuffle a stack of cards.
   * @param player
   */
  private void shuffle(Player player) {
    List<Card> cardsInDraw = new LinkedList<>();
    List<Card> cardsInDiscard = new LinkedList<>();
    if (cardRepository
        .getAllByPlayerAndLocation(player, Location.DRAW_PILE).isPresent()){
      cardsInDraw =cardRepository
          .getAllByPlayerAndLocation(player, Location.DRAW_PILE).get();
      cardsInDraw.addAll(cardsInDiscard);
      Collections.shuffle(cardsInDraw);
    }else{
      Collections.shuffle(cardsInDiscard);
      cardsInDraw.addAll(cardsInDiscard);
      cardsInDiscard.clear();
    }
    for (Card card: cardsInDraw){
      card.setLocation(Location.DRAW_PILE);
    }

    cardRepository.saveAll(cardsInDraw);
  }

  /**
   * This returns a list of Strings describing the plays from last turn.
   * @param game
   * @return
   */
  public List<String> getListOfPlaysStrings(Game game) {
    Turn turn = getPreviousTurn(game);
    List<Play> plays = playRepository.getAllByTurn(turn).get();
    List<String> playStrings = new ArrayList<>();
    for (Play play : plays) {
      playStrings.add(playToString(play));
    }
    return playStrings;
  }

  /**
   * This is a helper method used to build a description of the play that occurred.
   * @param play
   * @return
   */
  private String playToString(Play play) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(play.getPlayer().getDisplayName())
        .append(" ")
        .append(play.getType().toString())
        .append(" ")
        .append(play.getCard().getType().toString());
    return stringBuilder.toString();
  }

  /**
   * This is a helper method to draw one card at a time, called from several other methods.
   * @param player
   */
  private void draw(Player player) {
    int drawPileCount = cardRepository.countAllByPlayerAndLocation(player, Location.DRAW_PILE).get();
    if (drawPileCount ==0){
      shuffle(player);
    }
    ArrayList<Card> drawPile = cardRepository.getAllByPlayerAndLocationOrderByOrderInLocation(player, Location.DRAW_PILE);
    Card card = drawPile.get(0);
    card.setLocation(Location.HAND);
    cardRepository.save(card);
  }

  /**
   * This initializes the conditions for a turn, called by the phase state transition manager above.
   * @param game
   * @param player
   * @param isFirstTurn
   * @return
   */
  private Game initTurn(Game game, Player player, boolean isFirstTurn) {
    Turn turn = new Turn(game, player);
    turnRepository.save(turn);
    turn.setBuyingPower(calculateInitialBuyingPower(game, player));
    player.setPlayerStateInGame(PlayerStateInGame.ACTIVE);
    player.setActionsRemaining(1);
    player.setBuysRemaining(1);
    Turn previousTurn = getPreviousTurn(game);
    draw(player);
    boolean needsToDiscard = false;
    List<Card> cardsInHand = cardRepository.getAllByPlayerAndLocation(player, Location.HAND)
        .get();
    if (!isFirstTurn) {
      List<Play> playsFromOtherPlayer = playRepository.getAllByGameAndTurn(game, previousTurn)
          .get();
      for (Play play : playsFromOtherPlayer) {
        if (play.getCard() != null && play.getCard().getType().equals(Type.MILITIA)) {
          needsToDiscard = true;
          for (Card card : cardsInHand) {
            if (card.getType().equals(Type.MOAT)) {
              needsToDiscard = false;
            }
          }
        }
      }
      if (needsToDiscard) {
        game.setCurrentState(GameState.DISCARDING);
      } else {
        game.setCurrentState(GameState.ACTION);
      }
    } else {
      game.setCurrentState(GameState.ACTION);
    }

    //game.setCurrentGamePlayer(player);
    return game;
  }

  /**
   * This is a helper method to calculate the buying power at the beginning of
   * a player's turn.
   * @param game
   * @param player
   * @return
   */
  private int calculateInitialBuyingPower(Game game, Player player) {
    ArrayList<Card> cardsInHand = (ArrayList<Card>) cardRepository
        .getAllByPlayerAndLocation(player, Location.HAND).get();
    int buyingPower = 0;
    boolean hasSilver = false;
    for (Card card : cardsInHand) {
      switch (card.getType()) {
        case COPPER:
          buyingPower += 1;
          break;
        case SILVER:
          buyingPower += 3;
          hasSilver = true;
          break;
        case GOLD:
          buyingPower += 5;
          break;
        default:
          break;
      }
    }
    return buyingPower;
  }

  /**
   * This method counts up all the victory points from each card by type
   * in the player's location.
   * @param player
   * @return
   */
  private int calculateVictoryPoints(Player player) {
    ArrayList<Card> allPlayersCards = cardRepository.getAllByPlayer(player).get();
    int victoryCounter = 0;
    for (Card card : allPlayersCards) {
      victoryCounter+= card.getType().getVictoryPoints();
    }
    return victoryCounter;
  }

  /**
   * This is a helper method to update the state of the turn, game, and player
   * given the attributes of a certain card.
   * @param game
   * @param turn
   * @param player
   * @param card
   * @param hasSilver
   */
  private void constructNormalPlay(Game game, Turn turn, Player player, Card card,
      boolean hasSilver) {
    int currentActionsRemaining = player.getActionsRemaining();
    player.setActionsRemaining(currentActionsRemaining - 1 + card.getType().getExtraActions());

    int currentBuysRemaining = player.getBuysRemaining();
    player.setBuysRemaining(currentBuysRemaining - 1 + card.getType().getExtraBuys());

    int currentBuyingPower = turn.getBuyingPower();
    if (hasSilver) {
      currentBuyingPower += card.getType().getExtraGoldIfSilver();
    }
    turn.setBuyingPower(currentBuyingPower + card.getType().getExtraGold());

    for (int i = 0; i < card.getType().getDrawCards(); i++) {
      draw(player);
    }

  }

  /**
   * This is a helper method to handle the special situations
   * that arise when certain cards are played.
   * @param game
   * @param turn
   * @param player
   * @param card
   * @param hasSilver
   * @param otherCardsStrings
   */
  private void playCardProcessing(Game game, Turn turn, Player player, Card card,
      boolean hasSilver, List<String> otherCardsStrings) {
    constructNormalPlay(game, turn, player, card, hasSilver);
    ArrayList<Card> otherCards = new ArrayList<>();
    switch (card.getType()) {
      case WORKSHOP:
        Card buyCard = cardRepository
            .getAllByTypeAndLocation(Type.valueOf(otherCardsStrings.get(0)), Location.STACK).get()
            .get(0);
        if (buyCard.getType().getCost() <= 4) {
          buyCard.setLocation(Location.HAND);
          buyCard.setPlayer(player);
        }
        break;
      case CELLAR:
        for (String s : otherCardsStrings) {
          draw(player);
        }
        break;
      case MINE:
        trashAndBuy(3, otherCardsStrings);
        break;
      case REMODEL:
        trashAndBuy(2, otherCardsStrings);
        break;

    }
  }

  /**
   * This is a helper method to trash and buy a card, called from the
   * special card circumstances in the processingCard methods.
   * @param more
   * @param otherCardsStrings
   */
  private void trashAndBuy(int more, List<String> otherCardsStrings){
  Card trashCard = cardRepository.getByType(Type.valueOf(otherCardsStrings.get(0))).get();
  trashCard.setLocation(Location.TRASH);
  Card buyingCard = cardRepository.getByType(Type.valueOf(otherCardsStrings.get(1))).get();
  if (buyingCard.getType().getCost()<=more+trashCard.getType().getCost()){
    buyingCard.setLocation(Location.DISCARD);
  }
}

  /**
   * This method may be redundant.
   * @param game
   * @param turn
   * @param player
   * @param card
   * @param hasSilver
   */
  private void playCardProcessing(Game game, Turn turn, Player player, Card card,
      boolean hasSilver) {
    constructNormalPlay(game, turn, player, card, hasSilver);

  }

  /**
   * This method is called at the end of various phases and tests end-game conditions.
   * @param game
   * @return
   */
  private boolean testForGameEnd(Game game) {
    int emptyStacks = 0;
    boolean provincesEmpty = false;
    if (cardRepository.countAllByTypeAndLocation(Type.PROVINCE, Location.STACK).get() == 0) {
      return true;
    }
    for (Type type : Type.values()) {
      if (cardRepository.countAllByTypeAndLocation(type, Location.STACK).get() == 0 && !type
          .equals(Type.PROVINCE)) {
        emptyStacks += 1;
      }
    }
    if (emptyStacks >= 3) {
      return true;
    }
    return false;
  }

  /**
   * if testForGameEnd returns true, this method calculates who has more victory
   * points by querying the cards in their locations.
   * @param game
   * @param player1
   * @param player2
   * @return
   */
  private String whoWins(Game game, Player player1, Player player2) {
    if (calculateVictoryPoints(player1) > calculateVictoryPoints(player2)) {
      return player1.getDisplayName();
    } else if (calculateVictoryPoints(player2) > calculateVictoryPoints(player1)) {
      return player2.getDisplayName();
    } else {
      return "Tie";
    }
  }

  /**
   * this is a helper method to find who is the current active player.
   * @return
   */
  private Player whoIsActive() {
    return playerRepository.getByPlayerStateInGame(PlayerStateInGame.ACTIVE);
  }

  /**
   * This is a helper method to find who is the current non-active player.
   * @return
   */
  private Player whoIsPassive() {
    return playerRepository.getByPlayerStateInGame(PlayerStateInGame.PASSIVE);
  }


}

