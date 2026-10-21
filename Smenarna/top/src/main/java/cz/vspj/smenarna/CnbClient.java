package cz.vspj.smenarna;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
/** Veřejné TXT rozhraní ČNB. Síťové metody patří výhradně na pracovní vlákno. */
public final class CnbClient {
 private static final String BASE="https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/";
 public static class Rates {
  public String date;
  public final Map<String,Double> values=new TreeMap<>();
 }
 public static class Point {
  public final String date; public final double value;
  public Point(String d,double v){date=d;value=v;}
 }
 public static String download(String file)throws IOException {
  HttpURLConnection c=(HttpURLConnection)new URL(BASE+file).openConnection();
  c.setConnectTimeout(12000);c.setReadTimeout(15000);
  try {
   if(c.getResponseCode()!=200)throw new IOException("HTTP "+c.getResponseCode());
   try(BufferedReader r=new BufferedReader(new InputStreamReader(c.getInputStream(),StandardCharsets.UTF_8))){
    StringBuilder b=new StringBuilder();String line;
    while((line=r.readLine())!=null)b.append(line).append('\n');
    return b.toString();
   }
  }finally{c.disconnect();}
 }
 private static double number(String v){double x=Double.parseDouble(v.replace(',','.'));if(!Double.isFinite(x)||x<=0)throw new IllegalArgumentException();return x;}
 public static Rates parseDaily(String txt){
  String[] rows=txt.replace("\uFEFF", "").split("\\r?\\n");
  if(rows.length<3||!rows[0].matches("\\d{2}\\.\\d{2}\\.\\d{4} #\\d+"))throw new IllegalArgumentException();
  Rates result=new Rates();result.date=rows[0].split(" ")[0];
  for(int i=2;i<rows.length;i++){
   String[] cells=rows[i].split("\\|");
   if(cells.length!=5)throw new IllegalArgumentException();
   result.values.put(cells[3],number(cells[4])/number(cells[2]));
  }
  for(String code:new String[]{"EUR","USD","GBP","PLN"})if(!result.values.containsKey(code))throw new IllegalArgumentException();
  return result;
 }
 public static List<Point> parseYear(String txt,String code){
  String[] rows=txt.replace("\uFEFF", "").split("\\r?\\n");String[] head=rows[0].split("\\|");
  int column=-1;double amount=1;
  for(int i=1;i<head.length;i++){
   String[] parts=head[i].trim().split("\\s+");
   if(parts.length==2&&parts[1].equals(code)){column=i;amount=number(parts[0]);break;}
  }
  List<Point> points=new ArrayList<>();if(column<0)return points;
  for(int i=1;i<rows.length;i++){
   String[] cells=rows[i].split("\\|");
   if(cells.length>column&&!cells[column].isEmpty())points.add(new Point(cells[0],number(cells[column])/amount));
  }
  return points;
 }
}
