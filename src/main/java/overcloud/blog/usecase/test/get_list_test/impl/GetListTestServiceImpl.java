package overcloud.blog.usecase.test.get_list_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.test.get_list_test.GetListTestService;
import overcloud.blog.usecase.test.get_list_test.TestListRecord;
import overcloud.blog.usecase.test.get_list_test.SimpleTestResponse;
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
    public RestResponse<List<SimpleTestResponse>> getListTest() {
        List<TestListRecord> testList = testRepository.findAll();
        List<SimpleTestResponse> responses = (new ArrayList<>());
        for (TestListRecord test : testList) {
            String title = test.getTitle();
            String description = test.getDescription();
            String id = test.getId().toString();
            responses.add(new SimpleTestResponse(id, title, description));
        }

        return resFactory.success(TestResMsg.TEST_GET_LIST, responses);
    }
}
