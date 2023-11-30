package overcloud.blog.application.test;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.test.common.TestListResponse;
import overcloud.blog.application.test.common.TestRequest;
import overcloud.blog.application.test.common.TestResponse;
import overcloud.blog.application.test.create_test.CreateTestService;
import overcloud.blog.application.test.get_list_test.GetListTestService;
import overcloud.blog.application.test.get_test.GetTestService;
import overcloud.blog.infrastructure.ApiConst;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestController {

    private final CreateTestService createTestService;

    private final GetListTestService getListTestService;

    private final GetTestService getTestService;

    public TestController(CreateTestService createTestService,
                          GetListTestService getListTestService,
                          GetTestService getTestService) {
        this.createTestService = createTestService;
        this.getListTestService = getListTestService;
        this.getTestService = getTestService;
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

    @GetMapping(ApiConst.TESTS)
    public TestListResponse getTest() {
        return this.getListTestService.getListTest();
    }

    @GetMapping(ApiConst.TESTS_SLUG)
    public TestResponse getTestBySlug(@PathVariable("slug") String slug) {
        return this.getTestService.getTest(slug);
    }
}
