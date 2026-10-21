package cz.vspj.smenarna;
/** Výukové přepočty; kurz znamená počet CZK za jednu jednotku cizí měny. */
public final class Money {
 public static double parse(String raw) {
  String s=raw.trim().replace(',', '.');
  if (!s.matches("[0-9]+([.][0-9]+)?")) throw new NumberFormatException();
  double value=Double.parseDouble(s);
  if((Double.isNaN(value)||Double.isInfinite(value))||value<0) throw new NumberFormatException();
  return value;
 }
 public static double convert(double value,double rate,boolean toForeign) {
  if((Double.isNaN(rate)||Double.isInfinite(rate))||rate<=0) throw new IllegalArgumentException();
  double result=toForeign ? value/rate : value*rate;
  if((Double.isNaN(result)||Double.isInfinite(result))) throw new NumberFormatException();
  return result;
 }
}
