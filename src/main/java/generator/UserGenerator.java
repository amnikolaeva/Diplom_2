package generator;

import com.github.javafaker.Faker;
import model.User;

import java.util.Locale;

public class UserGenerator {

    public static User getRandom() {
        Faker faker = new Faker(Locale.forLanguageTag("ru"));
        User user = User.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .name(faker.name().firstName())
                .build();
        return user;
    }
}
