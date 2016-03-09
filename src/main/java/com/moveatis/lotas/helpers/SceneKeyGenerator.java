package com.moveatis.lotas.helpers;

import java.util.Random;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
public class SceneKeyGenerator {
    
    private static final int SCENE_KEY_LENGTH = 8;
    private static final int LETTERS_COUNT = 25;
    private static final int NUMBERS_COUNT = 10;
    
    private static final int CHOOSE_LETTER = 0;
    
    private static final char[] LETTERS = {'A','B','C','D','E','F','G','H',
            'I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y'};
    private static final int[] NUMBERS = {0,1,2,3,4,5,6,7,8,9};
    
    public SceneKeyGenerator() {
        
    }
    
    public static String getSceneKey() {
        Random randomizer = new Random();
        StringBuilder stb = new StringBuilder();
        int letterOrNumber = 0;
        
        for(int i = 0; i <= SCENE_KEY_LENGTH; i++) {
            letterOrNumber = randomizer.nextInt(2);
            if(letterOrNumber % 2 == CHOOSE_LETTER) {
                stb.append(LETTERS[randomizer.nextInt(LETTERS_COUNT)]);
            } else {
                stb.append(NUMBERS[randomizer.nextInt(NUMBERS_COUNT)]);
            }
        }
        
        return stb.toString();
    }
    
}
