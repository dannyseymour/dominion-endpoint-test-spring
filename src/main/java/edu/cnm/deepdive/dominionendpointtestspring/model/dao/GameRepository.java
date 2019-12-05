package edu.cnm.deepdive.dominionendpointtestspring.model.dao;


import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.GamePlayer;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameState;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

  Game getGameById(long id);

  ArrayList<Game> getAllByOrderByIdDesc();

  Game getFirstByCurrentState(GameState gameState);

  Optional<Game> findFirstByCurrentState(GameState state);

@Query(value = "SELECT DISTINCT g FROM game g INNER JOIN game_player gp ON gp.game_id = g.game_id WHERE "
    + "g.game_id = :gameId AND gp.player_id = :playerId")
  Optional<Game> findByIdAndPlayer(long gameId, long playerId);


  Game save(Game game);

  Game getFirstByGamePlayers(List<GamePlayer> gamePlayers);
}