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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

  public Game(Player player1, Player player2) {

    this.players.add(player1);
    this.players.add(player2);
  }
  public Game(Player player1) {
    this.players.add(player1);

  }
  public Game(String player1UID) {
    this.player1UID = player1UID;
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


@Column(name="stacks")
HashMap<String, Integer> stack = new HashMap<>();

@Column(name="players")
ArrayList<Player> players = new ArrayList<>();



  /**
   * Returns a list of stacks. This list documents all of the stacks available to the players. It
   * will be updated throughout the game as players pull cards for the stacks.
   */


  /**
   * Returns a list of Players. This allows for keeping track of players, turns, and points
   * throughout the game.
   */

  @Column(name="player_1_uid")
  private String player1UID;
  @Column(name="player_2_uid")
  private String player2UID;




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

  public HashMap<String, Integer> getStack() {
    return stack;
  }

  public void setStack(HashMap<String, Integer> stack) {
    this.stack = stack;
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

  public void join(Player player2){
    this.players.add(player2);
  }

  public int getCurrentTurn() {
    return currentTurn;
  }

  public void setCurrentTurn(int currentTurn) {
    this.currentTurn = currentTurn;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPlayer1UID() {
    return player1UID;
  }

  public void setPlayer1UID(String player1UID) {
    this.player1UID = player1UID;
  }

  public String getPlayer2UID() {
    return player2UID;
  }

  public void setPlayer2UID(String player2UID) {
    this.player2UID = player2UID;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public void setPlayers(
      ArrayList<Player> players) {
    this.players = players;
  }
}
