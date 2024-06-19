package overcloud.blog.controller;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.PracticeRequest;
import overcloud.blog.usecase.test.common.UserPracticeResponse;
import overcloud.blog.usecase.test.create_practice.CreatePracticeResponse;
import overcloud.blog.usecase.test.create_practice.CreatePracticeService;
import overcloud.blog.usecase.test.get_practice.GetPracticeResultService;
import overcloud.blog.usecase.test.get_practice.PracticeResult;
import overcloud.blog.usecase.test.get_practices.UserPracticeService;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PracticeController {

    private final CreatePracticeService createPracticeService;
    private final UserPracticeService userPracticeService;
    private final GetPracticeResultService getPracticeResultService;

    PracticeController(CreatePracticeService createPracticeService,
                       UserPracticeService userPracticeService,
                       GetPracticeResultService getPracticeResultService) {
        this.createPracticeService = createPracticeService;
        this.userPracticeService = userPracticeService;
        this.getPracticeResultService = getPracticeResultService;
    }

    @PostMapping(ApiConst.PRACTICE)
    public RestResponse<CreatePracticeResponse> practice(@RequestBody PracticeRequest practiceRequest) {
        return createPracticeService.createPractice(practiceRequest);
    }

    @GetMapping(ApiConst.USER_PRACTICES)
    public RestResponse<UserPracticeResponse> getUserPractice(@PathVariable("username") String username) {
        return userPracticeService.getUserPractice(username);
    }

    @GetMapping(ApiConst.PRACTICE_ID)
    public RestResponse<PracticeResult> getPracticeResult(@PathVariable("id") UUID practiceId) {
        return getPracticeResultService.getPracticeResult(practiceId);
    }
}
