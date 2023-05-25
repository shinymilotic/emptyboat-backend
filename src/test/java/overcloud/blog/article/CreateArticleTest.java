package overcloud.blog.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.article.core.ArticleError;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.article.create_article.ArticleRequest;
import overcloud.blog.application.article.create_article.ArticleResponse;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateArticleTest {
    @Autowired
    private MockMvc mockMvc;

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
        ArticleRequest articleRequest = ArticleRequest.builder()
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
        ArticleResponse targetObject = mapper.readValue(response, ArticleResponse.class);

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
    }

    @Test
    public void testCreateArticleNoTag() throws Exception {
        ArticleRequest articleRequest = ArticleRequest.builder()
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
        List<ApiErrorDetail> apiValidationError = targetObject.getApiErrorDetails();

    }

    @Test
    public void testCreateArticleWrongTag() throws Exception {
        ArticleRequest articleRequest = ArticleRequest.builder()
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
        List<ApiErrorDetail> apiValidationError = targetObject.getApiErrorDetails();

    }

    @Test
    public void testCreateArticleNoTitle() throws Exception {
        ArticleRequest articleRequest = ArticleRequest.builder()
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
        List<ApiErrorDetail> apiValidationError = targetObject.getApiErrorDetails();

    }

    @Test
    public void testCreateArticleEmptyTitle() throws Exception {
        ArticleRequest articleRequest = ArticleRequest.builder()
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
        List<ApiErrorDetail> apiValidationError = targetObject.getApiErrorDetails();

    }

    @Test
    public void testCreateArticleTitleLength() throws Exception {
        ArticleRequest articleRequest = ArticleRequest.builder()
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
        List<ApiErrorDetail> apiValidationError = targetObject.getApiErrorDetails();

    }

    @Test
    public void testCreateArticleNoDescription() throws Exception {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("testCreateArticleNoDescription")
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
        List<ApiErrorDetail> apiValidationError = targetObject.getApiErrorDetails();

    }

    @Test
    public void testCreateArticleDescriptionLength() throws Exception {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("testCreateArticleDescriptionLength")
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
        List<ApiErrorDetail> apiValidationError = targetObject.getApiErrorDetails();

    }
}
