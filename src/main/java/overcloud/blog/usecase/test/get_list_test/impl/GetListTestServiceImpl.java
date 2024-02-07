package overcloud.blog.usecase.test.get_list_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.test.get_list_test.GetListTestService;
import overcloud.blog.usecase.test.get_list_test.TestListRecord;
import overcloud.blog.usecase.test.get_list_test.TestListResponse;
import overcloud.blog.usecase.test.get_list_test.TestResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetListTestServiceImpl implements GetListTestService {

    private final ITestRepository testRepository;

    public GetListTestServiceImpl(ITestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    @Transactional
    public TestListResponse getListTest() {
        List<TestListRecord> testList = testRepository.findAll();
        TestListResponse responses = new TestListResponse(new ArrayList<>());
        for (TestListRecord test : testList) {
            String title = test.getTitle();
            String slug = test.getSlug();
            responses.getTests().add(new TestResponse(title, slug));
        }

        return responses;
    }
}
