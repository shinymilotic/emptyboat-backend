package overcloud.blog.controller;

public class ApiConst {

    /* Articles */
    public static final String ARTICLES = "/articles";

    public static final String ARTICLE = "/articles/{id}";

    public static final String ARTICLE_COMMENTS = "/articles/{id}/comments";

    public static final String ARTICLE_ID_COMMENTS_ID = "/articles/{id}/comments/{id}";

    public static final String ARTICLE_ID_FAVORITE = "/articles/{id}/favorite";

    public static final String ROLES_USERNAME = "/roles/{username}";

    public static final String ROLES = "/roles";

    public static final String TAGS = "/tags";

    public static final String USERS = "/users";

    public static final String USERS_LOGIN = "/users/login";

    public static final String USERS_LOGIN_ADMIN = "/users/loginAdmin";

    public static final String USERS_LOGOUT = "/users/logout";

    public static final String FOLLOWERS = "/followers/{userId}";

    public static final String USERS_REFRESHTOKEN = "/users/refreshToken";

    public static final String PROFILES_USERNAME = "/profiles/{username}";

    public static final String USERS_USERNAME_ASSIGNMENT = "/users/{username}/assignment";

    public static final String PROFILES_USERNAME_FOLLOW = "/profiles/{username}/follow";

    public static final String ARTICLES_SEARCH = "/searchArticles";

    public static final String USER_LIST = "/userList";

    public static final String CONFIRM_EMAIL = "/confirmEmail";

    public static final String TEST = "/test";

    public static final String TESTS = "/tests";

    public static final String TESTS_ID = "/tests/{id}";

    public static final String PRACTICE = "/practice";

    public static final String USER_PRACTICES = "/practices/{username}";

    public static final String PRACTICE_ID = "/practice/{id}";
}
