package overcloud.blog.usecase.test.delete_test;

import org.springframework.stereotype.Service;

import overcloud.blog.response.RestResponse;

@Service
public interface DeleteTestService {
    Void deleteTest(String id);
}
