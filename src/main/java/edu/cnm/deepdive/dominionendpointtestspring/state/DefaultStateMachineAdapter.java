package edu.cnm.deepdive.dominionendpointtestspring.state;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;

@RequiredArgsConstructor
public class DefaultStateMachineAdapter<S, E, T extends Game> {

  final StateMachineFactory<S, E> stateMachineFactory;

  final StateMachinePersister<S, E, Game> persister;

  @SneakyThrows
  public StateMachine<S, E> restore(Game contextObject) throws Exception {
    StateMachine<S, E> stateMachine = stateMachineFactory.getStateMachine();
    return persister.restore(stateMachine,  contextObject);
  }

  @SneakyThrows
  public void persist(StateMachine<S, E> stateMachine, T order) throws Exception {
    persister.persist(stateMachine, order);
  }

  public StateMachine<S, E> create() {
    StateMachine<S, E> stateMachine = stateMachineFactory.getStateMachine();
    stateMachine.start();
    return stateMachine;
  }

}