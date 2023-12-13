package overcloud.blog.application.practice.get_practices;

import org.springframework.stereotype.Service;

import overcloud.blog.application.practice.core.UserPracticeResponse;

@Service
public interface UserPracticeService {
    UserPracticeResponse getUserPractice(String username);
}
