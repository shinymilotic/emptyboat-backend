package overcloud.blog.application.test;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import overcloud.blog.application.test.create_test.CreateTestService;
import overcloud.blog.infrastructure.ApiConst;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestController {

    private CreateTestService createTestService;

    public TestController(CreateTestService createTestService) {
        this.createTestService = createTestService;
    }

    /* get exam
    *   create exam
    *   update exam
    *   delete exam
    * */

    @PostMapping(ApiConst.TEST)
    public String createTest(@RequestBody TestRequest testRequest) {
        return this.createTestService.createTest(testRequest);
    }
}
