package overcloud.blog.usecase.test.get_practices;

import java.util.List;
import org.springframework.stereotype.Service;

import overcloud.blog.usecase.test.get_practices.response.PracticeResponse;

@Service
public interface UserPracticeService {
    List<PracticeResponse> getUserPractice(String username);
}
