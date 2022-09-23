package anthill.Anthill.api.service;

public interface JwtService {
    String create(String key, String data, String subject);
    boolean isUsable(String jwt);
    byte[] generateKey();
}
