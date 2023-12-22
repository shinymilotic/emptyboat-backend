// package overcloud.blog.article.create;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.context.MessageSource;
// import org.springframework.context.support.ReloadableResourceBundleMessageSource;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import overcloud.blog.entity.ArticleEntity;
// import overcloud.blog.infrastructure.InvalidDataException;
// import overcloud.blog.repository.jparepository.ArticleRepository;
// import overcloud.blog.repository.jparepository.ArticleTagRepository;
// import overcloud.blog.application.article.create_article.ArticleRequest;
// import overcloud.blog.application.article.create_article.ArticleResponse;
// import overcloud.blog.application.article.create_article.CreateArticleService;
// import overcloud.blog.entity.TagEntity;
// import overcloud.blog.application.tag.core.TagError;
// import overcloud.blog.application.tag.core.repository.TagRepository;
// import overcloud.blog.entity.UserEntity;
// import overcloud.blog.application.user.core.repository.UserRepository;
// import overcloud.blog.article.ArticleRequestFactory;
// import overcloud.blog.infrastructure.exceptionhandling.ApiError;
// import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;
// import overcloud.blog.infrastructure.security.bean.SecurityUser;
// import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
// import overcloud.blog.infrastructure.validation.ObjectsValidator;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// public class CreateArticleTest {
//     @Mock
//     private CreateArticleService createArticleService;
//     @Mock
//     private SpringAuthenticationService authenticationService;
//     @Mock
//     private TagRepository tagRepository;
//     @Mock
//     private ArticleRepository articleRepository;
//     @Mock
//     private UserRepository userRepository;
//     @Mock
//     private ObjectsValidator<ArticleRequest> validator;

//     @Mock
//     private ArticleTagRepository articleTagRepository;

//     public MessageSource messageSource() {
//         ReloadableResourceBundleMessageSource messageSource
//                 = new ReloadableResourceBundleMessageSource();

//         messageSource.setBasename("classpath:messages");
//         messageSource.setDefaultEncoding("UTF-8");
//         return messageSource;
//     }
//     @BeforeEach
//     void initUseCase() {
//         setAuthentication();
//         validator = new ObjectsValidator<ArticleRequest>(messageSource());
//         authenticationService = new SpringAuthenticationService(userRepository,new BCryptPasswordEncoder());
//         createArticleService = new CreateArticleService(authenticationService, tagRepository, articleRepository, validator, articleTagRepository, null);
//     }

//     private void setAuthentication() {
//         // add principal object to SecurityContextHolder
//         UserEntity user = UserEntity.builder()
//                 .id(UUID.fromString("e7e861df-2c3f-4304-a2b0-3b98c1ba16c8"))
//                 .email("trungtin.mai1412@gmail.com")
//                 .username("thepianist00")
//                 .bio("A pragmatddsdsadsaic programmerss")
//                 .image("https://avatars.githubusercontent.com/u/19252712?s=100&v=100")
//                 .password("$2a$10$ba45PLemGgZxRXAjHkyuRuuHE0o4dmrKFcyW5a")
//                 .build();

//         UserDetails secureUser = new SecurityUser(user);
//         Authentication auth = new UsernamePasswordAuthenticationToken(secureUser,secureUser.getAuthorities());
//         SecurityContextHolder.getContext().setAuthentication(auth);
//     }
//     private ArticleResponse createArticle(ArticleRequest articleRequest) throws Exception {
//         when(articleRepository.save(any(ArticleEntity.class))).thenAnswer(invocation -> {
//             ArticleEntity articleEntity = invocation.getArgument(0);
//             articleEntity.setId(UUID.fromString("011d2f94-7897-4a7a-9737-bb600154be00"));
//             return articleEntity;
//         });

//         when(tagRepository.findByTagName(anyList())).thenAnswer(invocation -> {
//             List<String> tags = invocation.getArgument(0);
//             List<TagEntity> tagEntities = new ArrayList<>();
//             for (String tag : tags ){
//                 TagEntity tagEntity = new TagEntity();
//                 tagEntity.setName(tag);
//                 tagEntities.add(tagEntity);
//             }
//             return tagEntities;
//         });

//         return createArticleService.createArticle(articleRequest);
//     }

//     private ArticleResponse createArticleError(ArticleRequest articleRequest) throws Exception {
//         return createArticleService.createArticle(articleRequest);
//     }

//     private ArticleResponse createArticleNoExistTag(ArticleRequest articleRequest) throws JsonProcessingException {
//         when(tagRepository.findByTagName(anyList())).thenAnswer(invocation -> {
//             List<String> tags =  invocation.getArgument(0);
//             List<String> newTagList = new ArrayList<>();
//             for (int i = 1; i < tags.size(); i++) {
//                 newTagList.add(tags.get(i));
//             }

