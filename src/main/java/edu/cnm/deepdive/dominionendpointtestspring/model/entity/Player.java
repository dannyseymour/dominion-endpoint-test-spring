package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


/**
 * The type Player.
 */
@Entity
@Table
public class Player implements Serializable {

  public Player(String uid) {

this.uid = uid;
  }

  @Id
  @Column(name="player_id", updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "user_name", nullable = true, updatable = false)
  private String userName;

  @Column(name = "player_score")
  private int playerScore;

  @Column(name = "cards_in_hand", nullable = true)
  private ArrayList<Card> playerHand;
  @Column(name = "cards_in_discard", nullable = true)
  private ArrayList<Card> playerDiscard;
  @Column(name = "cards_in_draw_pile", nullable = true)
  private ArrayList<Card> playerDrawPile;

  @Column(name="firebase_fcm_registration_token")
 private  String playerFCMRegistrationToken;

  @Column(name="Uid")
  private String uid;

  public ArrayList<Card> getPlayerDiscard() {
    return playerDiscard;
  }

  public void setPlayerDiscard(
      ArrayList<Card> playerDiscard) {
    this.playerDiscard = playerDiscard;
  }

  public String getPlayerFCMRegistrationToken() {
    return playerFCMRegistrationToken;
  }

  public void setPlayerFCMRegistrationToken(String playerFCMRegistrationToken) {
    this.playerFCMRegistrationToken = playerFCMRegistrationToken;
  }

  public ArrayList<Card> getPlayerDrawPile() {
    return playerDrawPile;
  }

  public void setPlayerDrawPile(
      ArrayList<Card> playerDrawPile) {
    this.playerDrawPile = playerDrawPile;
  }
/**
  @OneToMany(mappedBy = "player", cascade = CascadeType.ALL,
      orphanRemoval = true)
  @OrderBy("game_id ASC")
  private ArrayList<Game> games = new ArrayList<>();
*/
  @OneToMany(mappedBy = "player", cascade = CascadeType.ALL,
      orphanRemoval = true)
  @OrderBy("turn_id, player_id ASC")
  private List<Turn> turns = new LinkedList<>();

  @OneToMany(mappedBy = "player", cascade = CascadeType.ALL,
      orphanRemoval = true)
  @OrderBy("play_id, player_id ASC")
  private List<Play> plays = new LinkedList<>();

  @Column(name="has_moat")
  private boolean hasMoat;

  /**
   * Sets player score.
   *
   * @param playerScore the player score
   * @OneToMany(mappedBy= "deck", cascade = CascadeType.ALL)private List<Card> deck = new
   * LinkedList<>();
   * @OneToMany(mappedBy= "player", cascade = CascadeType.ALL)private List<Card> discard = new
   * LinkedList<>();
   * @OneToMany(mappedBy= "player", cascade = CascadeType.ALL)private List<Card> hand = new
   * LinkedList<>();
   */

  @Column(name="player_state")
  private String playerState;

  @Column(name="game_order")
  private int gameOrder;


  /**@OneToMany(mappedBy= "deck", cascade = CascadeType.ALL)
  private List<Card> deck = new LinkedList<>();

  @OneToMany(mappedBy= "player", cascade = CascadeType.ALL)
  private List<Card> discard = new LinkedList<>();

  @OneToMany(mappedBy= "player", cascade = CascadeType.ALL)
  private List<Card> hand = new LinkedList<>();
*/
  public void setPlayerScore(int playerScore) {
    this.playerScore = playerScore;
  }

  /**
   * Sets whose turn.
   *
   * @param whoseTurn the whose turn
   */


  public void setId(Long id) {
    this.id = id;
  }





  public String getPlayerState() {
    return playerState;
  }

  public void setPlayerState(PlayerState playerState) {
    this.playerState = playerState.toString();
  }

  /** public void setDeck(List<Card> deck) {
    this.deck = deck;
  }

  public void setDiscard(List<Card> discard) {
    this.discard = discard;
  }

  public void setHand(List<Card> hand) {
    this.hand = hand;
  }
*/
  public Long getId() {
    return id;
  }

  /**
   * Gets game.
   *
   * @return the game
   */

  public boolean isHasMoat() {
    return hasMoat;
  }

  public void setHasMoat(boolean hasMoat) {
    this.hasMoat = hasMoat;
  }

  /**
   * Gets player score.
   *
   * @return the player score
   */
  public int getPlayerScore() {
    return playerScore;
  }

  /**
   * Gets whose turn.
   *
   * @return the whose turn
   */


  /**
   * public List<Card> getDeck() { return deck; } public List<Card> getDiscard() { return discard; }
   * public List<Card> getHand() { return hand; }
   *
   * @return the num action
   */

  /**
   * Sets num action.
   *
   * @param numAction the num action
   */


  /**
   * Gets player's number of buys.
   *
   * @return the player's number of buys.
   */

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
/**
  public ArrayList<Game> getGames() {
    return games;
  }

  public void setGames(
      ArrayList<Game> games) {
    this.games = games;
  }
*/
  public List<Turn> getTurns() {
    return turns;
  }

  public void setTurns(
      List<Turn> turns) {
    this.turns = turns;
  }

  public List<Play> getPlays() {
    return plays;
  }

  public void setPlays(
      List<Play> plays) {
    this.plays = plays;
  }

  public void setPlayerState(String playerState) {
    this.playerState = playerState;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  /**
   * Sets num buy.
   *
   * @param numBuy the num buy
   */


  public ArrayList<Card> getPlayerHand() {
    return playerHand;
  }

  public void setPlayerHand(ArrayList<Card> playerHand) {
    this.playerHand = playerHand;
  }
 public enum PlayerState{
   MY_TURN,
   WATCHING,
   BUYING,
   DISCARDING,
   MILITIA_RESPONSE,
   ACTION;

 }

//  public static class Hand {
//
//  }
  /**
  private void shuffleDrawPile(){
    //TODO
  }
  private void checkDrawPile(){
    if (drawPile.size() ==0){
      //add discard to draw (remove cards from discard, add to drawPile)
      while (discard.size() > 0){
        drawPile.add(discard.remove(0));
      }
      shuffleDrawPile();
    }
  }

  public void drawCard(){
    //takes a card from the players drawPile and adds to hand
    //first remove a card from the drawPile
    //first make sure we have something to draw
    checkDrawPile();
    Card newCard = drawPile.remove(0);
    hand.add(newCard);
  }
*/

  public int getGameOrder() {
    return gameOrder;
  }

  public void setGameOrder(int gameOrder) {
    this.gameOrder = gameOrder;
  }
}