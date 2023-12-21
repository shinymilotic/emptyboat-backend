package overcloud.blog.usecase.practice.create_practice;

import org.springframework.stereotype.Service;

import overcloud.blog.usecase.practice.core.PracticeRequest;

@Service
public interface CreatePracticeService {
    void createPractice(PracticeRequest practiceRequest);
}
