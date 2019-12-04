package edu.cnm.deepdive.dominionendpointtestspring.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Response entity for game state- catches response coming back from server.
 */
public class GameStateInfo implements Serializable {

  public GameStateInfo(Integer gameId,
      List<String> cardsInHand, int myVictoryPoints, int theirVictoryPoints, int myActionsRemaining,
      int myBuysRemaining, int myBuyingPower, boolean wasAttacked,
      LinkedHashMap<String, Integer> stacks,
      List<String> playsMadeLastTurnByOtherPlayer,
      PhaseState whatStateAmIIn, boolean showSelectCard, boolean debugging) {
    this.gameId = gameId;
    this.cardsInHand = cardsInHand;
    this.myVictoryPoints = myVictoryPoints;
    this.theirVictoryPoints = theirVictoryPoints;
    this.myActionsRemaining = myActionsRemaining;
    this.myBuysRemaining = myBuysRemaining;
    this.myBuyingPower = myBuyingPower;
    this.wasAttacked = wasAttacked;
    this.stacks = stacks;
    this.playsMadeLastTurnByOtherPlayer = playsMadeLastTurnByOtherPlayer;
    this.whatStateAmIIn = whatStateAmIIn;
    this.showSelectCard = showSelectCard;
    this.debugging = debugging;
  }

  Integer gameId;

  List<String> cardsInHand;


  int myVictoryPoints;


  int theirVictoryPoints;


  int myActionsRemaining;


  int myBuysRemaining;


  int myBuyingPower;


  boolean wasAttacked;


  LinkedHashMap<String, Integer> stacks;


  List<String> playsMadeLastTurnByOtherPlayer;



  PhaseState whatStateAmIIn;

  boolean showSelectCard;

  public boolean debugging = true;

  public List<String> getCardsInHand() {
    return cardsInHand;
  }


  public int getMyVictoryPoints() {
    return myVictoryPoints;
  }

  public void setMyVictoryPoints(int myVictoryPoints) {
    this.myVictoryPoints = myVictoryPoints;
  }

  public int getTheirVictoryPoints() {
    return theirVictoryPoints;
  }

  public void setTheirVictoryPoints(int theirVictoryPoints) {
    this.theirVictoryPoints = theirVictoryPoints;
  }

  public int getMyActionsRemaining() {
    return myActionsRemaining;
  }

  public void setMyActionsRemaining(int myActionsRemaining) {
    this.myActionsRemaining = myActionsRemaining;
  }

  public int getMyBuysRemaining() {
    return myBuysRemaining;
  }

  public void setMyBuysRemaining(int myBuysRemaining) {
    this.myBuysRemaining = myBuysRemaining;
  }

  public int getMyBuyingPower() {
    return myBuyingPower;
  }

  public void setMyBuyingPower(int myBuyingPower) {
    this.myBuyingPower = myBuyingPower;
  }

  public LinkedHashMap<String, Integer> getStacks() {
    return stacks;
  }

  public String[] getStackStrings() {
    String[] stackStrings = new String[stacks.size()];
    String[] keysStrings = stacks.keySet().toArray(new String[stacks.size()]);
    for (int i = 0; i < keysStrings.length; i++) {
      stackStrings[i] = keysStrings[i].toLowerCase();
    }
    return stackStrings;
  }

  public void setStacks(LinkedHashMap<String, Integer> stacks) {
    this.stacks = stacks;
  }

  public List<String> getPlaysMadeLastTurnByOtherPlayer() {
    return playsMadeLastTurnByOtherPlayer;
  }

  public void setPlaysMadeLastTurnByOtherPlayer(
      List<String> playsMadeLastTurnByOtherPlayer) {
    this.playsMadeLastTurnByOtherPlayer = playsMadeLastTurnByOtherPlayer;
  }

  public PhaseState getWhatStateAmIIn() {
    return whatStateAmIIn;
  }

  public void setWhatStateAmIIn(PhaseState whatStateAmIIn) {
    this.whatStateAmIIn = whatStateAmIIn;
  }

  public boolean isShowSelectCard() {
    return showSelectCard;
  }

  public void setShowSelectCard(boolean showSelectCard) {
    this.showSelectCard = showSelectCard;
  }

  @Override
  public String toString() {
    StringBuilder gameStateInfoString = new StringBuilder();
    gameStateInfoString.append(gameId.toString());
    gameStateInfoString.append("Cards in hand: \n");
    for (int i = 0; i < getCardsInHand().size(); i++) {
      gameStateInfoString.append("Card [" + i + "]: " + getCardsInHand().get(i) + "\n");
    }
    HashMap<String, Integer> stacks = getStacks();
    for (String stackName : stacks.keySet()) {
      gameStateInfoString.append("Stack " + stackName + ": " + stacks.get(stackName) + "\n");
    }
    gameStateInfoString.append("My Victory Points: " + getMyVictoryPoints() + "\n");
    gameStateInfoString.append("Their Victory Points: " + getTheirVictoryPoints() + "\n");
    gameStateInfoString.append("Actions Remaining: " + getMyActionsRemaining() + "\n");
    gameStateInfoString.append("Buys Remaining: " + getMyBuysRemaining() + "\n");
    gameStateInfoString.append("Buying Power: " + getMyBuyingPower() + "\n");
    List<String> otherPlayerTurn = getPlaysMadeLastTurnByOtherPlayer();
    for (int i = 0; i < otherPlayerTurn.size(); i++) {
      gameStateInfoString.append("Play [" + i + "]: " + otherPlayerTurn.get(i) + "\n");
    }
    gameStateInfoString.append("Current State: " + getWhatStateAmIIn() + "\n");
    return gameStateInfoString.toString();
  }

  public Integer getGameId() {
    return gameId;
  }

  public void setGameId(Integer gameId) {
    this.gameId = gameId;
  }

  public boolean isWasAttacked() {
    return wasAttacked;
  }

  public void setWasAttacked(boolean wasAttacked) {
    this.wasAttacked = wasAttacked;
  }

  public boolean isDebugging() {
    return debugging;
  }

  public void setDebugging(boolean debugging) {
    this.debugging = debugging;
  }
}
