package overcloud.blog.article;

import overcloud.blog.application.user.core.UserEntity;

import java.util.UUID;

public class UserEntityFactory {
    public static UserEntity createArticleTestNormal() {
        return UserEntity.builder()
                .id(UUID.fromString("e7e861df-2c3f-4304-a2b0-3b98c1ba16c8"))
                .email("trungtin.mai1412@gmail.com")
                .username("thepianist00")
                .bio("A pragmatddsdsadsaic programmerss")
                .image("https://avatars.githubusercontent.com/u/19252712?s=100&v=100")
                .password("$2a$10$ba45PLemGgZxRXAjHkyuRuuHE0o4dmrKFcyW5a")
                .build();
    }
}
