package overcloud.blog.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.article.create_article.CreateArticleRequest;
import overcloud.blog.application.article.create_article.CreateArticleResponse;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiValidationError;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CreateArticleTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    public CreateArticleTest() {
    }

    @Test
    public void testCreateArticle() throws Exception {
        CreateArticleRequest articleRequest = CreateArticleRequest.builder()
                .title("testCreateArticle")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring", "Clean code"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                        .content(articleRequestStr)).andReturn();


        String response = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        CreateArticleResponse targetObject = mapper.readValue(response, CreateArticleResponse.class);

        UUID id = UUID.fromString(targetObject.getId());
        ArticleEntity articleEntity = articleRepository.findById(id).orElse(null);

        assertEquals(UUID.fromString(targetObject.getId()), articleEntity.getId());
        assertEquals(articleEntity.getSlug(), targetObject.getSlug());
        assertEquals(articleEntity.getTitle(), targetObject.getTitle());
        assertEquals(articleEntity.getDescription(), targetObject.getDescription());
        assertEquals(articleEntity.getBody(), targetObject.getBody());
        assertEquals(articleEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")), targetObject.getCreatedAt());
        assertEquals(articleEntity.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")), targetObject.getUpdatedAt());
        Iterable<String> responseTags = targetObject.getTagList();

        for (String tagName : responseTags) {
            TagEntity tag = tagRepository.findByTagName(tagName);
            if(tag != null) {
                assertTrue(true);
            }
        }

        UserEntity author = articleEntity.getAuthor();
        assertEquals(author.getUsername(), author.getUsername());
        assertEquals(author.getBio(), author.getBio());
        assertEquals(author.getImage(), author.getImage());

        articleRepository.delete(articleEntity);
    }

    @Test
    public void testCreateArticleNoTag() throws Exception {
        CreateArticleRequest articleRequest = CreateArticleRequest.builder()
                .title("testCreateArticleNoTag")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of())
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "CreateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "tagList");
        assertEquals(apiValidationError.get(0).getRejectedValue(), List.of());
        assertEquals(apiValidationError.get(0).getMessage(), "Tag must be specified");
    }

    @Test
    public void testCreateArticleWrongTag() throws Exception {
        CreateArticleRequest articleRequest = CreateArticleRequest.builder()
                .title("testCreateArticleWrongTag")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Wrong", "Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "CreateArticle");
        assertEquals(apiValidationError.get(0).getField(), "tagList");
        assertEquals(apiValidationError.get(0).getRejectedValue(), List.of("Wrong", "Spring"));
        assertEquals(apiValidationError.get(0).getMessage(), "There is tag doesn't exist");
    }

    @Test
    public void testCreateArticleNoTitle() throws Exception {
        CreateArticleRequest articleRequest = CreateArticleRequest.builder()
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "CreateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "title");
        assertEquals(apiValidationError.get(0).getRejectedValue(), null);
        assertEquals(apiValidationError.get(0).getMessage(), "Title must be specified");
    }

    @Test
    public void testCreateArticleEmptyTitle() throws Exception {
        CreateArticleRequest articleRequest = CreateArticleRequest.builder()
                .title("")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "CreateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "title");
        assertEquals(apiValidationError.get(0).getRejectedValue(), "");
        assertEquals(apiValidationError.get(0).getMessage(), "Title must be specified");
    }

    @Test
    public void testCreateArticleTitleLength() throws Exception {
        CreateArticleRequest articleRequest = CreateArticleRequest.builder()
                .title("1dsadsssssssssssssssssssssssssssasfsafsafssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "CreateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "title");
        assertEquals(apiValidationError.get(0).getRejectedValue(), "1dsadsssssssssssssssssssssssssssasfsafsafssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        assertEquals(apiValidationError.get(0).getMessage(), "Title length must be between 1 and 60 characters");
    }

    @Test
    public void testCreateArticleNoDescription() throws Exception {
        CreateArticleRequest articleRequest = CreateArticleRequest.builder()
                .title("testCreateArticleNoTag")
                .body("Nothing inside the body")
                .tagList(List.of("Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "CreateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "description");
        assertEquals(apiValidationError.get(0).getRejectedValue(), null);
        assertEquals(apiValidationError.get(0).getMessage(), "Description must be specified");
    }

    @Test
    public void testCreateArticleDescriptionLength() throws Exception {
        CreateArticleRequest articleRequest = CreateArticleRequest.builder()
                .title("testCreateArticleNoTag")
                .body("Nothing inside the body")
                .description("1dsadsssssssssssssssssssssssssssasfsafsafsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd")
                .tagList(List.of("Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "CreateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "description");
        assertEquals(apiValidationError.get(0).getRejectedValue(), "1dsadsssssssssssssssssssssssssssasfsafsafsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        assertEquals(apiValidationError.get(0).getMessage(), "Description size must between 1 and 100");
    }
}
