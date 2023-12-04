package overcloud.blog.application.practice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import overcloud.blog.application.practice.core.PracticeRequest;
import overcloud.blog.application.practice.create_practice.CreatePracticeService;
import overcloud.blog.infrastructure.ApiConst;

@Controller
public class PracticeController {
    private final CreatePracticeService createPracticeService;

    PracticeController(CreatePracticeService createPracticeService) {
        this.createPracticeService = createPracticeService;
    }

    @PostMapping(ApiConst.PRACTICE)
    public boolean practice(PracticeRequest practiceRequest) {
        return createPracticeService.createPractice(practiceRequest);
    }
    
}
