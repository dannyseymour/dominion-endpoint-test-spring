package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameEvents;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
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

@Table
public class Game extends AbstractPersistable<Integer> implements Serializable, ContextEntity<GameStates, GameEvents, Integer>{

  public Game() {
  }

  public Game(String player1Name, String player2Name) {
    this.player1Name = player1Name;
    this.player2Name = player2Name;
  }

  /**
   * Creates the primary Game Id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="id")
  private int id;

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

  @Column(name="player_1_name")
  private String player1Name;
  @Column(name="player_2_name")
  private String player2Name;




  @JsonIgnore
      @Column(name="state_machine")
  StateMachineContext<GameStates, GameEvents> stateMachineContext;

  @Enumerated(EnumType.STRING)
      @Column(name="current_state")
  GameStates currentState;

  @Column(name="current_turn")
  private int currentTurn;

  public HashMap<String, Integer> getStacks() {
    HashMap<String, Integer> stack = new HashMap<>();
    stack.put("Bronze", 10);
    stack.put("Silver", 8);
    stack.put("Gold", 7);
    stack.put("Estate", 10);
    stack.put("Duchy", 12);
    stack.put("Province", 15);
    stack.put("Cellar", 9);
    stack.put("Moat", 10);
    stack.put("Village", 5);
    stack.put("Workshop", 3);
    stack.put("Smithy", 2);
    stack.put("Remodel", 15);
    stack.put("Militia", 9);
    stack.put("Market", 8);
    stack.put("Mine", 2);
    stack.put("Merchant", 3);
    stack.put("Trash", 0);
    return stack;
  }


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
  public Integer getId() {
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

}
