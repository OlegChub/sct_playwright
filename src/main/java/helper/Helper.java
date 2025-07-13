package helper;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

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

    public void printMapAsMapOf(Map<String, String> map) {
        System.out.println("Map<String, String> mapWithNamesAndPrices = new HashMap<>();");
        for (Map.Entry<String, String> mapEl : map.entrySet()) {
            String key = formatQuotes(mapEl.getKey());
            String value = mapEl.getValue();
            System.out.printf("map.put(\"%s\", \"%s\");\n", key, value);
        }
    }

    public void printListAsArraysAsList(List<String> list) {
        System.out.println("List<String> namesList = Arrays.asList(");
        for (var name : list) {
            System.out.printf("\"%s\",\n", formatQuotes(name));
        }
        System.out.println(");");
    }

    public String formatQuotes(String s) {
        if (s.contains("\"")) {
            return s.replace("\"", "\\\"");
        }
        return s;
    }

}
