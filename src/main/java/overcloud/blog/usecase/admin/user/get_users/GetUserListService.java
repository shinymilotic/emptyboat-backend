package overcloud.blog.usecase.admin.user.get_users;

public interface GetUserListService {
    UserListResponse getUsers(int pageNumber, int itemsPerPage);
}
