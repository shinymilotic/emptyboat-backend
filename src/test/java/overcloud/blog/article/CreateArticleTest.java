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
import overcloud.blog.application.article.create_article.CreateArticleRequest;
import overcloud.blog.application.article.core.repository.ArticleRepository;

import java.util.List;


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

    public CreateArticleTest() {
    }

    @Test
    public void testCreateArticle() throws Exception {
        CreateArticleRequest articleRequest = getArticleRequest();
        String articleRequestStr = objectMapper.writeValueAsString(articleRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                        .content(articleRequestStr)).andReturn();

    }

    @Test
    public void testDeleteArticle() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/articles/" + "f2f37ca6-4673-419a-8a62-0261b1b7b801")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content("articleRequestStr")).andReturn();

    }

    private CreateArticleRequest getArticleRequest() {
        return CreateArticleRequest.builder()
                .title("Empty title")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring", "Clean code"))
                .build();
    }

}
