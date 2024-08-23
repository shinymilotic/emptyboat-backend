package overcloud.blog.core.datetime;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeService {

    public String nowDateToString(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.now().format(formatter);
    }

    public String nowDateToString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.ddMMyyyyHHmm);
        return LocalDateTime.now().format(formatter);
    }

    public String dateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.ddMMyyyyHHmm);
        return dateTime.format(formatter);
    }
}
