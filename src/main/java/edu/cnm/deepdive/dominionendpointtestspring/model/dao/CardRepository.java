package edu.cnm.deepdive.dominionendpointtestspring.model.dao;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<Game, Long> {





}