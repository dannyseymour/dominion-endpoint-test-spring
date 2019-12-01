package edu.cnm.deepdive.dominionendpointtestspring.model.entity;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.springframework.lang.NonNull;


/**
 * Keeps a table consisting of every turn taken by both players, Organized by a Turn ID and a Player
 * ID. For example, Turn 1-Player 1 Is player 1's first turn, while Turn 1- Player 2 is Player 2's
 * First Turn. When HasFinished updates to True, the game's state resets for the next turn.
 */
@Entity
@Table
public class Turn implements Serializable {

  public Turn(Game game, Player player) {
    //this.playerId = player.getId();
    this.buysRemaining = 1;
    this.actionsRemaining = 1;
   // this.gameId = game.getId();
  }

  //private CardRepository cardRepository;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "turn_id", updatable = false, nullable = false)
  private int turnId;

  /**
   * Foreign Key playerId, refers to the player who took this turn.
   */
/**
  @NonNull
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "game_id", nullable = false, updatable = false)
  private int gameId;
*/
  @Column(name="buying_power")
  private int buyingPower;
/**
  @NonNull
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "player_id", nullable = false, updatable = false)
  private long playerId;
  */
/**
  @OneToMany(mappedBy = "turn_id", cascade = CascadeType.ALL,
      orphanRemoval = true)
  @OrderBy("play_id ASC")
  private List<Play> plays = new LinkedList<>();

**/
/**
  public long getPlayerId() {
    return playerId;
  }

  public void setPlayerId(long playerId) {
    this.playerId = playerId;
  }
*/
  /**
   * Buys Remaining- a counter that iterates down to zero. When it returns zero, a method is
   * triggered to move to the next phase (discard).
   */

  @Column(name="buys_remaining")
  private int buysRemaining;

  @Column(name="did_attack")
  private boolean didAttack;

  /**
   * Actions Remaining- a counter that iterates down to zero. When it returns zero, a method is
   * triggered to move to the next phase (buy).
   */
  @Column(name="actions_remaining")
  private int actionsRemaining;

  /**
   * Includes a list of plays per turn.
   */


/**
  public List<Play> getPlays() {
    return plays;
  }
*/



  public int getTurnId() {
    return turnId;
  }





  public void setTurnId(int turnId) {
    this.turnId = turnId;
  }
/**
  public int getGame() {
    return gameId;
  }

  public void setGame(int gameId) {
    this.gameId = gameId;
  }
*/
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
/**
  public void setPlays(List<Play> plays) {
    this.plays = plays;
  }
*/
/**
  public int getGameId() {
    return gameId;
  }
*/

}
