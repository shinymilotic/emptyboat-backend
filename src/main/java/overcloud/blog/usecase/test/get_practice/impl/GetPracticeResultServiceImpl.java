package overcloud.blog.usecase.test.get_practice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.PracticeRepository;
import overcloud.blog.usecase.test.get_practice.GetPracticeResultService;
import overcloud.blog.usecase.test.get_practice.PracticeResult;
import java.util.UUID;

@Service
public class GetPracticeResultServiceImpl implements GetPracticeResultService {
    private final PracticeRepository practiceRepository;

    GetPracticeResultServiceImpl(PracticeRepository practiceRepository) {
        this.practiceRepository = practiceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PracticeResult getPracticeResult(UUID practiceId) {
        return practiceRepository.getPracticeResult(practiceId);
    }

}
