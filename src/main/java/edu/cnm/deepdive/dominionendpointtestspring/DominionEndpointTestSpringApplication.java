package edu.cnm.deepdive.dominionendpointtestspring;

import edu.cnm.deepdive.dominionendpointtestspring.state.GameEvents;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
public class DominionEndpointTestSpringApplication {
  private StateMachine<GameStates, GameEvents> stateMachine;
  public static void main(String[] args) {
    SpringApplication.run(DominionEndpointTestSpringApplication.class, args);

  }

/**
@Bean
  public StateMachine<ApplicationReviewStates, ApplicationReviewEvents> buildMachine() throws Exception {
    Builder<ApplicationReviewStates, ApplicationReviewEvents> builder = StateMachineBuilder.builder();

    builder.configureStates()
        .withStates()
        .initial(ApplicationReviewStates.PEER_REVIEW)
        .states(EnumSet.allOf(ApplicationReviewStates.class));

    builder.configureTransitions()
        .withExternal()
        .source(ApplicationReviewStates.PEER_REVIEW).target(ApplicationReviewStates.APPROVED)
        .event(ApplicationReviewEvents.APPROVE)
        .and()
        .withExternal()
        .source(ApplicationReviewStates.APPROVED).target(ApplicationReviewStates.REJECTED)
        .event(ApplicationReviewEvents.REJECT);

    return builder.build();
  }
*/
}
