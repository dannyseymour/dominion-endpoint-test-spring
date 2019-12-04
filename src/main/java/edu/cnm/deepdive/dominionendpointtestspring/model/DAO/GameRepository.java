package edu.cnm.deepdive.dominionendpointtestspring.model.dao;


import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;

import edu.cnm.deepdive.dominionendpointtestspring.state.GameState;
import java.util.ArrayList;

import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {


}