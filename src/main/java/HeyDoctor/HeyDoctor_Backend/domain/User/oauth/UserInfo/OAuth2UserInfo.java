package HeyDoctor.HeyDoctor_Backend.domain.User.oauth.UserInfo;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
}