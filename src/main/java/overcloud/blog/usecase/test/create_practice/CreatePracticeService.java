package overcloud.blog.usecase.test.create_practice;

import org.springframework.stereotype.Service;

import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.test.common.PracticeRequest;

@Service
public interface CreatePracticeService {
    RestResponse<CreatePracticeResponse> createPractice(PracticeRequest practiceRequest);
}
