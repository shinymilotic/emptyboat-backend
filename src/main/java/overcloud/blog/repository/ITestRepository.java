package overcloud.blog.repository;

import overcloud.blog.usecase.test.get_list_test.TestListRecord;

import java.util.List;

public interface ITestRepository {
    List<TestListRecord> findAll();
}
