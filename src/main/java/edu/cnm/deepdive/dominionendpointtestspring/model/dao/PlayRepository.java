package edu.cnm.deepdive.dominionendpointtestspring.model.dao;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Play;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayRepository extends CrudRepository<Play, Integer> {

  Optional<List<Play>> getAllByGameAndPlayerAndTurn(Game game, Player player, Turn turn);

  Optional<List<Play>> getAllByTurn(Turn turn);

  Optional<List<Play>> getAllByGameAndTurn(Game game, Turn previousTurn);

  Optional<List<Play>> getAllByTurnAndType(Turn turn);
}
