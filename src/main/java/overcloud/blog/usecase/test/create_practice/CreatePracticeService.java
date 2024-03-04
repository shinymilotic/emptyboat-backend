package overcloud.blog.usecase.test.create_practice;

import org.springframework.stereotype.Service;
import overcloud.blog.usecase.test.common.PracticeRequest;

@Service
public interface CreatePracticeService {
    CreatePracticeResponse createPractice(PracticeRequest practiceRequest);
}
