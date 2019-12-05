package edu.cnm.deepdive.dominionendpointtestspring.model.dao;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnRepository extends CrudRepository<Turn, Long> {

  Optional<List<Turn>> getAllByOrderByIdDesc();

  Optional<List<Turn>> getAllByGameOrderByIdDesc(Game game);

}
