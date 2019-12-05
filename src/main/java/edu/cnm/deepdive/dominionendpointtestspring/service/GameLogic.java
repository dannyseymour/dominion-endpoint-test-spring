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
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.GamePlayer;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.GamePlayer.GamePlayerState;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Play;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameState;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStateInfo;
import java.util.ArrayList;
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

  public Turn getCurrentTurn(Game game) {
    return turnRepository.getAllByGameOrderByTurnIdDesc(game).get()
        .get(turnRepository.getAllByOrderByTurnIdDesc().get().size() - 1);
  }

  public Turn getPreviousTurn(Game game) {
    return turnRepository.getAllByGameOrderByTurnIdDesc(game).get()
        .get(turnRepository.getAllByOrderByTurnIdDesc().get().size() - 2);
  }

  public Game getCurrentGame(List<GamePlayer> players) {
    return gameRepository.getFirstByGamePlayers(players);
  }

  public Game playCard(String cardType, Game game, GamePlayer player,
      ArrayList<String> cardStrings) {
    Turn turn = getCurrentTurn(game);
    boolean hasSilver = false;
    ArrayList<Card> currentHand = (ArrayList<Card>) cardRepository
        .getAllByGamePlayerAndLocation(player, Location.HAND).get();
    for (Card card: currentHand) {
      if (card.getType().equals(Type.SILVER)) {
        hasSilver = true;
      }
    }
    if (cardRepository
        .getByLocationAndGamePlayerAndType(Location.HAND, player,
            Type.valueOf(cardType.toUpperCase()))
        .isPresent()) {
      Card playingCard = cardRepository
          .getByLocationAndGamePlayerAndType(Location.HAND, player, Type.valueOf(cardType)).get();
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

  public Game playCard(String cardType, Game game, GamePlayer player) {
    Turn turn = getCurrentTurn(game);
    boolean hasSilver = false;
    ArrayList<Card> currentHand = (ArrayList<Card>) cardRepository
        .getAllByGamePlayerAndLocation(player, Location.HAND).get();
    for (Card card: currentHand) {
      if (card.getType().equals(Type.SILVER)) {
        hasSilver = true;
      }
    }
    if (cardRepository
        .getByLocationAndGamePlayerAndType(Location.HAND, player,
            Type.valueOf(cardType.toUpperCase()))
        .isPresent()) {
      Card playingCard = cardRepository
          .getByLocationAndGamePlayerAndType(Location.HAND, player, Type.valueOf(cardType)).get();
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


  public Game buyCard(String cardName, Game game, GamePlayer player) {
    Card.Type cardType = Card.Type.valueOf(cardName.toUpperCase());
    Turn turn = getCurrentTurn(game);

    int buyingPower = turn.getBuyingPower();
    if (buyingPower > 0) {

    } else {
      gameStateInfo.getCurrentPlayerStateInfo().getDiscardPile().addToDiscard(buyCard);

      int buysRemaining = gameStateInfo.getCurrentPlayerStateInfo().calculateBuyingPower() - 1;
      if (buysRemaining <= 0) {
        gameStateInfo.getCurrentPlayerStateInfo().getPlayer().setNumBuy(buysRemaining);
        // gameStateInfo.saveAll();
        endTurn(getCurrentGame(), gameStateInfo.getCurrentPlayer().get());
        return gameStateInfo;
      } else {
        gameStateInfo.getCurrentPlayerStateInfo().getPlayer().setNumBuy(buysRemaining);
        //  gameStateInfo.saveAll();
        return gameStateInfo;
      }
    }
    return gameStateInfo;
    initTurn(game game, )
    return game;
  }


  public Game endPhase(Game game, GamePlayer gamePlayer) {
    GamePlayer activePlayer = whoIsActive();
    GamePlayer inactivePlayer = whoIsPassive();
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
          activePlayer.setGamePlayerState(GamePlayerState.PASSIVE);
          initTurn(game, inactivePlayer, false);
          break;
      }

    } else {
      game.setCurrentState(GameState.END_GAME);
      game.setWhoWins(whoWins(game, activePlayer, inactivePlayer));
    }
    return game;
  }

  //i= 1 get this turn, i=2 get last turn
  private Turn getSpecificTurnsAgo(int i) {
    ArrayList<Turn> turns = (ArrayList<Turn>) turnRepository.getAllByOrderByTurnIdDesc().get();
    return turns.get(turns.size() - i);
  }


  public Game discardForMilitia(Game game, GamePlayer gamePlayer, List<String> cardStrings) {
    ArrayList<Card> cards = new ArrayList<>();
    for (String s: cardStrings){
      cards.add(cardRepository.getByType(Type.valueOf(s)).get());
    }
    discard(gamePlayer, cards);
    if (cardRepository.countAllByGamePlayerAndLocation(gamePlayer, Location.HAND).get()<=3){
      game.setCurrentState(GameState.ACTION);
    }
    return game;
  }

  private void discard(GamePlayer gamePlayer, List<Card> cards){
    for (Card card: cards){
      card.setLocation(Location.DISCARD);
    }
  }

  public Game initializeGame(Game game, GamePlayer player1, GamePlayer player2) {
    setupStacks(game);
    deal(game, player1);
    deal(game, player2);
    game.setCurrentGamePlayer(player1);
    player1.setGamePlayerState(GamePlayerState.ACTIVE);
    player2.setGamePlayerState(GamePlayerState.PASSIVE);
    game = initTurn(game, player1, true);
    return game;
  }



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


  private void deal(Game game, GamePlayer player) {
    LinkedList<Card> initialCardsForPlayer = new LinkedList<>();
    for (Card.Type type : Card.Type.values()) {
      for (int i = 0; i < type.getInitialPlayerCount(); i++) {
        Card card = new Card(type);
        card.setLocation(Location.DRAW_PILE);
        card.setGame(game);
        card.setGamePlayer(player);
        initialCardsForPlayer.add(card);
        card.setOrderInLocation(initialCardsForPlayer.indexOf(card));
      }
    }
    shuffleAndDraw(player);
    cardRepository.saveAll(initialCardsForPlayer);
  }

  private void shuffleAndDraw(GamePlayer player) {
    List<Card> cardsInDraw = cardRepository
        .getAllByGamePlayerAndLocation(player, Location.DRAW_PILE).get();
    List<Card> cardsInHand = new LinkedList<>();

    //SHUFFLE
    //DRAW
  }


  private List<String> getListOfPlaysStrings(Turn turn) {
    List<Play> plays = playRepository.getAllByTurn(turn).get();
    List<String> playStrings = new ArrayList<>();
    for (Play play : plays) {
      playStrings.add(playToString(play));
    }
    return playStrings;
  }

  private String playToString(Play play) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(play.getGamePlayer().getPlayer().getDisplayName())
        .append(" ")
        .append(play.getType().toString())
        .append(" ")
        .append(play.getCard().getType().toString());
    return stringBuilder.toString();
  }

  private void draw(GamePlayer player) {

  }

  private Game initTurn(Game game, GamePlayer player, boolean isFirstTurn) {
    Turn turn = new Turn(game, player);
    turnRepository.save(turn);
    turn.setBuyingPower(calculateInitialBuyingPower(game, player));
    player.setActionsRemaining(1);
    player.setBuysRemaining(1);
    Turn previousTurn = getPreviousTurn(game);
    draw(player);
    boolean needsToDiscard = false;
    List<Card> cardsInHand = cardRepository.getAllByGamePlayerAndLocation(player, Location.HAND)
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
    game.setCurrentGamePlayer(player);
    return game;
  }

  private int calculateInitialBuyingPower(Game game, GamePlayer player) {
    ArrayList<Card> cardsInHand = (ArrayList<Card>) cardRepository
        .getAllByGamePlayerAndLocation(player, Location.HAND).get();
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


  private int calculateVictoryPoints(GamePlayer player) {
    ArrayList<Card> allPlayersCards = cardRepository.getAllByGamePlayer(player).get();
    int victoryCounter = 0;
    for (Card card : allPlayersCards) {
      switch (card.getType()) {
        case ESTATE:
          victoryCounter += 1;
          break;
        case DUCHY:
          victoryCounter += 3;
          break;
        case PROVINCE:
          victoryCounter += 5;
          break;
        default:
          break;
      }

    }
    return victoryCounter;
  }

  private void constructNormalPlay(Game game, Turn turn, GamePlayer player, Card card,
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

  private void playCardProcessing(Game game, Turn turn, GamePlayer player, Card card,
      boolean hasSilver, List<String> otherCardsStrings) {
    constructNormalPlay(game, turn, player, card, hasSilver);
    ArrayList<Card> otherCards = new ArrayList<>();
    switch (card.getType()) {
      case MILITIA:
        break;
      case WORKSHOP:
        int limit = 4;
        Card buyCard = cardRepository
            .getAllByTypeAndLocation(Type.valueOf(otherCardsStrings.get(0)), Location.STACK).get()
            .get(0);
        if (buyCard.getType().getCost() <= 4) {
          buyCard.setLocation(Location.HAND);
          buyCard.setGamePlayer(player);
        }
        break;
      case CELLAR:
        for (String s : otherCardsStrings) {

        }
        break;
      case MINE:
        break;
      case REMODEL:
        break;


    }
  }

  private void playCardProcessing(Game game, Turn turn, GamePlayer player, Card card,
      boolean hasSilver) {
    constructNormalPlay(game, turn, player, card, hasSilver);
    ArrayList<Card> otherCards = new ArrayList<>();
    switch (card.getType()) {
      case MILITIA:
        break;
      case MINE:
        break;
      case REMODEL:
        break;
      default:
        break;
    }
  }

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

  private String whoWins(Game game, GamePlayer player1, GamePlayer player2) {
    if (calculateVictoryPoints(player1) > calculateVictoryPoints(player2)) {
      return player1.getDisplayName();
    } else if (calculateVictoryPoints(player2) > calculateVictoryPoints(player1)) {
      return player2.getDisplayName();
    } else {
      return "Tie";
    }
  }

  private GamePlayer whoIsActive() {
    return gamePlayerRepository.getByGamePlayerState(GamePlayerState.ACTIVE);
  }

  private GamePlayer whoIsPassive() {
    return gamePlayerRepository.getByGamePlayerState(GamePlayerState.PASSIVE);
  }


}

