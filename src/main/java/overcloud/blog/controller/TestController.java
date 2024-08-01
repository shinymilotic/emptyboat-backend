package overcloud.blog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.TestRequest;
import overcloud.blog.usecase.test.common.TestResponse;
import overcloud.blog.usecase.test.create_test.CreateTestService;
import overcloud.blog.usecase.test.delete_test.DeleteTestService;
import overcloud.blog.usecase.test.get_list_test.GetListTestService;
import overcloud.blog.usecase.test.get_list_test.SimpleTestResponse;
import overcloud.blog.usecase.test.get_test.GetTestService;
import overcloud.blog.usecase.test.update_test.TestUpdateRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @PostMapping(ApiConst.TEST)
    public RestResponse<Void> createTest(@RequestBody TestRequest testRequest) {
        return this.createTestService.createTest(testRequest);
    }

    @GetMapping(ApiConst.TESTS)
    public RestResponse<List<SimpleTestResponse>> getTest() {
        return this.getListTestService.getListTest();
    }

    @GetMapping(ApiConst.TESTS_ID)
    public RestResponse<TestResponse> getTestById(@PathVariable("id") String id) {
        return this.getTestService.getTest(id);
    }

    @DeleteMapping(ApiConst.TESTS_ID)
    public RestResponse<Void> deleteTestById(@PathVariable("id") String id) {
        return this.deleteTestService.deleteTest(id);
    }

    @PostMapping(ApiConst.TEST_UPDATE)
    public RestResponse<Void> updateTest(@PathVariable("id") String id, @RequestBody TestUpdateRequest entity) {
        
        return entity;
    }
    
}
