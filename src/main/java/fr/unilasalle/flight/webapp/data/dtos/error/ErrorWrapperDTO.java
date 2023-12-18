package fr.unilasalle.flight.webapp.data.dtos.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorWrapperDTO implements Serializable {
    private String message;

    private final List<String> errors = new ArrayList<>();

    public boolean isMessageEmpty() {
        return StringUtils.isBlank(message);
    }

    public boolean isErrorsEmpty() {
        return errors.isEmpty();
    }

    public boolean hasEveryInformation() {
        return !(isMessageEmpty() || isErrorsEmpty());
    }

    public boolean isEmpty() {
        return isMessageEmpty() && isErrorsEmpty();
    }
}
