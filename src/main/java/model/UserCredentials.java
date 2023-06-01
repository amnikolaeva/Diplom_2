package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCredentials {

    private String email;
    private String password;
    private String name;

    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword(), user.getName());
    }
}
