package overcloud.blog.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import overcloud.blog.infrastructure.datetime.DateTimeFormat;

public class DateTimeTest {
    
    @Test
    public void hello() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.ddMMyyyyHHmm);
        String date = LocalDateTime.now().format(formatter).toString();
        System.out.println("fsafsafasfsa");
    }
}
