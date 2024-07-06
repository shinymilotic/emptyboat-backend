package overcloud.blog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.TestRequest;
import overcloud.blog.usecase.test.common.TestResponse;
import overcloud.blog.usecase.test.create_test.CreateTestService;
import overcloud.blog.usecase.test.delete_test.DeleteTestService;
import overcloud.blog.usecase.test.get_list_test.GetListTestService;
import overcloud.blog.usecase.test.get_list_test.SimpleTestResponse;
import overcloud.blog.usecase.test.get_test.GetTestService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestController {
    private final CreateTestService createTestService;
    private final GetListTestService getListTestService;
    private final GetTestService getTestService;
    private final DeleteTestService deleteTestService;

    public TestController(CreateTestService createTestService,
                          GetListTestService getListTestService,
                          GetTestService getTestService,
                          DeleteTestService deleteTestService) {
        this.createTestService = createTestService;
        this.getListTestService = getListTestService;
        this.getTestService = getTestService;
        this.deleteTestService = deleteTestService;
    }

    /* get exam
     *   create exam
     *   update exam
     *   delete exam
     * */

    @PostMapping(ApiConst.TEST)
    public RestResponse<Void> createTest(@RequestBody TestRequest testRequest) {
        return this.createTestService.createTest(testRequest);
    }

    @GetMapping(ApiConst.TESTS)
    public RestResponse<List<SimpleTestResponse>> getTest() {
        return this.getListTestService.getListTest();
    }

    @GetMapping(ApiConst.TESTS_ID)
    public RestResponse<TestResponse> getTestBySlug(@PathVariable("slug") String slug) {
        return this.getTestService.getTest(slug);
    }

    @DeleteMapping(ApiConst.TESTS_ID)
    public RestResponse<Void> deleteTestBySlug(@PathVariable("slug") String slug) {
        return this.deleteTestService.deleteTest(slug);
    }
}
