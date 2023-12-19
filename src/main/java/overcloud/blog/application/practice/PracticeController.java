package overcloud.blog.application.practice;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import overcloud.blog.application.practice.core.PracticeRequest;
import overcloud.blog.application.practice.core.UserPracticeResponse;
import overcloud.blog.application.practice.create_practice.CreatePracticeService;
import overcloud.blog.application.practice.get_practices.UserPracticeService;
import overcloud.blog.infrastructure.ApiConst;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PracticeController {
    private final CreatePracticeService createPracticeService;

    private final UserPracticeService userPracticeService;

    PracticeController(CreatePracticeService createPracticeService, UserPracticeService userPracticeService) {
        this.createPracticeService = createPracticeService;
        this.userPracticeService = userPracticeService;
    }

    @PostMapping(ApiConst.PRACTICE)
    public void practice(@RequestBody PracticeRequest practiceRequest) {
        createPracticeService.createPractice(practiceRequest);
    }

    @GetMapping(ApiConst.USER_PRACTICES)
    public UserPracticeResponse getUserPractice(@PathVariable("username") String username) {
        return userPracticeService.getUserPractice(username);
    }
}
