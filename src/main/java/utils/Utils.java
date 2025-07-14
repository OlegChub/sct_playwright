package utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class Utils {

    public int generateRandomNumber() {
        Random random = new Random();
        // Generate a random number between 1700 and 2500 (inclusive)
        int min = 1700;
        int max = 2500;
        return random.nextInt(max - min + 1) + min;
    }

}
