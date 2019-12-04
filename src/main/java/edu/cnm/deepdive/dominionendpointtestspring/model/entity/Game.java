package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatGame;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameState;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatGameGamePlayer;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatPlayer;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

//TODO Remove this later:

//game-id
//State
//Current Player
//Current Phase

/**
 * The game is responsible for the following: creating a new game keeping track of players keep
 * track of stacks determining end of game
 */
@Entity

@Table
public class Game implements FlatGame {

  /**
   * Creates the primary Game Id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "game_id")
  private Long id;

  @NonNull
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date created;

  @NonNull
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date updated;

  @Column(name = "current_state")
  GameState currentState = GameState.WAITING;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "current_player_id")
  @JsonSerialize(as= FlatPlayer.class)
  private Player currentPlayer;

  @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @OrderBy(value = "id")
  @JsonSerialize(contentAs = FlatGameGamePlayer.class)
  private List<GamePlayer> gamePlayers = new LinkedList<>();

  @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @OrderBy(value = "id")
  @JsonIgnore
  private List<Turn> turns  = new LinkedList<>();

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public GameState getCurrentState() {
    return currentState;
  }

  public void setCurrentState(GameState currentState) {
    this.currentState = currentState;
  }

  @Override
  public Date getCreated() {
    return created;
  }

  @Override
  @NonNull
  public Date getUpdated() {
    return updated;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(
      Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public List<GamePlayer> getGamePlayers() {
    return gamePlayers;
  }

  public List<Turn> getTurns() {
    return turns;
  }

  public Turn getLastTurn(){
    return (!turns.isEmpty()) ? turns.get(turns.size()-1):null;
  }
}
