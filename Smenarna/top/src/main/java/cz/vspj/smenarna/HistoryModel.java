package cz.vspj.smenarna;
import androidx.lifecycle.*;
import java.util.*;
import java.util.concurrent.*;
public class HistoryModel extends ViewModel {
 public static class State {
  final List<CnbClient.Point> points;final boolean loading,error;final String code;
  State(List<CnbClient.Point> p,boolean l,boolean e,String c){points=p;loading=l;error=e;code=c;}
 }
 public final MutableLiveData<State> data=new MutableLiveData<>();
 private final ExecutorService worker=Executors.newSingleThreadExecutor();
 private String cached; private int cachedYear; private int request;
 public void load(String code){
  int token=++request;data.setValue(new State(Collections.emptyList(),true,false,code));
  worker.execute(()->{
   try{
    int year=Calendar.getInstance(TimeZone.getTimeZone("Europe/Prague")).get(Calendar.YEAR);
    if(cached==null||cachedYear!=year){cached=CnbClient.download("rok.txt?rok="+year);cachedYear=year;}
    List<CnbClient.Point> points=CnbClient.parseYear(cached,code);
    if(points.size()<2){List<CnbClient.Point> previous=CnbClient.parseYear(CnbClient.download("rok.txt?rok="+(year-1)),code);previous.addAll(points);points=previous;}
    if(points.size()>60)points=new ArrayList<>(points.subList(points.size()-60,points.size()));
    final List<CnbClient.Point> output=points;
    new android.os.Handler(android.os.Looper.getMainLooper()).post(()->{if(token==request)data.setValue(new State(output,false,false,code));});
   }catch(Exception e){new android.os.Handler(android.os.Looper.getMainLooper()).post(()->{if(token==request)data.setValue(new State(Collections.emptyList(),false,true,code));});}
  });
 }
 @Override protected void onCleared(){worker.shutdownNow();}
}
