package overcloud.blog.usecase.test.get_profile_test;

import overcloud.blog.response.RestResponse;

import java.util.List;

public interface GetProfileTestService {
    RestResponse<List<ProfileTestRes>> getProfileTests(String username);
}
