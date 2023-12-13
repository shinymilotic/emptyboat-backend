package overcloud.blog.application.test.delete_test.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import overcloud.blog.application.test.delete_test.DeleteTestService;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.repository.PracticeRepository;
import overcloud.blog.repository.TestRepository;

@Service
public class DeleteTestServiceImpl implements DeleteTestService{

    private final TestRepository testRepository;

    private final PracticeRepository practiceRepository;

    DeleteTestServiceImpl(
        TestRepository testRepository,
        PracticeRepository practiceRepository) {
        this.testRepository = testRepository;
        this.practiceRepository = practiceRepository;
    }

    @Override
    @Transactional
    public void deleteTest(String slug) {
        Optional<TestEntity> test = testRepository.findBySlug(slug);

        if (!test.isPresent()) {

        }
        
        practiceRepository.deleteByTestId(test.get().getId());
        testRepository.deleteById(test.get().getId());
    }
    
}
