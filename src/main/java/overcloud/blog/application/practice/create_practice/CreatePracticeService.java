package overcloud.blog.application.practice.create_practice;

import overcloud.blog.application.practice.core.PracticeRequest;

public interface CreatePracticeService {
    boolean createPractice(PracticeRequest practiceRequest);
}
