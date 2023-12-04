package overcloud.blog.application.test.delete_test.impl;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import overcloud.blog.application.test.delete_test.DeleteTestService;
import overcloud.blog.repository.TestRepository;

@Service
public class DeleteTestServiceImpl implements DeleteTestService{

    private final TestRepository testRepository;

    DeleteTestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    @Transactional
    public void deleteTest(String slug) {
        testRepository.deleteBySlug(slug);
    }
    
}
