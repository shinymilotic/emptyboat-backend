package overcloud.blog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.usecase.test.create_test.request.TestRequest;
import overcloud.blog.usecase.test.get_profile_test.GetProfileTestService;
import overcloud.blog.usecase.test.get_profile_test.ProfileTestRes;
import overcloud.blog.usecase.test.get_test.response.TestResponse;
import overcloud.blog.usecase.test.create_test.CreateTestService;
import overcloud.blog.usecase.test.delete_test.DeleteTestService;
import overcloud.blog.usecase.test.get_list_test.GetListTestService;
import overcloud.blog.usecase.test.get_list_test.SimpleTestResponse;
import overcloud.blog.usecase.test.get_test.GetTestService;
import overcloud.blog.usecase.test.update_test.TestUpdateRequest;
import overcloud.blog.usecase.test.update_test.UpdateTestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestController {
    private final CreateTestService createTestService;
    private final GetListTestService getListTestService;
    private final GetTestService getTestService;
    private final DeleteTestService deleteTestService;
    private final UpdateTestService updateTestService;
    private final GetProfileTestService getProfileTestService;

    public TestController(CreateTestService createTestService,
                          GetListTestService getListTestService,
                          GetTestService getTestService,
                          DeleteTestService deleteTestService,
                          UpdateTestService updateTestService,
                          GetProfileTestService getProfileTestService) {
        this.createTestService = createTestService;
        this.getListTestService = getListTestService;
        this.getTestService = getTestService;
        this.deleteTestService = deleteTestService;
        this.updateTestService = updateTestService;
        this.getProfileTestService = getProfileTestService;
    }

    @PostMapping("/tests")
    public Void createTest(@RequestBody TestRequest testRequest) {
        return this.createTestService.createTest(testRequest);
    }

    @GetMapping("/tests")
    public List<SimpleTestResponse> getTest() {
        return this.getListTestService.getListTest();
    }

    @GetMapping("/tests/{id}")
    public TestResponse getTestById(@PathVariable("id") String id) {
        return this.getTestService.getTest(id);
    }

    @DeleteMapping("/tests/{id}")
    public Void deleteTestById(@PathVariable("id") String id) {
        return this.deleteTestService.deleteTest(id);
    }

    @PutMapping("/tests/{id}")
    public Void updateTest(@PathVariable("id") String id, @RequestBody TestUpdateRequest request) {
        return updateTestService.updateTest(id, request);
    }

    @GetMapping("/tests/profile/{username}")
    public List<ProfileTestRes> getProfileTests(@PathVariable("username") String username) {
        return getProfileTestService.getProfileTests(username);
    }
}
