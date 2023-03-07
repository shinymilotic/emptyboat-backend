package overcloud.blog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SomethingController {
    @GetMapping("something")
    public String getSomething() {
        return "Hello, world!";
    }
}
