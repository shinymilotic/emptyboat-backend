package overcloud.blog.application.practice.create_practice;

import org.springframework.stereotype.Service;

import overcloud.blog.application.practice.core.PracticeRequest;

@Service
public interface CreatePracticeService {
    void createPractice(PracticeRequest practiceRequest);
}
