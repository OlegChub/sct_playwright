package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BasicBouquetTypes {

    BASIC("Базовый букет"),
    IN_BASKET("Базовый букет в корзине"),
    IN_BOX("Базовый букет в коробке"),
    IN_HAT_BOX("Базовый букет в шляпной коробке"),
    IN_PUMPKIN("Базовый букет в тыкве"),
    IN_POT("Базовый букет в кашпо"),
    COMPOSITION("Базовый букет - композиция");

    private final String type;
}
