package overcloud.blog.usecase.test.get_list_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
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

    public GetListTestServiceImpl(ITestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    @Transactional
    public List<SimpleTestResponse> getListTest() {
        List<TestListRecord> testList = testRepository.findAll();
        List<SimpleTestResponse> responses = (new ArrayList<>());
        for (TestListRecord test : testList) {
            String title = test.getTitle();
            String description = test.getDescription();
            String id = test.getId().toString();
            responses.add(new SimpleTestResponse(id, title, description));
        }

        return responses;
    }
}
