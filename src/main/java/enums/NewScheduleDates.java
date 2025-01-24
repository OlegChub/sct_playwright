package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NewScheduleDates {

    DEC_31("31.12.2024"),
    JAN_01("01.01.2025"),
    JAN_02("02.01.2025"),
    JAN_03("03.01.2025"),
    JAN_04("04.01.2025"),
    JAN_05("05.01.2025");

    private final String date;
}
