package edu.cnm.deepdive.dominionendpointtestspring.model.DAO;


import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;

import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;
import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates.*;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

  Game getGameById(long id);

  ArrayList<Game> getAllByOrderByIdDesc();

  Game getFirstByCurrentState(GameStates gameStates);

}