package helper;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

import static enums.BasicBouquetTypes.*;

@UtilityClass
public class Helper {

    public String getBasicBouquet(String bouquetName) {
        if (bouquetName.contains("в корзине")) {
            return IN_BASKET.getType();
        } else if (bouquetName.contains("в коробке")) {
            return IN_BOX.getType();
        } else if (bouquetName.contains("в шляпной коробке")) {
            return IN_HAT_BOX.getType();
        } else if (bouquetName.contains("в кашпо")) {
            return IN_POT.getType();
        } else if (bouquetName.contains("в тыкве")) {
            return IN_PUMPKIN.getType();
        } else if (bouquetName.contains("Композиция")) {
            return COMPOSITION.getType();
        } else {
            return BASIC.getType();
        }
    }

}
