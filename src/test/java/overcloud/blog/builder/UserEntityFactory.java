package overcloud.blog.builder;

import overcloud.blog.application.user.core.UserEntity;

import java.util.UUID;

public class UserEntityFactory {
    public static UserEntity createArticleTestNormal() {
        UserEntity currentUser = new UserEntity();
        currentUser.setId(UUID.fromString("e7e861df-2c3f-4304-a2b0-3b98c1ba16c8"));
        currentUser.setEmail("trungtin.mai1412@gmail.com");
        currentUser.setUsername("thepianist00");
        currentUser.setBio("A pragmatddsdsadsaic programmerss");
        currentUser.setImage("https://avatars.githubusercontent.com/u/19252712?s=100&v=100");
        currentUser.setPassword("$2a$10$ba45PLemGgZxRXAjHkyuRuuHE0o4dmrKFcyW5a");

        return currentUser;
    }
}
