package edu.cnm.deepdive.dominionendpointtestspring.entity;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * Keeps a table consisting of every turn taken by both players, Organized by a Turn ID and a Player
 * ID. For example, Turn 1-Player 1 Is player 1's first turn, while Turn 1- Player 2 is Player 2's
 * First Turn. When HasFinished updates to True, the game's state resets for the next turn.
 */

public class Turn implements Serializable {

  public Turn(Game game, Player player) {
    this.playerId = player.getId();
    this.buysRemaining = 1;
    this.actionsRemaining = 1;
    this.game = game;
  }

  //private CardRepository cardRepository;


  private int id;

  /**
   * Foreign Key playerId, refers to the player who took this turn.
   */


  private Game game;


  private int buyingPower;


  private long playerId;

  public long getPlayerId() {
    return playerId;
  }

  public void setPlayerId(long playerId) {
    this.playerId = playerId;
  }

  /**
   * Buys Remaining- a counter that iterates down to zero. When it returns zero, a method is
   * triggered to move to the next phase (discard).
   */

  private int buysRemaining;


  private boolean didAttack;

  /**
   * Actions Remaining- a counter that iterates down to zero. When it returns zero, a method is
   * triggered to move to the next phase (buy).
   */

  private int actionsRemaining;

  /**
   * Includes a list of plays per turn.
   */

  private List<Play> plays = new LinkedList<>();

  public List<Play> getPlays() {
    return plays;
  }


  private TurnState turnState;

  public TurnState getTurnState() {
    return turnState;
  }

  public void setTurnState(TurnState turnState) {
    this.turnState = turnState;
  }

  public int getId() {
    return id;
  }



  public int getBuysRemaining() {
    return buysRemaining;
  }

  public void setBuysRemaining(int buysRemaining) {
    this.buysRemaining = buysRemaining;
  }

  public int getActionsRemaining() {
    return actionsRemaining;
  }

  public void setActionsRemaining(int actionsRemaining) {
    this.actionsRemaining = actionsRemaining;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public boolean isDidAttack() {
    return didAttack;
  }

  public void setDidAttack(boolean didAttack) {
    this.didAttack = didAttack;
  }

  public int getBuyingPower() {
    return buyingPower;
  }

  public void setBuyingPower(int buyingPower) {
    this.buyingPower = buyingPower;
  }

  public void setPlays(List<Play> plays) {
    this.plays = plays;
  }

  public void addGoldIfSilver() {
    //TODO needs implementation
  }


  public enum TurnState {
    ACTING,
    BUYING,
    DISCARDING,
    DRAWING,
    PASSIVE,
    MILITIA;
  }
}
