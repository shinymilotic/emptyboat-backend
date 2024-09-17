package overcloud.blog.article.update;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.blog.update_article.UpdateArticleRequest;
import overcloud.blog.usecase.blog.update_article.UpdateArticleService;
import overcloud.blog.repository.jparepository.JpaTagRepository;

@ExtendWith(MockitoExtension.class)
public class UpdateArticleTest {
    @Mock
    private UpdateArticleService updateArticleRequest;
    @Mock
    private SpringAuthenticationService authenticationService;
    @Mock
    private JpaTagRepository tagRepository;
    @Mock
    private JpaArticleRepository articleRepository;
    @Mock
    private JpaUserRepository userRepository;
    @Mock
    private ObjectsValidator<UpdateArticleRequest> validator;
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    private void setAuthentication() {
        // add principal object to SecurityContextHolder
    }

    // @BeforeEach
    // void initUseCase() {
    //     validator = new ObjectsValidator<UpdateArticleRequest>(messageSource());
    //     authenticationService = new SpringAuthenticationService(userRepository,new BCryptPasswordEncoder());
    //     updateArticleRequest = new UpdateArticleService(authenticationService, articleRepository, tagRepository, favoriteUtils, validator);
    // }

    // private UpdateArticleResponse updateArticle(UpdateArticleRequest articleRequest, String currentSlug) throws Exception {
    //     when(articleRepository.findBySlug(anyString())).thenAnswer(invocation -> {
    //         String slug = invocation.getArgument(0);
    //         List<ArticleEntity> articleEntities = new ArrayList<>();
    //         ArticleEntity article = ArticleEntityFactory.createArticleBySlug();
    //         article.setSlug(slug);
    //         articleEntities.add(article);
    //         return articleEntities;
    //     });

    //     when(articleRepository.save(any(ArticleEntity.class))).thenAnswer(invocation -> {
    //         ArticleEntity article = invocation.getArgument(0);
    //         ArticleEntity savedArticle = new ArticleEntity();
    //         savedArticle.setId(article.getId());
    //         savedArticle.setTitle(article.getTitle());
    //         savedArticle.setSlug(article.getSlug());
    //         savedArticle.setBody(article.getBody());
    //         savedArticle.setAuthor(article.getAuthor());
    //         savedArticle.setCreatedAt(article.getCreatedAt());
    //         savedArticle.setUpdatedAt(article.getUpdatedAt());

    //         return savedArticle;
    //     });

    //     return updateArticleRequest.updateArticle(articleRequest, currentSlug);
    // }

    // private UpdateArticleResponse updateArticleError(UpdateArticleRequest articleRequest, String currentSlug) throws Exception {


    //     return updateArticleRequest.updateArticle(articleRequest, currentSlug);
    // }

    // @Test
    // void test_updateArticle() throws Exception {
    //     setAuthentication();
    //     UpdateArticleRequest request = UpdateArticleRequest.builder()
    //             .body("dsadasdsads").build();
    //     String slug = "contrasting-three-projects";

    //     UpdateArticleResponse articleResponse = updateArticle(request, slug);

    //     assertEquals(request.getBody(), articleResponse.getBody());
    // }

    // @Test
    // void test_updateArticleBlankBody() throws Exception {
    //     setAuthentication();
    //     UpdateArticleRequest request = UpdateArticleRequest.builder()
    //             .body("")
    //             .build();
    //     String slug = "contrasting-three-projects";
    //     ApiError apiError = null;

    //     try {
    //         UpdateArticleResponse articleResponse = updateArticleError(request, slug);
    //     } catch (InvalidDataException e) {
    //         apiError = e.getApiError();
    //     }

    //     assertApiError(apiError, "article.body.not-blank", "Article body must be specified");
    // }

    // @Test
    // void test_updateArticleNoLogin() throws Exception {
    //     UpdateArticleRequest request = UpdateArticleRequest.builder()
    //             .body("")
    //             .build();
    //     String slug = "contrasting-three-projects";
    //     ApiError apiError = null;

    //     try {
    //         UpdateArticleResponse articleResponse = updateArticleError(request, slug);
    //     } catch (InvalidDataException e) {
    //         apiError = e.getApiError();
    //     }

    //     assertApiError(apiError, "user.get-current-user.not-found", "You have to sign in first!");
    // }

    // @Test
    // void test_updateArticleNullBody() throws Exception {
    //     UpdateArticleRequest request = UpdateArticleRequest.builder()
    //             .build();
    //     String slug = "contrasting-three-projects";
    //     ApiError apiError = null;

    //     try {
    //         UpdateArticleResponse articleResponse = updateArticleError(request, slug);
    //     } catch (InvalidDataException e) {
    //         apiError = e.getApiError();
    //     }

    //     assertApiError(apiError, "article.body.not-blank", "Article body must be specified");
    // }

    // public void assertApiError(ApiError apiError, String id, String message) {
    //     List<ApiErrorDetail> apiErrorDetail = apiError.getApiErrorDetails();

    //     for (ApiErrorDetail detail : apiErrorDetail) {
    //         if(detail.getId().equals(id)) {
    //             assertEquals(detail.getMessage(), message);
    //             return;
    //         }
    //     }

    //     assertFalse(false);
    // }
}
