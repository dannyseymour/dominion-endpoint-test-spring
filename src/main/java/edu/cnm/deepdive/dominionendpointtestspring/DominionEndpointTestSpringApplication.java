package edu.cnm.deepdive.dominionendpointtestspring;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameEvents;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
public class DominionEndpointTestSpringApplication {
  private StateMachine<GameStates, GameEvents> stateMachine;
  public static void main(String[] args) throws IOException {
    SpringApplication.run(DominionEndpointTestSpringApplication.class, args);

    FileInputStream serviceAccount =
        new FileInputStream("src/dominion-android-testing-firebase-adminsdk-xe8uj-079c4963d3.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://dominion-android-testing.firebaseio.com")
        .build();

    FirebaseApp.initializeApp(options);
   // FileInputStream refreshToken = new FileInputStream("path/to/refreshToken.json");


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
