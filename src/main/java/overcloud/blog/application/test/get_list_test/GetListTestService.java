package overcloud.blog.application.test.get_list_test;

import org.springframework.stereotype.Service;
import overcloud.blog.application.test.common.TestListResponse;

@Service
public interface GetListTestService {
    TestListResponse getListTest();
}
