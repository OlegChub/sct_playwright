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
    JAN_05("05.01.2025"),
    FEB_14("14.02.2025"),
    MAR_07("07.03.2025"),
    MAR_08("08.03.2025"),
    AUG_31("31.08.2025"),
    SEPT_01("01.09.2025");

    private final String date;
}
