package overcloud.blog.usecase.user.admin_create_user;

import java.io.IOException;

public interface IAdminCreateUser {
    Void adminCreateUser(AdminCreateUserRequest request) throws IOException;
}
