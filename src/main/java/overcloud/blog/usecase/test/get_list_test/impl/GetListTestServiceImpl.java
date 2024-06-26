package overcloud.blog.usecase.test.get_list_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.test.get_list_test.GetListTestService;
import overcloud.blog.usecase.test.get_list_test.TestListRecord;
import overcloud.blog.usecase.test.get_list_test.TestListResponse;
import overcloud.blog.usecase.test.get_list_test.TestResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetListTestServiceImpl implements GetListTestService {
    private final ITestRepository testRepository;
    private final ResFactory resFactory;

    public GetListTestServiceImpl(ITestRepository testRepository, ResFactory resFactory) {
        this.testRepository = testRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional
    public RestResponse<TestListResponse> getListTest() {
        List<TestListRecord> testList = testRepository.findAll();
        TestListResponse responses = new TestListResponse(new ArrayList<>());
        for (TestListRecord test : testList) {
            String title = test.getTitle();
            String slug = test.getSlug();
            String description = test.getDescription();

            responses.getTests().add(new TestResponse(title, description, slug));
        }

        return resFactory.success(TestResMsg.TEST_GET_LIST, responses);
    }
}
