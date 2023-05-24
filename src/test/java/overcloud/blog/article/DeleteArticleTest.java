package overcloud.blog.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import overcloud.blog.application.article.delete_article.DeleteArticleService;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteArticleTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeleteArticleService deleteArticleService;

    @Test
    public void testDeleteArticle() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/articles/" + "850ab09f-5d29-431b-a55d-c26ae5a65113")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content("articleRequestStr")).andReturn();
    }

    @Test
    public void testDeleteArticle2() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/articles/" + "132a7a38-9834-4cce-90a1-0a91deaebbde")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3Rpbi5tYWkxNDEyQGdtYWlsLmNvbSIsImlhdCI6MTY4MjI0NTU0OCwiZXhwIjoxNjg1MjQ1NTQ4fQ.VGevNNJXuUvYESDSmARGbtCpNvX2-mqnZjtx1nYXv88")
                .content("articleRequestStr")).andReturn();
    }
}
