package overcloud.blog.usecase.admin.user.create_user;

import java.io.IOException;

public interface AdminCreateUser {
    Void adminCreateUser(AdminCreateUserRequest request) throws IOException;
}
