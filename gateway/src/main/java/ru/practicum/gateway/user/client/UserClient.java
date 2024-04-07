package ru.practicum.gateway.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.client.BaseClient;
import ru.practicum.gateway.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> getAllUser() {
        return get("");
    }

    public ResponseEntity<Object> postItem(UserDto userDto) {
        return post("", userDto);
    }

    public ResponseEntity<Object> getUserById(int userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> patchUser(int id, UserDto userDto) {
        return patch("/" + id, userDto);
    }

    public ResponseEntity<Object> dellUser(int userId) {
        return delete("/" + userId);
    }
}
