package overcloud.blog.usecase.test.get_list_test;

import java.util.List;

import org.springframework.stereotype.Service;

import overcloud.blog.common.response.RestResponse;

@Service
public interface GetListTestService {
    RestResponse<List<SimpleTestResponse>> getListTest();
}
