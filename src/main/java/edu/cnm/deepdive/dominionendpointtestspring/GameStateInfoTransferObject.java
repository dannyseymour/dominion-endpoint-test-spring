package edu.cnm.deepdive.dominionendpointtestspring;

import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card;
import java.util.HashMap;
import java.util.List;

public class GameStateInfoTransferObject {
  List<Card> cardsInHand;
  int myVictoryPoints;
  int theirVictoryPoints;
  int myActionsRemaining;
  int myBuysRemaining;
  int myBuyingPower;
  HashMap<String, Integer> stacks;
  List<String> playsMadeLastTurnByOtherPlayer;
  String whatState;
  String message;
  boolean successful;
  boolean wasAttacked;

  public GameStateInfoTransferObject(
      List<Card> cardsInHand, int myVictoryPoints, int theirVictoryPoints, int myActionsRemaining,
      int myBuysRemaining, int myBuyingPower,
     HashMap<String, Integer> stacks,
      List<String> playsMadeLastTurnByOtherPlayer, String whatState, boolean wasAttacked) {
    this.cardsInHand = cardsInHand;
    this.myVictoryPoints = myVictoryPoints;
    this.theirVictoryPoints = theirVictoryPoints;
    this.myActionsRemaining = myActionsRemaining;
    this.myBuysRemaining = myBuysRemaining;
    this.myBuyingPower = myBuyingPower;
    this.stacks = stacks;
    this.playsMadeLastTurnByOtherPlayer = playsMadeLastTurnByOtherPlayer;
    this.whatState = whatState;
    this.wasAttacked = wasAttacked;
  }

  public GameStateInfoTransferObject(
      List<Card> cardsInHand, int myVictoryPoints, int theirVictoryPoints, int myActionsRemaining,
      int myBuysRemaining, int myBuyingPower,
      HashMap<String, Integer> stacks,
      List<String> playsMadeLastTurnByOtherPlayer, String whatState, boolean wasAttacked, String message,
      boolean successful) {
    this.cardsInHand = cardsInHand;
    this.myVictoryPoints = myVictoryPoints;
    this.theirVictoryPoints = theirVictoryPoints;
    this.myActionsRemaining = myActionsRemaining;
    this.myBuysRemaining = myBuysRemaining;
    this.myBuyingPower = myBuyingPower;
    this.stacks = stacks;
    this.playsMadeLastTurnByOtherPlayer = playsMadeLastTurnByOtherPlayer;
    this.whatState = whatState;
    this.message = message;
    this.successful = successful;
    this.wasAttacked = wasAttacked;
  }

  public List<Card> getCardsInHand() {
    return cardsInHand;
  }

  public void setCardsInHand(
      List<Card> cardsInHand) {
    this.cardsInHand = cardsInHand;
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

  public HashMap<String, Integer> getStacks() {
    return stacks;
  }

  public void setStacks(HashMap<String, Integer> stacks) {
    this.stacks = stacks;
  }

  public List<String> getPlaysMadeLastTurnByOtherPlayer() {
    return playsMadeLastTurnByOtherPlayer;
  }

  public void setPlaysMadeLastTurnByOtherPlayer(
      List<String> playsMadeLastTurnByOtherPlayer) {
    this.playsMadeLastTurnByOtherPlayer = playsMadeLastTurnByOtherPlayer;
  }

  public String getWhatState() {
    return whatState;
  }

  public void setWhatState(String whatState) {
    this.whatState = whatState;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isSuccessful() {
    return successful;
  }

  public void setSuccessful(boolean successful) {
    this.successful = successful;
  }

  public boolean isWasAttacked() {
    return wasAttacked;
  }

  public void setWasAttacked(boolean wasAttacked) {
    this.wasAttacked = wasAttacked;
  }
}
