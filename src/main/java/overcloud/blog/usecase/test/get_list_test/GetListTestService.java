package overcloud.blog.usecase.test.get_list_test;

import org.springframework.stereotype.Service;

import overcloud.blog.usecase.test.common.TestListResponse;

@Service
public interface GetListTestService {
    TestListResponse getListTest();
}
