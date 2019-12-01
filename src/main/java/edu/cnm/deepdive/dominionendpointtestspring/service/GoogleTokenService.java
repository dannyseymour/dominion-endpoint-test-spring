package edu.cnm.deepdive.dominionendpointtestspring.service;
/**
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import edu.cnm.deepdive.dominionservice.model.entity.Player;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;

@Component
public class GoogleTokenService implements ResourceServerTokenServices {

 // private static final String CLIENT_ID_1 = 183198357284-ve1q5rd7bdo0hfne76mkfatpkc1hdlov.apps.googleusercontent.com;
//  private static final String CLIENT_ID_2 = ;

  private final AccessTokenConverter converter = new DefaultAccessTokenConverter();
  private final String clientId;
  private final PlayerService playerService;

  public GoogleTokenService(@Value("183198357284-ve1q5rd7bdo0hfne76mkfatpkc1hdlov.apps.googleusercontent.com") String clientId,
      PlayerService playerService) {
    this.clientId = clientId;
    this.playerService = playerService;
  }

  @Override
  public OAuth2Authentication loadAuthentication(String token)
      throws AuthenticationException, InvalidTokenException {
    try {
      HttpTransport transport = new NetHttpTransport();
      JacksonFactory jsonFactory = new JacksonFactory();
      GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
          .setAudience(Collections.singletonList(clientId))
          .build();
      GoogleIdToken idToken = verifier.verify(token);
      if (idToken != null) {
        Payload payload = idToken.getPayload();
        Player player = playerService.getOrCreatePlayer(payload.getSubject());
        Collection<GrantedAuthority> grants =
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication base =
            new UsernamePasswordAuthenticationToken(player, token, grants);
        OAuth2Request request = converter.extractAuthentication(payload).getOAuth2Request();
        return new OAuth2Authentication(request, base);
      } else {
        throw new BadCredentialsException(token);
      }
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public OAuth2AccessToken readAccessToken(String s) {
    return null;
  }

}
*/