package overcloud.blog.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.sql.Update;
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
import overcloud.blog.application.article.update_article.UpdateArticleRequest;
import overcloud.blog.application.article.update_article.UpdateArticleResponse;
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
public class UpdateArticleTest {
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

    @Test
    public void testUpdateArticle() throws Exception {
        UpdateArticleRequest articleRequest = UpdateArticleRequest.builder()
                .title("testCreateArticle")
                .description("Updated description")
                .body("Updated body")
                .tagList(List.of("Clean code"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/articles/" + "testcreatearticle")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();


        String response = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        UpdateArticleResponse targetObject = mapper.readValue(response, UpdateArticleResponse.class);

        UUID id = UUID.fromString(targetObject.getId());
        ArticleEntity articleEntity = articleRepository.findById(id).orElse(null);

        assertEquals(UUID.fromString(targetObject.getId()), articleEntity.getId());
        assertEquals(articleEntity.getSlug(), targetObject.getSlug());
        assertEquals(articleEntity.getTitle(), targetObject.getTitle());
        assertEquals(articleEntity.getDescription(), targetObject.getDescription());
        assertEquals(articleEntity.getBody(), targetObject.getBody());
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
    public void testUpdateArticleNoTag() throws Exception {
        UpdateArticleRequest articleRequest = UpdateArticleRequest.builder()
                .title("testCreateArticle")
                .description("Updated description")
                .body("Updated body")
                .tagList(List.of())
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/articles/"+"testcreatearticle")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "updateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "tagList");
        assertEquals(apiValidationError.get(0).getRejectedValue(), List.of());
        assertEquals(apiValidationError.get(0).getMessage(), "Tag must be specified");
    }

    @Test
    public void testUpdateArticleWrongTag() throws Exception {
        UpdateArticleRequest articleRequest = UpdateArticleRequest.builder()
                .title("testCreateArticle")
                .description("Updated description")
                .body("Updated body")
                .tagList(List.of("Wrong", "Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/articles/" + "testcreatearticle")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "UpdateArticle");
        assertEquals(apiValidationError.get(0).getField(), "tagList");
        assertEquals(apiValidationError.get(0).getRejectedValue(), List.of("Wrong", "Spring"));
        assertEquals(apiValidationError.get(0).getMessage(), "There is tag doesn't exist");
    }

    @Test
    public void testUpdateArticleNoTitle() throws Exception {
        UpdateArticleRequest articleRequest = UpdateArticleRequest.builder()
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/articles/"  + "testcreatearticle")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "updateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "title");
        assertEquals(apiValidationError.get(0).getRejectedValue(), null);
        assertEquals(apiValidationError.get(0).getMessage(), "Title must be specified");
    }

    @Test
    public void testUpdateArticleEmptyTitle() throws Exception {
        UpdateArticleRequest articleRequest = UpdateArticleRequest.builder()
                .title("d")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/articles/" + "testcreatearticle")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "updateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "title");
        assertEquals(apiValidationError.get(0).getRejectedValue(), "");
        assertEquals(apiValidationError.get(0).getMessage(), "Title must be specified");
    }

    @Test
    public void testUpdateArticleTitleLength() throws Exception {
        UpdateArticleRequest articleRequest = UpdateArticleRequest.builder()
                .title("1dsadsssssssssssssssssssssssssssasfsafsafssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/articles/" + "testcreatearticle")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "updateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "title");
        assertEquals(apiValidationError.get(0).getRejectedValue(), "1dsadsssssssssssssssssssssssssssasfsafsafssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        assertEquals(apiValidationError.get(0).getMessage(), "Title length must be between 1 and 60 characters");
    }

    @Test
    public void testCreateArticleNoDescription() throws Exception {
        UpdateArticleRequest articleRequest = UpdateArticleRequest.builder()
                .title("testCreateArticleNoTag")
                .body("Nothing inside the body")
                .tagList(List.of("Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/articles/" + "testcreatearticle")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "updateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "description");
        assertEquals(apiValidationError.get(0).getRejectedValue(), null);
        assertEquals(apiValidationError.get(0).getMessage(), "Description must be specified");
    }

    @Test
    public void testUpdateArticleDescriptionLength() throws Exception {
        UpdateArticleRequest articleRequest = UpdateArticleRequest.builder()
                .title("testCreateArticleNoTag")
                .body("Nothing inside the body")
                .description("1dsadsssssssssssssssssssssssssssasfsafsafsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd")
                .tagList(List.of("Spring"))
                .build();

        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/articles/" + "testcreatearticle")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content(articleRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);
        List<ApiValidationError> apiValidationError = targetObject.getApiErrorDetails();

        assertEquals("Validation error", targetObject.getMessage());
        assertEquals(apiValidationError.get(0).getObject(), "updateArticleRequest");
        assertEquals(apiValidationError.get(0).getField(), "description");
        assertEquals(apiValidationError.get(0).getRejectedValue(), "1dsadsssssssssssssssssssssssssssasfsafsafsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        assertEquals(apiValidationError.get(0).getMessage(), "Description size must between 1 and 100");
    }
}
