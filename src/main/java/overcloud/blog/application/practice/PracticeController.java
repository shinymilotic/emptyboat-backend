package overcloud.blog.application.practice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import overcloud.blog.application.practice.core.PracticeRequest;
import overcloud.blog.application.practice.create_practice.CreatePracticeService;
import overcloud.blog.infrastructure.ApiConst;

@RestController
public class PracticeController {
    private final CreatePracticeService createPracticeService;

    PracticeController(CreatePracticeService createPracticeService) {
        this.createPracticeService = createPracticeService;
    }

    @PostMapping(ApiConst.PRACTICE)
    public void practice(@RequestBody PracticeRequest practiceRequest) {
        createPracticeService.createPractice(practiceRequest);
    }

    @GetMapping(ApiConst.USER_PRACTICES)
    public void practice(@PathVariable("username") String username) {
        //createPracticeService.createPractice(practiceRequest);
    }
}
