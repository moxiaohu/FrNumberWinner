package com.tiger.xiaohumo.frnumberwinner.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.xml.parsers.SAXParser;

/**
 * Created by xiaohumo on 27/10/15.
 */
public class NumberGenerator {


    private final static String[] months = new String[]{"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre",
            "Novembre", "Décembre"};
    private static Random random = new Random();

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = random.nextInt((max - min) + 1) + min;
        return randomNum;
    }


    // this function generates choices from min to max, the right choice is called target and put in the first place of the array
    public static ArrayList<String> generateNumberArray(int min, int max) {
        int[] ran = new int[]{-20, -10, 10, 20};
        ArrayList<String> integers;
        HashSet<String> set = new HashSet<>();

        int diff = min / 5;

        int target = randInt(min + diff, max - diff);
        set.add(String.valueOf(target));
        while (set.size() != 4) {
            int badChoice = target + NumberGenerator.randInt(-diff, diff);
            set.add(String.valueOf(badChoice));
        }
        set.remove(target);
        integers = new ArrayList<>(set);
        integers.add(0, String.valueOf(target));

        //add a number which has the same last number as target
        while (true) {
            int insertPos = NumberGenerator.randInt(1, 4);
            int subIndex = NumberGenerator.randInt(0, 3);
            int badChoice = target + ran[subIndex];

            if (!set.contains(badChoice) && badChoice >= min && badChoice <= max) {
                integers.add(insertPos, String.valueOf(badChoice));
                break;
            }
        }
        return integers;
    }

    public static ArrayList<ArrayList<String>> generateTelePhoneArray() {
        ArrayList<ArrayList<String>> numberLists = new ArrayList<>();

        String[] headset = new String[]{"06", "07", "08", "01"};
        String head = headset[randInt(0, headset.length - 1)];

        ArrayList<String> target = generateOneTelePhoneNumber(head);

        HashSet<ArrayList<String>> set = new HashSet<>();
        set.add(target);

        while (set.size() != 5) {
            set.add(generateOneBadTelePhoneNumber(target));
        }

        set.remove(target);
        numberLists = new ArrayList<ArrayList<String>>(set);
        numberLists.add(0, target);

        return numberLists;
    }

    public static ArrayList<String> generateOneTelePhoneNumber(String head) {

        ArrayList<String> numbers = new ArrayList<>();
        numbers.add(head);
        while (numbers.size() != 5) {
            int num = NumberGenerator.randInt(0, 99);
            if (num >= 0 && num < 10) {
                numbers.add("0" + num);
            } else {
                numbers.add(num + "");
            }
        }
        return numbers;
    }

    private static ArrayList<String> generateOneBadTelePhoneNumber(ArrayList<String> target) {

        ArrayList<String> badTarget = (ArrayList<String>) target.clone();
        int changePosition = randInt(1, 4);

        while (badTarget.get(changePosition).equals(target.get(changePosition))) {
            int changeValue = NumberGenerator.randInt(0, 99);

            if (changeValue >= 0 && changeValue < 10) {
                badTarget.set(changePosition, "0" + changeValue);
            } else {
                badTarget.set(changePosition, changeValue + "");
            }
        }
        return badTarget;
    }

    public static ArrayList<String> generateTimeList() {

        ArrayList<String> list = new ArrayList<>();
        HashSet<String> set = new HashSet<>();

        String target = createATime();
        set.add(target);

        while (set.size() != 5) {

            String time = createATime();
            set.add(time);
        }
        set.remove(target);
        list = new ArrayList<>(set);
        list.add(0, target);

        return list;
    }

    public static ArrayList<String> generateDateList() {
        ArrayList<String> list = new ArrayList<>();
        HashSet<String> set = new HashSet<>();

        String target = createADate();
        set.add(target);

        while (set.size() != 5) {

            set.add(createADate());
        }
        set.remove(target);
        list = new ArrayList<>(set);
        list.add(0, target);

        return list;
    }

    private static String createADate() {
        return randInt(1, 28) + " " + months[randInt(0, 11)] + " " + randInt(1900, 2016);
    }

    private static String createATime() {
        String hour = String.valueOf(randInt(1, 24));
        if (hour.equals("1")) {
            hour = "une heure ";
        } else {
            hour = hour + " heures ";
        }
        return hour + randInt(0, 59);
    }

    public static ArrayList<String> generateMoneyList() {
        ArrayList<String> list = new ArrayList<>();
        HashSet<String> set = new HashSet<>();

        String target = createADate();
        set.add(target);

        while (set.size() != 5) {

            set.add(createADate());
        }
        set.remove(target);
        list = new ArrayList<>(set);
        list.add(0, target);

        return list;
    }

    private static String createAMoney() {
        String euro = String.valueOf(randInt(1, 20));
        if (euro.equals("1")) {
            euro = "1 euro ";
        } else {
            euro = euro + " euros ";
        }
        return euro + randInt(1, 99) + "Centime";
    }
}
