package cz.vspj.bmi;

/** Výpočet BMI a validace vstupů nezávislá na Android UI. */
public final class BmiCalculator {
    private BmiCalculator() { }

    /** Výška v cm musí být kladné celé číslo; výstup je double podle zadání. */
    public static double parseHeight(String text) throws NumberFormatException {
        if (text == null || !text.trim().matches("[0-9]+")) throw new NumberFormatException();
        return positive(Double.parseDouble(text.trim()));
    }

    /** Hmotnost může obsahovat desetinnou tečku nebo čárku. */
    public static double parseWeight(String text) throws NumberFormatException {
        if (text == null || !text.trim().matches("(?:[0-9]+(?:[.,][0-9]*)?|[.,][0-9]+)"))
            throw new NumberFormatException();
        return positive(Double.parseDouble(text.trim().replace(',', '.')));
    }

    private static double positive(double value) {
        if ((Double.isNaN(value) || Double.isInfinite(value)) || value <= 0) throw new NumberFormatException();
        return value;
    }

    /** Vrátí BMI = kg / (cm / 100)^2. Odmítne nulové a nekonečné hodnoty. */
    public static double calculate(double heightCm, double weightKg) {
        positive(heightCm);
        positive(weightKg);
        double heightM = heightCm / 100.0;
        double bmi = weightKg / (heightM * heightM);
        return positive(bmi);
    }

    /** Index kategorie odpovídá pořadí string-array bmi_category_names. */
    public static int categoryIndex(double bmi) {
        positive(bmi);
        if (bmi < 18.5) return 0;
        if (bmi < 25) return 1;
        if (bmi < 30) return 2;
        if (bmi < 35) return 3;
        if (bmi < 40) return 4;
        return 5;
    }
}
