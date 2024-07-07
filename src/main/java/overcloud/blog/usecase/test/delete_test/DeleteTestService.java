package overcloud.blog.usecase.test.delete_test;

import org.springframework.stereotype.Service;

import overcloud.blog.usecase.common.response.RestResponse;

@Service
public interface DeleteTestService {
    RestResponse<Void> deleteTest(String id);
}
