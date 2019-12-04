package edu.cnm.deepdive.dominionendpointtestspring.model.dao;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<Game, Long> {

  Optional<List<Card>> getAllByPlayerAnd



}