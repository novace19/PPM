import cz.vspj.smenarna.*;
public class LogicCheck {
 static void near(double a,double b){if(Math.abs(a-b)>1e-9)throw new AssertionError(a+" != "+b);}
 public static void main(String[] args){
  near(Money.parse(" 12,50 "),12.5);near(Money.parse("0"),0);
  for(String bad:new String[]{"","-1","NaN","Infinity","1,2,3","1e3","1.2.3","9".repeat(400)}){
   try{Money.parse(bad);throw new AssertionError(bad);}catch(NumberFormatException expected){}
  }
  near(Money.convert(2500,25,true),100);near(Money.convert(100,25,false),2500);
  for(double rate:new double[]{25,23,29.5,5.8})near(Money.convert(Money.convert(1234.56,rate,true),rate,false),1234.56);
  try{Money.convert(Double.MAX_VALUE,25,false);throw new AssertionError();}catch(NumberFormatException expected){}
  String day="20.10.2026 #200\nzemě|měna|množství|kód|kurz\nEMU|euro|1|EUR|25,000\nUSA|dolar|1|USD|23,000\nBritánie|libra|1|GBP|29,500\nPolsko|zlotý|1|PLN|5,800\nJaponsko|jen|100|JPY|15,000\n";
  CnbClient.Rates r=CnbClient.parseDaily(day);near(r.values.get("JPY"),.15);near(r.values.get("EUR"),25);
  if(!r.date.equals("20.10.2026"))throw new AssertionError();
  var p=CnbClient.parseYear("Datum|1 EUR|100 JPY\n19.10.2026|25,0|15,0\n20.10.2026|24,9|15,2\n","JPY");
  near(p.get(1).value,.152);if(p.size()!=2)throw new AssertionError();
  if(!CnbClient.parseYear("Datum|1 EUR\n20.10.2026|25,0\n","USD").isEmpty())throw new AssertionError();
  try{CnbClient.parseDaily("<html>chyba</html>");throw new AssertionError();}catch(IllegalArgumentException expected){}
  System.out.println("OK: vstupy, prevody, preteceni, denni a rocni format CNB, mnozstvi a chybejici mena.");
 }
}
