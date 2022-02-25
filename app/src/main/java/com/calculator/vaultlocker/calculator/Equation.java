package com.calculator.vaultlocker.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Equation extends ArrayList<String> {
    public String getText() {
        Iterator<String> it = iterator();
        StringBuilder str = new StringBuilder();
        while (it.hasNext()) {
            str.append(it.next()).append(" ");
        }
        return str.toString();
    }

    public void setText(String str) {
        while (size() > 0) {
            removeLast();
        }
        if (str.length() > 0) {
            this.addAll(Arrays.asList(str.split(" ")));
        }
    }

    public void attachToLast(char c) {
        if (size() == 0) {
            add("" + c);
            return;
        }
        set(size() - 1, getLast() + c);
    }

    public void detachFromLast() {
        if (getLast().length() > 0) {
            set(size() - 1, getLast().substring(0, getLast().length() - 1));
        }
    }

    public void removeLast() {
        if (size() > 0) {
            remove(size() - 1);
        }
    }

    public String getLast() {
        return getRecent(0);
    }

    public char getLastChar() {
        String last = getLast();
        return last.length() > 0 ? last.charAt(last.length() - 1) : ' ';
    }

    public String getRecent(int i) {
        if (size() <= i) {
            return "";
        }
        return get((size() - i) - 1);
    }

    public boolean isNumber(int i) {
        String recent = getRecent(i);
        if (recent != null && recent.length() > 0) {
            char charAt = recent.charAt(0);
            return isRawNumber(i) || charAt == 960 || charAt == 'e' || charAt == ')' || charAt == '!';
        }
        return false;
    }

    public boolean isOperator(int i) {
        char charAt;
        String recent = getRecent(i);
        return recent != null && recent.length() == 1 && ((charAt = recent.charAt(0)) == '/' || charAt == '*' || charAt == '-' || charAt == '+' || charAt == '%');
    }

    public boolean isRawNumber(int i) {
        String recent = getRecent(i);
        return recent != null && recent.length() > 0 && (Character.isDigit(recent.charAt(0)) || (recent.charAt(0) == '-' && isStartCharacter(i + 1) && (recent.length() == 1 || Character.isDigit(recent.charAt(1)))));
    }

    public boolean isStartCharacter(int i) {
        String recent = getRecent(i);
        if (recent.length() > 0) {
            char charAt = recent.charAt(0);
            if (recent.length() > 1 && charAt == '-') {
                charAt = recent.charAt(1);
            }
            if (charAt == 8730 || charAt == 's' || charAt == 'c' || charAt == 't' || charAt == 'n' || charAt == 'l' || charAt == '(' || charAt == '/' || charAt == '*' || charAt == '-' || charAt == '+' || charAt == '%') {
                return true;
            }
        }
        return recent.equals("");
    }

    public int numOf(char c) {
        int i = 0;
        for (int i2 = 0; i2 < getText().length(); i2++) {
            if (getText().charAt(i2) == c) {
                i++;
            }
        }
        return i;
    }
}
