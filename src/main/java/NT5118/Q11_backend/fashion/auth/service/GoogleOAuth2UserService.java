package NT5118.Q11_backend.fashion.auth.service;

import NT5118.Q11_backend.fashion.auth.dto.GoogleOAuth2UserInfo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GoogleOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final AuthService authService;

    public GoogleOAuth2UserService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        String id = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        GoogleOAuth2UserInfo info = new GoogleOAuth2UserInfo(id, email, name, picture);

        // Register or update user in our system
        NT5118.Q11_backend.fashion.user.model.User user = authService.registerOAuthUser(info);

        // Return a DefaultOAuth2User with the user's authorities and attributes
        return new DefaultOAuth2User(java.util.Collections.emptyList(), attributes, "sub");
    }
}

