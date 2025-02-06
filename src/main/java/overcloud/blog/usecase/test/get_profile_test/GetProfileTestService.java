package overcloud.blog.usecase.test.get_profile_test;

import java.util.List;

public interface GetProfileTestService {
    List<ProfileTestRes> getProfileTests(String username);
}
