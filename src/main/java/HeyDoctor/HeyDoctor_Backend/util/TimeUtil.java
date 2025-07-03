package HeyDoctor.HeyDoctor_Backend.util;

import java.time.LocalDateTime;

public class TimeUtil {
    public static String now() {
        return LocalDateTime.now().toString();
    }
}
