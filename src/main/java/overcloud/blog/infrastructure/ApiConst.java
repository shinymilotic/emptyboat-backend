package overcloud.blog.infrastructure;

public class ApiConst {

    /* Articles */
    public static final String ARTICLES = "/articles";
    public static final String ARTICLES_SLUG = "/articles/{slug}";

    public static final String ARTICLES_SLUG_COMMENTS = "/articles/{slug}/comments";

    public static final String ARTICLES_SLUG_COMMENTS_ID = "/articles/{slug}/comments/{id}";

    public static final String ARTICLES_SLUG_FAVORITE = "/articles/{slug}/favorite";
    public static final String ROLES_USERNAME = "/roles/{username}";

    public static final String ROLES = "/roles";

    public static final String TAGS = "/tags";

    public static final String USERS = "/users";

    public static final String USERS_LOGIN = "/users/login";
    public static final String USERS_LOGIN_ADMIN = "/users/loginAdmin";

    public static final String USERS_LOGOUT = "/users/logout";

    public static final String USERS_REFRESHTOKEN = "/users/refreshToken";

    public static final String PROFILES_USERNAME = "/profiles/{username}";

    public static final String USERS_USERNAME_ASSIGNMENT = "/users/{username}/assignment";

    public static final String PROFILES_USERNAME_FOLLOW = "/profiles/{username}/follow";

    public static final String ARTICLES_SEARCH = "/searchArticles";

    public static final String USER_LIST = "/userList";

    public static final String TEST = "/test";

    public static final String TESTS = "/tests";

    public static final String TESTS_SLUG = "/tests/{slug}";

    public static final String PRACTICE = "/practice";
}
