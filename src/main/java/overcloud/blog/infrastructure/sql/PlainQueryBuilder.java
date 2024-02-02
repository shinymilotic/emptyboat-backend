package overcloud.blog.infrastructure.sql;

import org.springframework.stereotype.Component;

@Component
public class PlainQueryBuilder {

    public int getOffset(int page, int limit) {
        int offset = limit * (page - 1);

        return offset;
    }
}
