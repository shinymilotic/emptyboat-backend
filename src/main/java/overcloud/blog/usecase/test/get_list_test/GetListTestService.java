package overcloud.blog.usecase.test.get_list_test;

import org.springframework.stereotype.Service;

@Service
public interface GetListTestService {
    TestListResponse getListTest();
}
