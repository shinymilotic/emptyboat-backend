package overcloud.blog.application.test.delete_test;

import org.springframework.stereotype.Service;

@Service
public interface DeleteTestService {
    void deleteTest(String slug);
}
