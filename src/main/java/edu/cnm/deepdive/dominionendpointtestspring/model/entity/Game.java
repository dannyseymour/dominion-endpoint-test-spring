package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameEvents;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.lang.NonNull;
import org.springframework.statemachine.StateMachineContext;


/**
 * The game is responsible for the following: creating a new game keeping track of players keep
 * track of stacks determining end of game
 */
@Entity
@NoArgsConstructor
public class Game extends AbstractPersistable<Long> implements ContextEntity<GameStates, GameEvents, Long>{

  /**
   * Creates the primary Game Id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @NonNull
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date created;


  /**
   * Returns a list of stacks. This list documents all of the stacks available to the players. It
   * will be updated throughout the game as players pull cards for the stacks.
   */


  /**
   * Returns a list of Players. This allows for keeping track of players, turns, and points
   * throughout the game.
   */

  private String player1Name;
  private String player2Name;

  private ArrayList<Player> players;
  //TODO use google auth to get players

  @JsonIgnore
  StateMachineContext<GameStates, GameEvents> stateMachineContext;

  @Enumerated(EnumType.STRING)
  GameStates currentState;


  private int currentTurn;



  /**
   * Gets players.
   *
   * @return the players
   */


  /**
   * Sets players.
   *
   * @param players the players
   */

  /**
   * Gets id.
   *
   * @return the id
   */
  public Long getId() {
    return id;
  }


  @Override
  public StateMachineContext<GameStates, GameEvents> getStateMachineContext() {
    return this.stateMachineContext;
  }

  @Override
  public void setStateMachineContext(StateMachineContext<GameStates, GameEvents> context) {
      this.stateMachineContext = context;
  }


  public GameStates getCurrentState() {
    return currentState;
  }

  public void setCurrentState(GameStates currentState) {
    this.currentState = currentState;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public String getPlayer1Name() {
    return player1Name;
  }

  public void setPlayer1Name(String player1Name) {
    this.player1Name = player1Name;
  }

  public String getPlayer2Name() {
    return player2Name;
  }

  public void setPlayer2Name(String player2Name) {
    this.player2Name = player2Name;
  }

  public int getCurrentTurn() {
    return currentTurn;
  }

  public void setCurrentTurn(int currentTurn) {
    this.currentTurn = currentTurn;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public void setPlayers(
      ArrayList<Player> players) {
    this.players = players;
  }

}
