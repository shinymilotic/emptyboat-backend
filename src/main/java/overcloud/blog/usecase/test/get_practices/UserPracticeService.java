package overcloud.blog.usecase.test.get_practices;

import java.util.List;
import org.springframework.stereotype.Service;

import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.test.common.PracticeResponse;

@Service
public interface UserPracticeService {
    RestResponse<List<PracticeResponse>> getUserPractice(String username);
}
