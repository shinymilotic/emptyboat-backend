package overcloud.blog.usecase.test.delete_test;

import org.springframework.stereotype.Service;

@Service
public interface DeleteTestService {
    Void deleteTest(String id);
}
