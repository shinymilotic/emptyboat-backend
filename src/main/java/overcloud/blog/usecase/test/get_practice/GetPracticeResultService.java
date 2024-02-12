package overcloud.blog.usecase.test.get_practice;

import java.util.UUID;

public interface GetPracticeResultService {
    PracticeResult getPracticeResult(UUID practiceId);
}
