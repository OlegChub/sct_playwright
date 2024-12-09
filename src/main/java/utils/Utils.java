package utils;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class Utils {

    public List<String> replaceCharsInList(List<String> listToModify, String[] arrayOfItemsToReplace) {
        List<String> modifiedList = new ArrayList<>();
        for (String el : listToModify) {
            String newValue =
                    el.replace(arrayOfItemsToReplace[0], " «")
                            .replace(arrayOfItemsToReplace[1], "»");
            modifiedList.add(newValue);
        }
        return modifiedList;
    }

    public void replaceWebElementsText(List<WebElement> listWithWebElements, List<String> listWithNewText) {
        for (int i = 0; i < listWithWebElements.size(); i++) {
            listWithWebElements.get(i).clear();
            listWithWebElements.get(i).sendKeys(listWithNewText.get(i));
        }
    }

    public int generateRandomNumber() {
        Random random = new Random();
        // Generate a random number between 1700 and 2500 (inclusive)
        int min = 1700;
        int max = 2500;
        return random.nextInt(max - min + 1) + min;
    }

}
