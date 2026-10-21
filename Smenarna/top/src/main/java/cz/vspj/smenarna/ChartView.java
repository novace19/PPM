package cz.vspj.smenarna;
import android.content.Context;
import android.graphics.*;
import android.view.View;
import java.util.*;
/** Vlastní čárový graf bez další síťové nebo grafové knihovny. */
public class ChartView extends View {
 private List<CnbClient.Point> points=Collections.emptyList();
 private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
 public ChartView(Context c){super(c);setMinimumHeight(500);}
 public void setPoints(List<CnbClient.Point> p){points=p;invalidate();}
 @Override protected void onDraw(Canvas c){
  super.onDraw(c);if(points.size()<2)return;
  float d=getResources().getDisplayMetrics().density,left=76*d,right=getWidth()-16*d,top=28*d,bottom=getHeight()-48*d;
  if(right<=left||bottom<=top)return;
  double min=Double.MAX_VALUE,max=-Double.MAX_VALUE;
  for(CnbClient.Point p:points){min=Math.min(min,p.value);max=Math.max(max,p.value);}
  double pad=Math.max((max-min)*.12,.01);min-=pad;max+=pad;
  paint.setTextSize(12*d);paint.setStrokeWidth(d);
  for(int i=0;i<5;i++){float y=top+(bottom-top)*i/4;paint.setColor(0xffcedde5);c.drawLine(left,y,right,y,paint);paint.setColor(0xff17334d);c.drawText(String.format(Locale.forLanguageTag("cs-CZ"),"%.3f",max-(max-min)*i/4),0,y,paint);}
  Path path=new Path();
  // Časová osa respektuje skutečné kalendářní rozestupy, včetně víkendů.
  java.text.SimpleDateFormat fmt=new java.text.SimpleDateFormat("dd.MM.yyyy",Locale.ROOT);
  long first=0,last=1;try{first=fmt.parse(points.get(0).date).getTime();last=fmt.parse(points.get(points.size()-1).date).getTime();}catch(java.text.ParseException e){return;}
  if(last<=first)return;
  for(int i=0;i<points.size();i++){
   long time;try{time=fmt.parse(points.get(i).date).getTime();}catch(java.text.ParseException e){return;}
   float x=left+(right-left)*(float)((double)(time-first)/(last-first));float y=bottom-(float)((points.get(i).value-min)/(max-min))*(bottom-top);
   if(i==0)path.moveTo(x,y);else path.lineTo(x,y);
  }
  paint.setColor(0xff176b83);paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(3*d);c.drawPath(path,paint);paint.setStyle(Paint.Style.FILL);
  paint.setColor(0xff17334d);String start=points.get(0).date,end=points.get(points.size()-1).date;c.drawText(start,left,bottom+24*d,paint);c.drawText(end,right-paint.measureText(end),bottom+24*d,paint);
 }
}
