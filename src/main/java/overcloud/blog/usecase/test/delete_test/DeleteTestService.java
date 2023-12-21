package overcloud.blog.usecase.test.delete_test;

import org.springframework.stereotype.Service;

@Service
public interface DeleteTestService {
    void deleteTest(String slug);
}
