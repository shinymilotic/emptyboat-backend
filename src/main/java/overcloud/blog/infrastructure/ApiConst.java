package overcloud.blog.infrastructure;

public class ApiConst {

    /* Articles */
    public static final String ARTICLES = "/articles";

    public static final String ARTICLES_SLUG = "/articles/{slug}";


    /* Comments */
/*    @PostMapping("articles/{slug}/comments")

- Authorization: USER, ADMIN

    @GetMapping("articles/{slug}/comments")

- Authorization: USER, ADMIN, GUEST

    @DeleteMapping("articles/{slug}/comments/{id}")

- Authorization: USER, ADMIN*/

    public static final String ARTICLES_SLUG_COMMENTS = "/articles/{slug}/comments";

    public static final String ARTICLES_SLUG_COMMENTS_ID = "/articles/{slug}/comments/{id}";

    /*@PostMapping("articles/{slug}/favorite")

- Authorization: USER, ADMIN

@DeleteMapping("articles/{slug}/favorite")

- Authorization: USER, ADMIN*/

    public static final String ARTICLES_SLUG_FAVORITE = "/articles/{slug}/favorite";
    /*
    * @GetMapping("/role/{username}")
- Authorization:  ADMIN

@GetMapping("/roles")
- Authorization:  ADMIN

@PutMapping("/roles")
- Authorization: ADMIN*/
    public static final String ROLES_USERNAME = "/roles/{username}";

    public static final String ROLES = "/roles";

    public static final String TAGS = "/tags";

/*@PostMapping("users")
- Authorization: USER, ADMIN

@PutMapping("user")
- Authorization: USER, ADMIN

@PostMapping("users/login")
- Authorization: USER, ADMIN

@PostMapping("users/logout")
- Authorization: USER, ADMIN

@GetMapping("user")
- Authorization: USER, ADMIN

@GetMapping(value = "profiles/{username}")
- Authorization: USER, ADMIN, GUEST*/

    public static final String USERS = "/users";

    public static final String USERS_LOGIN = "/users/login";

    public static final String USERS_LOGOUT = "/users/logout";

    public static final String PROFILES_USERNAME = "/profiles/{username}";

    /*
@PutMapping("/roleAssignment/{username}/assignment")
- Authorization:  ADMIN*/

    public static final String USERS_USERNAME_ASSIGNMENT = "/users/{username}/assignment";

    public static final String PROFILES_USERNAME_FOLLOW = "/profiles/{username}/follow";

    public static final String ARTICLES_SEARCH = "/searchArticles";
}
