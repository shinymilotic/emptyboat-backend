package overcloud.blog.application.practice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import overcloud.blog.application.practice.create_practice.CreatePracticeService;

@Controller
public class PracticeController {
    private final CreatePracticeService createPracticeService;

    PracticeController(CreatePracticeService createPracticeService) {
        this.createPracticeService = createPracticeService;
    }

    @PostMapping()
    
}
