package edu.cnm.deepdive.dominionendpointtestspring.state;

import java.util.EnumSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class SimpleEnumStateMachineConfiguration extends StateMachineConfigurerAdapter<GameStates, GameEvents> {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Override
  public void configure(StateMachineConfigurationConfigurer<GameStates, GameEvents> config)
      throws Exception {
    config
        .withConfiguration()
        .autoStartup(true)
        .beanFactory(new StaticListableBeanFactory())
        .taskExecutor(new SyncTaskExecutor())
        .taskScheduler(new ConcurrentTaskScheduler())
        .listener(new StateMachineListener<GameStates, GameEvents>());
  }

  @Override
  public void configure(StateMachineStateConfigurer<GameStates, GameEvents> states) throws Exception {
    states
        .withStates()
        .initial(GameStates.INITIAL)
        .end(GameStates.END_GAME)
        .states(EnumSet.allOf(GameStates.class));

  }

  @Override
  public void configure(StateMachineTransitionConfigurer<GameStates, GameEvents> transitions) throws Exception {
    transitions.withExternal()
        .source(GameStates.INITIAL).target(GameStates.PLAYER_1_DISCARDING).event(GameEvents.START_GAME)
        .and().withExternal()
        .source(GameStates.PLAYER_1_DISCARDING).target(GameStates.PLAYER_1_ACTION).event(GameEvents.PLAYER_1_END_DISCARDS)
        .and().withExternal()
        .source(GameStates.PLAYER_1_ACTION).target(GameStates.PLAYER_1_BUYING).event(GameEvents.PLAYER_1_END_ACTIONS)
        .and().withExternal()
        .source(GameStates.PLAYER_1_BUYING).target(GameStates.PLAYER_2_DISCARDING).event(GameEvents.PLAYER_1_END_BUYS)
        .and().withExternal()
        .source(GameStates.PLAYER_2_DISCARDING).target(GameStates.PLAYER_2_ACTION).event(GameEvents.PLAYER_2_END_DISCARDS)
        .and().withExternal()
        .source(GameStates.PLAYER_2_ACTION).target(GameStates.PLAYER_2_BUYING).event(GameEvents.PLAYER_2_END_ACTIONS)
        .and().withExternal()
        .source(GameStates.PLAYER_2_BUYING).target(GameStates.PLAYER_1_DISCARDING).event(GameEvents.PLAYER_2_END_BUYS)
        .and().withExternal()
        .source(GameStates.PLAYER_2_BUYING).target(GameStates.END_GAME).event(GameEvents.END_GAME)
     .and().withExternal()
        .source(GameStates.PLAYER_1_BUYING).target(GameStates.END_GAME).event(GameEvents.END_GAME);
  }
}