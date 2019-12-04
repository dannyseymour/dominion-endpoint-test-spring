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
