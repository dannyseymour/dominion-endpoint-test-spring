package edu.cnm.deepdive.dominionendpointtestspring.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;



/**
 * The game is responsible for the following: creating a new game keeping track of players keep
 * track of stacks determining end of game
 */

public class Game implements Serializable {

  /**
   * Creates the primary Game Id.
   */

  private Long id;

  /**
   * Returns a list of stacks. This list documents all of the stacks available to the players. It
   * will be updated throughout the game as players pull cards for the stacks.
   */

  private List<Stack> stacks = new LinkedList<>();

  /**
   * Returns a list of Players. This allows for keeping track of players, turns, and points
   * throughout the game.
   */

  private List<Player> players = new LinkedList<>();

  /**
   * Gets players.
   *
   * @return the players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Sets players.
   *
   * @param players the players
   */
  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * Gets stacks.
   *
   * @return the stacks
   */
  public List<Stack> getStacks() {
    return stacks;
  }

  /**
   * Sets stacks.
   *
   * @param stacks the stacks
   */
  public void setStacks(List<Stack> stacks) {
    this.stacks = stacks;
  }
}
