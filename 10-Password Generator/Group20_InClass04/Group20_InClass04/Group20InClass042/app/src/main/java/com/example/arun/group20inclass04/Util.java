package com.example.arun.group20inclass04;

import java.util.Random;

public class Util {
    private static String _NUM = "";
    private static String _UCASE = "";
    private static String _LCASE = "";
    private static String _SYMB = "";
    private static final String _Rand = "13567RandomPASSWORD";

    public static String getPassword(String name, String dept, int age, int zip){
        Random rand = new Random();
        boolean num = true;
        boolean upper = true;
        boolean lower = true;
        boolean special = true;
        int length = 12;
        _NUM = name;
        _UCASE = dept;
        _LCASE = ""+zip+"";
        _SYMB = ""+age+"";

        String CHAR_SET = "";
        CHAR_SET = CHAR_SET.concat(_NUM);
        CHAR_SET = CHAR_SET.concat(_UCASE);
        CHAR_SET = CHAR_SET.concat(_LCASE);
        CHAR_SET = CHAR_SET.concat(_SYMB);
        CHAR_SET = CHAR_SET.concat(_Rand);
        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<length; i++){ //using randomly generated size of password
            int randomIndex = getRandomIndex(CHAR_SET.length()); //get random index
            char ch = CHAR_SET.charAt(randomIndex); //select character at random index
            randStr.append(ch); //append selected character to password substring
        }
        return  randStr.toString(); //return password string
    }

    private static int getRandomIndex(int len){
        Random rand = new Random();
        int randomInt = 0;
        for(int i=0; i<10000; i++){
            for(int j=0; j<1000;j++){
                randomInt = rand.nextInt(len);
            }
        }
        return randomInt;
    }
}
