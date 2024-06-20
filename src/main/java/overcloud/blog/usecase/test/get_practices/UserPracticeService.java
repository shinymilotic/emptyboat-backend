package overcloud.blog.usecase.test.get_practices;

import org.springframework.stereotype.Service;

import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.UserPracticeResponse;

@Service
public interface UserPracticeService {
    RestResponse<UserPracticeResponse> getUserPractice(String username);
}
