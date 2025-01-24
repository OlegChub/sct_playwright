package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Region {

    MOSCOW("Москва"),
    MOSCOW_REGION("Московская область");

    private final String region;
}
