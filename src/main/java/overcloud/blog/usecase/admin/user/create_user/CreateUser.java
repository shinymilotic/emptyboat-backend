package overcloud.blog.usecase.admin.user.create_user;

import java.io.IOException;

public interface CreateUser {
    Void createUser(CreateUserRequest request) throws IOException;
}
