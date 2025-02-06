package overcloud.blog.usecase.test.get_list_test;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface GetListTestService {
    List<SimpleTestResponse> getListTest();
}
