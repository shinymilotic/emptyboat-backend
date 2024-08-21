package overcloud.blog.usecase.test.get_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestQuestion;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.Answer;
import overcloud.blog.usecase.test.common.EssayQuestion;
import overcloud.blog.usecase.test.common.Question;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.test.common.TestResponse;
import overcloud.blog.usecase.test.get_test.ChoiceQuestion;
import overcloud.blog.usecase.test.get_test.GetTestService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetTestServiceImpl implements GetTestService {
    private final ITestRepository testRepository;
    private final ResFactory resFactory;

    public GetTestServiceImpl(ITestRepository testRepository, ResFactory resFactory) {
        this.testRepository = testRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional
    public RestResponse<TestResponse> getTest(String id) {
        Optional<TestResponse> res = this.testRepository.getTestResponse(UUID.fromString(id));

        if (!res.isPresent()) {
            throw new InvalidDataException(resFactory.fail(TestResMsg.TEST_NOT_FOUND));
        }
        
        return resFactory.success(TestResMsg.TEST_CREATE_SUCCESS, res.get());
    }

}
