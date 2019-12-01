package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

/**
import edu.cnm.deepdive.dominionendpointtestspring.state.DefaultStateMachineAdapter;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameEvents;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;

import java.io.Serializable;

import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import org.springframework.hateoas.server.EntityLinks;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.transition.Transition;

@RequiredArgsConstructor
@Slf4j
public class ContextObjectResourceProcessor<S, E, T extends ContextEntity<S, E, ? extends Serializable>> implements ResourceProcessor<EntityModel<T>> {

  private static final KebabCaseStrategy TO_KEBAB = new KebabCaseStrategy();

  final EntityLinks entityLinks;

  final DefaultStateMachineAdapter<GameStates, GameEvents, Long> stateMachineAdapter;


  public EntityModel<T> process(EntityModel<T> resource) {
    ContextEntity<S, E, ? extends Serializable> contextObject = resource.getContent();
    StateMachine<S, E> stateMachine = null;
    try {
      stateMachine = (StateMachine<S, E>) stateMachineAdapter.restore((Game) contextObject);
    } catch (Exception e) {
      e.printStackTrace();
    }

    assert stateMachine != null;
    for (Transition<S, E> transition : stateMachine.getTransitions()) {
      if (stateMachine.getState().getId().equals(transition.getSource().getId())) {
        E event = transition.getTrigger().getEvent();
        log.info("Found transition triggered by event: {}", event);
        resource.add(eventLink(contextObject, event, TO_KEBAB.translate(event.toString())));
      }
    }

    return resource;
  }

  private Link eventLink(ContextEntity<S, E, ? extends Serializable> contextObject, E event,
      String rel) {
    return entityLinks.linkFor(contextObject).slash("receive").slash(event)
        .withRel(rel);
  }


  // copied from Jackson.PropertyNamingStrategy
  static class KebabCaseStrategy {

    public String translate(String input) {
      if (input == null)
        return input; // garbage in, garbage out
      int length = input.length();
      if (length == 0) {
        return input;
      }

      StringBuilder result = new StringBuilder(length + (length >> 1));

      int upperCount = 0;

      for (int i = 0; i < length; ++i) {
        char ch = input.charAt(i);
        char lc = Character.toLowerCase(ch);

        if (lc == ch) { // lower-case letter means we can get new word
          // but need to check for multi-letter upper-case (acronym), where assumption
          // is that the last upper-case char is start of a new word
          if (upperCount > 1) {
            // so insert hyphen before the last character now
            result.insert(result.length() - 1, '-');
          }
          upperCount = 0;
        } else {
          // Otherwise starts new word, unless beginning of string
          if ((upperCount == 0) && (i > 0)) {
            result.append('-');
          }
          ++upperCount;
        }
        result.append(lc);
      }
      return result.toString();
    }
  }
}

*/