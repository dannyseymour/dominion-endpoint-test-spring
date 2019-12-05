package edu.cnm.deepdive.dominionendpointtestspring;

import edu.cnm.deepdive.dominionendpointtestspring.state.GameState;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
@EnableWebSecurity
@EnableResourceServer
public class DominionEndpointTestSpringApplication extends ResourceServerConfigurerAdapter {
  @Value("${oauth.clientId}")
  private String clientId;
  public static void main(String[] args) throws IOException {
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




  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId(clientId);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeRequests().anyRequest().authenticated();
   // http.authorizeRequests().anyRequest().hasRole("USER");
  }
}
