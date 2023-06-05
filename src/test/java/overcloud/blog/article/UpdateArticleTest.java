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
import overcloud.blog.application.article.update_article.UpdateArticleRequest;
import overcloud.blog.application.article.update_article.UpdateArticleResponse;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UpdateArticleTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void testUpdateArticle() throws Exception {
        UpdateArticleRequest articleRequest = UpdateArticleRequest.builder()
                .body("Updated body")
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

    }
}
