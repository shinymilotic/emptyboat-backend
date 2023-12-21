package overcloud.blog.infrastructure.datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class DateTimeService {

    public String nowDateToString(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.now().format(formatter).toString();
    }

    public String nowDateToString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.ddMMyyyyHHmm);
        return LocalDateTime.now().format(formatter).toString();
    }

    public String dateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.ddMMyyyyHHmm);
        return dateTime.format(formatter).toString();
    }
}
