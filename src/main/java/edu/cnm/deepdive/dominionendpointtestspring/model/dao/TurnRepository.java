package edu.cnm.deepdive.dominionendpointtestspring.model.dao;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnRepository {

  Optional<List<Turn>> getAllByOrderByTurnIdDesc();

  Optional<List<Turn>> getAllByGameOrderByTurnIdDesc(Game game);

  void save(Turn turn);
}
