package overcloud.blog.usecase.test.get_profile_test;

import overcloud.blog.response.RestResponse;

public interface GetProfileTestService {
    RestResponse<ProfileTestRes> getProfileTests(String username);
}
