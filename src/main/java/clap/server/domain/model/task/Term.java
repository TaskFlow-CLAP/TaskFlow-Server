package clap.server.domain.model.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Term {
    ONE_HOUR(1, "1시간 이내"),
    ONE_DAY(24, "24시간 이내"),
    ONE_WEEK(168, "1주일 이내"),
    ONE_MONTH(730, "1개월 이내"),
    THREE_MONTHS(2190, "3개월 이내");

    private final int hours;
    private final String description;

    @JsonCreator
    public static Term fromDescription(String description) {
        for (Term term : values()) {
            if (term.getDescription().equals(description)) {
                return term;
            }
        }
        throw new IllegalArgumentException("Invalid description value: " + description);
    }
}