//             List<TagEntity> tagEntities = new ArrayList<>();
//             for (String tag : newTagList){
//                 TagEntity tagEntity = new TagEntity();
//                 tagEntity.setName(tag);
//                 tagEntities.add(tagEntity);
//             }
//             return tagEntities;
//         });

//         return createArticleService.createArticle(articleRequest);
//     }

//     @Test
//     void testCreateArticle() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequest();

//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticle(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertEquals(articleResponse.getBody(), articleRequest.getBody());
//         assertEquals(articleResponse.getTitle(), articleRequest.getTitle());
//         assertEquals("testa-dsadcreaatdasdas-earticle", articleResponse.getSlug());
//         assertEquals(0, articleResponse.getFavoritesCount());
//         assertEquals("thepianist00", articleResponse.getAuthor().getUsername());
//         assertEquals("A pragmatddsdsadsaic programmerss", articleResponse.getAuthor().getBio());
//         assertEquals("https://avatars.githubusercontent.com/u/19252712?s=100&v=100", articleResponse.getAuthor().getImage());
//     }

//     public void assertApiError(ApiError apiError, String id, String message) {
//         List<ApiErrorDetail> apiErrorDetail = apiError.getApiErrorDetails();

//         for (ApiErrorDetail detail : apiErrorDetail) {
//             if(detail.getId().equals(id)) {
//                 assertEquals(detail.getMessage(), message);
//                 return;
//             }
//         }

//         assertFalse(false);
//     }

//     @Test
//     void testCreateArticleEmptyTitle() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestEmptyTitle();

//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleError(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, "article.create.not-blank" ,"Title must be specified");
//     }

//     @Test
//     void testCreateArticleNullTitle() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestNullTitle();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleError(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, "article.create.not-blank" ,"Title must be specified");
//     }

//     @Test
//     void testCreateArticleNotFitSizeTitle() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestNotFitSizeTitle();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleError(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, "article.create.size" ,"Title length must be between 1 and 60 characters");
//     }

//     @Test
//     void testCreateArticleNullDescription() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestNullDescription();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleError(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, "article.description.not-blank" ,"Description must be specified");
//     }

//     @Test
//     void testCreateArticleEmptyDescription() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestEmptyDescription();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleError(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, "article.description.not-blank" ,"Description must be specified");
//     }

//     @Test
//     void testCreateArticleNotFitDescription() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestNotFitDescription();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleError(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, "article.description.size" ,"Description size must be between 1 and 100");
//     }

//     @Test
//     void testCreateArticleNullBody() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestNullBody();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleError(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, "article.body.not-blank" ,"Article body must be specified");
//     }

//     @Test
//     void testCreateArticleBlankBody() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestBlankBody();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleError(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, "article.body.not-blank" ,"Article body must be specified");
//     }

//     @Test
//     void testCreateArticleNullTag() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestNullTag();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleError(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, "article.tags.not-empty" ,"Tags must be specified");
//     }

//     @Test
//     void testCreateArticleEmptyTag() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestEmptyTag();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleError(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, "article.tags.not-empty" ,"Tags must be specified");
//     }

//     @Test
//     void testCreateArticleDuplicateTag() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestDuplicateTag();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticle(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertEquals(articleResponse.getBody(), articleRequest.getBody());
//         assertEquals(articleResponse.getTitle(), articleRequest.getTitle());
//         assertEquals("titlecss", articleResponse.getSlug());
//         assertEquals(0, articleResponse.getFavoritesCount());
//         assertEquals("thepianist00", articleResponse.getAuthor().getUsername());
//         assertEquals("A pragmatddsdsadsaic programmerss", articleResponse.getAuthor().getBio());
//         assertEquals("https://avatars.githubusercontent.com/u/19252712?s=100&v=100", articleResponse.getAuthor().getImage());
//     }

//     @Test
//     void testCreateArticleNotExistTag() throws Exception {
//         ArticleRequest articleRequest = ArticleRequestFactory.createNormalArticleRequestNotExistTag();
//         ArticleResponse articleResponse = null;
//         ApiError apiError = null;
//         try {
//             articleResponse = createArticleNoExistTag(articleRequest);
//         } catch (InvalidDataException e) {
//             apiError = e.getApiError();
//         }

//         assertApiError(apiError, TagError.TAG_NO_EXISTS.getMessageId(), TagError.TAG_NO_EXISTS.getErrorMessage());
//     }
// }
