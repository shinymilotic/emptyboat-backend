package overcloud.blog.usecase.test.get_list_test;

import org.springframework.stereotype.Service;

import overcloud.blog.usecase.common.response.RestResponse;

@Service
public interface GetListTestService {
    RestResponse<TestListResponse> getListTest();
}
