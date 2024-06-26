package overcloud.blog.usecase.test.get_practice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.PracticeResMsg;
import overcloud.blog.usecase.test.get_practice.GetPracticeResultService;
import overcloud.blog.usecase.test.get_practice.PracticeResult;
import java.util.UUID;

@Service
public class GetPracticeResultServiceImpl implements GetPracticeResultService {
    private final IPracticeRepository practiceRepository;
    private final ResFactory resFactory;

    GetPracticeResultServiceImpl(IPracticeRepository practiceRepository, ResFactory resFactory) {
        this.practiceRepository = practiceRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public RestResponse<PracticeResult> getPracticeResult(UUID practiceId) {
        PracticeResult res = practiceRepository.getPracticeResult(practiceId);
        return resFactory.success(PracticeResMsg.PRACTICE_GETRESULT_SUCCESS, res);
    }

}
