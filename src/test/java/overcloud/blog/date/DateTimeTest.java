package overcloud.blog.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import overcloud.blog.datetime.DateTimeFormat;

public class DateTimeTest {
    
    @Test
    public void hello() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.ddMMyyyyHHmm);
        System.out.println(LocalDateTime.now().format(formatter).toString());
    }
}
