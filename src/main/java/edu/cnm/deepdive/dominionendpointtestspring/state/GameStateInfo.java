package edu.cnm.deepdive.dominionendpointtestspring.state;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;

public class GameStateInfo {

  private Game game;
  private Turn turn;
  private Player requestingPlayer;
  private Player activePlayer;
  private GameState gameState;

  public GameStateInfo(Game game, Turn turn,
      Player requestingPlayer,
      Player activePlayer, GameState gameState) {
    this.game = game;
    this.turn = turn;
    this.requestingPlayer = requestingPlayer;
    this.activePlayer = activePlayer;
    this.gameState = gameState;
  }
  public GameStateInfo(Game game, Turn turn,
      Player player, GameState gameState) {
    this.game = game;
    this.turn = turn;
    this.requestingPlayer = player;
    this.activePlayer = player;
    this.gameState = gameState;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public Turn getTurn() {
    return turn;
  }

  public void setTurn(Turn turn) {
    this.turn = turn;
  }

  public Player getRequestingPlayer() {
    return requestingPlayer;
  }

  public void setRequestingPlayer(
      Player requestingPlayer) {
    this.requestingPlayer = requestingPlayer;
  }

  public Player getActivePlayer() {
    return activePlayer;
  }

  public void setActivePlayer(Player activePlayer) {
    this.activePlayer = activePlayer;
  }

  public GameState getGameState() {
    return gameState;
  }

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }
}
