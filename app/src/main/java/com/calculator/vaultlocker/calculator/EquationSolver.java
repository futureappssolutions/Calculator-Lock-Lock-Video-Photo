package com.calculator.vaultlocker.calculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.commons.math3.special.Gamma;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class EquationSolver {
    private final SharedPreferences sp;

    public EquationSolver(Context context) {
        this.sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String solve(String str) {
        return solveBasicOperators(solveBasicOperators(solveBasicOperators(solveAdvancedOperators(str.replace("π", "3.141592653589793").replace("e", "2.718281828459045").replace("n ", "ln ( ").replace("l ", "log ( ").replace("√ ", "√ ( ").replace("s ", "sin ( ").replace("c ", "cos ( ").replace("t ", "tan ( ")), " % ", " % "), " * ", " / "), " + ", " - ");
    }

    private String solveAdvancedOperators(String str) {
        double d;
        String str2;
        String str3;
        String str4 = str;
        while (numOfOccurrences('(', str4) > numOfOccurrences(')', str4)) {
            str4 = str4 + ") ";
        }
        while (str4.contains("(")) {
            int indexOf = str4.indexOf(40);
            int i = indexOf;
            int i2 = 0;
            while (true) {
                if (i >= str4.length()) {
                    i = 0;
                    break;
                }
                if (str4.charAt(i) == '(') {
                    i2++;
                }
                if (str4.charAt(i) == ')') {
                    i2--;
                }
                if (i2 == 0) {
                    break;
                }
                i++;
            }
            str4 = str4.substring(0, indexOf) + solveAdvancedOperators(str4.substring(indexOf + 2, i)) + " " + str4.substring(i + 2);
        }
        while (str4.contains("ln")) {
            int indexOf2 = str4.indexOf("ln");
            int i3 = indexOf2 + 3;
            int indexOf3 = str4.indexOf(32, i3);
            str4 = str4.substring(0, indexOf2) + Math.log(Double.parseDouble(solveAdvancedOperators(str4.substring(i3, indexOf3)))) + str4.substring(indexOf3);
        }
        while (str4.contains("log")) {
            int indexOf4 = str4.indexOf("log");
            int i4 = indexOf4 + 4;
            int indexOf5 = str4.indexOf(32, i4);
            str4 = str4.substring(0, indexOf4) + Math.log10(Double.parseDouble(solveAdvancedOperators(str4.substring(i4, indexOf5)))) + str4.substring(indexOf5);
        }
        while (true) {
            d = 1.0E10d;
            if (str4.contains("sin")) {
                int indexOf6 = str4.indexOf("sin");
                int i5 = indexOf6 + 4;
                int indexOf7 = str4.indexOf(32, i5);
                double parseDouble = Double.parseDouble(solveAdvancedOperators(str4.substring(i5, indexOf7)));
                if (!this.sp.getBoolean("pref_radians", false)) {
                    parseDouble *= 0.017453292519943295d;
                }
                double sin = Math.sin(parseDouble);
                if (Math.abs(sin) < 1.0E-11d) {
                    sin = 0.0d;
                }
                if (Math.abs(sin) > 1.0E10d) {
                    sin /= 0.0d;
                }
                str4 = str4.substring(0, indexOf6) + sin + str4.substring(indexOf7);
            }
            break;
        }
        while (str4.contains("cos")) {
            int indexOf8 = str4.indexOf("cos");
            int i6 = indexOf8 + 4;
            int indexOf9 = str4.indexOf(32, i6);
            double parseDouble2 = Double.parseDouble(solveAdvancedOperators(str4.substring(i6, indexOf9)));
            if (!this.sp.getBoolean("pref_radians", false)) {
                parseDouble2 *= 0.017453292519943295d;
            }
            double cos = Math.cos(parseDouble2);
            if (Math.abs(cos) < 1.0E-11d) {
                cos = 0.0d;
            }
            if (Math.abs(cos) > d) {
                cos /= 0.0d;
            }
            str4 = str4.substring(0, indexOf8) + cos + str4.substring(indexOf9);
            d = 1.0E10d;
        }
        while (str4.contains("tan")) {
            int indexOf10 = str4.indexOf("tan");
            int i7 = indexOf10 + 4;
            int indexOf11 = str4.indexOf(32, i7);
            double parseDouble3 = Double.parseDouble(solveAdvancedOperators(str4.substring(i7, indexOf11)));
            if (!this.sp.getBoolean("pref_radians", false)) {
                parseDouble3 *= 0.017453292519943295d;
            }
            double tan = Math.tan(parseDouble3);
            if (Math.abs(tan) < 1.0E-11d) {
                tan = 0.0d;
            }
            if (Math.abs(tan) > 1.0E10d) {
                tan /= 0.0d;
            }
            str4 = str4.substring(0, indexOf10) + tan + str4.substring(indexOf11);
        }
        while (str4.contains("√")) {
            int indexOf12 = str4.indexOf(8730);
            int i8 = indexOf12 + 2;
            int indexOf13 = str4.indexOf(32, i8);
            str4 = str4.substring(0, indexOf12) + Math.sqrt(Double.parseDouble(solveAdvancedOperators(str4.substring(i8, indexOf13)))) + str4.substring(indexOf13);
        }
        while (true) {
            str2 = "";
            if (str4.contains("!")) {
                String str5 = " " + str4 + " ";
                String substring = str5.substring(str5.lastIndexOf(" ", str5.indexOf("!") - 2) + 1, str5.indexOf("!") - 1);
                try {
                    str3 = str5.substring(1, str5.lastIndexOf(" ", str5.indexOf("!") - 2));
                } catch (Exception unused) {
                    str3 = str2;
                }
                String str6 = str2 + Gamma.digamma(Double.parseDouble(substring) + 1.0d);
                try {
                    str2 = str5.substring(str5.indexOf("!") + 2);
                } catch (Exception unused2) {
                }
                str4 = str3 + " " + str6 + " " + str2;
            }
            break;
        }
        while (str4.contains("%")) {
            String str7 = " " + str4 + " ";
            String substring2 = str7.substring(str7.lastIndexOf(" ", str7.indexOf("%") - 2) + 1, str7.indexOf("%") - 1);
            try {
                str7.substring(1, str7.lastIndexOf(" ", str7.indexOf("%") / 100));
            } catch (Exception unused3) {
            }
            double parseDouble4 = Double.parseDouble(substring2) / 100.0d;
            Log.e("percentage", parseDouble4 + str2);
            try {
                str7.substring(str7.indexOf("%") + 2);
            } catch (Exception unused4) {
            }
            str4 = Double.toString(parseDouble4);
        }
        return str4;
    }

    private String solveBasicOperators(String str, String str2, String str3) {
        String str4 = " " + str + " ";
        while (true) {
            if (!str4.contains(str2) && !str4.contains(str3)) {
                return str4.trim();
            }
            String str5 = str4.indexOf(str3) < str4.indexOf(str2) ? str3 : str2;
            if (!str4.contains(str2)) {
                str5 = str3;
            }
            if (!str4.contains(str3)) {
                str5 = str2;
            }
            String substring = str4.substring(0, str4.indexOf(str5));
            String substring2 = str4.substring(str4.indexOf(str5) + 3);
            double parseDouble = Double.parseDouble(substring.substring(substring.lastIndexOf(32) + 1));
            double parseDouble2 = Double.parseDouble(substring2.substring(0, substring2.indexOf(32)));
            String trim = substring.substring(0, substring.lastIndexOf(32)).trim();
            String trim2 = substring2.substring(substring2.indexOf(32), substring2.length()).trim();
            int hashCode = str5.hashCode();
            if (hashCode != 31931) {
                if (hashCode != 32086) {
                    if (hashCode != 32117) {
                        if (hashCode != 32179) {
                            if (hashCode == 32241 && str5.equals(" / ")) {
                                str4 = " " + trim + " " + (parseDouble / parseDouble2) + " " + trim2 + " ";
                            }
                        } else if (str5.equals(" - ")) {
                            str4 = " " + trim + " " + (parseDouble - parseDouble2) + " " + trim2 + " ";
                        }
                    } else if (str5.equals(" + ")) {
                        str4 = " " + trim + " " + (parseDouble + parseDouble2) + " " + trim2 + " ";
                    }
                } else if (str5.equals(" * ")) {
                    str4 = " " + trim + " " + (parseDouble * parseDouble2) + " " + trim2 + " ";
                }
            } else if (str5.equals(" % ")) {
                str4 = " " + trim + " " + (parseDouble / 100.0d) + " " + trim2 + " ";
            } else {
                str4 = " " + trim + " 0.0 " + trim2 + " ";
            }
        }
    }

    public String formatNumber(String str) {
        if (str.contains("∞") || str.contains("Infinity") || str.contains("NaN")) {
            return str;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.########E0");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        String format = decimalFormat.format(Double.parseDouble(str));
        if (Math.abs(Double.parseDouble(format.substring(format.indexOf("E") + 1))) >= 8.0d) {
            return format;
        }
        decimalFormat.applyPattern("#.########");
        return decimalFormat.format(Double.parseDouble(str));
    }

    private int numOfOccurrences(char c, String str) {
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == c) {
                i++;
            }
        }
        return i;
    }
}
