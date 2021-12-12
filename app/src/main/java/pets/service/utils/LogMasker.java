package pets.service.utils;

import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;
import static pets.service.utils.Constants.LOG_MASKER_PATTERN;

public class LogMasker {

    /**
     * for SonarLint
     */
    private LogMasker() {
        throw new IllegalStateException("Utility class");
    }

    public static String maskDetails(String body) {
        if (body == null) {
            return null;
        } else {
            Matcher matcher = compile(quote("\"password\":\"") + LOG_MASKER_PATTERN + quote("\"")).matcher(body);
            if (matcher.find()) {
                body = matcher.replaceAll("\"password\":\"****\"");
            }

            matcher = compile(quote("\"email\":\"") + LOG_MASKER_PATTERN + quote("\"")).matcher(body);
            if (matcher.find()) {
                body = matcher.replaceAll("\"email\":\"****@****.***\"");
            }

            matcher = compile(quote("\"phone\":\"") + LOG_MASKER_PATTERN + quote("\"")).matcher(body);
            if (matcher.find()) {
                body = matcher.replaceAll("\"phone\":\"**********\"");
            }

            return body;
        }
    }
}
