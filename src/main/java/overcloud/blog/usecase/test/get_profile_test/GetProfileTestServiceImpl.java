package overcloud.blog.usecase.test.get_profile_test;

import org.springframework.stereotype.Service;
import overcloud.blog.response.RestResponse;

import java.util.UUID;

@Service
public class GetProfileTestServiceImpl implements GetProfileTestService {
    @Override
    public RestResponse<ProfileTestRes> getProfileTests(String userId) {
        UUID uuidUserId = UUID.fromString(userId);
        return null;
    }
}
