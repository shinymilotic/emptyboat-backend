package overcloud.blog.usecase.practice.get_practices;

import org.springframework.stereotype.Service;

import overcloud.blog.usecase.practice.core.UserPracticeResponse;

@Service
public interface UserPracticeService {
    UserPracticeResponse getUserPractice(String username);
}
