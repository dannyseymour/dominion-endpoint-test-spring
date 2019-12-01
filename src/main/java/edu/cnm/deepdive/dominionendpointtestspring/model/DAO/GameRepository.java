package edu.cnm.deepdive.dominionendpointtestspring.model.DAO;


import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

  Game getGameById(long id);

  ArrayList<Game> getAllByOrderByIdDesc();
}