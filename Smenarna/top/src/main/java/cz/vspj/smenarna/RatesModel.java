package cz.vspj.smenarna;
import androidx.lifecycle.*;
import java.util.concurrent.*;
public class RatesModel extends ViewModel {
 public static class State {
  public final CnbClient.Rates rates; public final boolean loading,error;
  State(CnbClient.Rates r,boolean l,boolean e){rates=r;loading=l;error=e;}
 }
 public final MutableLiveData<State> data=new MutableLiveData<>();
 private final ExecutorService worker=Executors.newSingleThreadExecutor();
 public void load(){
  State old=data.getValue();if(old!=null&&old.loading)return;
  data.setValue(new State(old==null?null:old.rates,true,false));
  worker.execute(()->{try{data.postValue(new State(CnbClient.parseDaily(CnbClient.download("denni_kurz.txt")),false,false));}
   catch(Exception e){data.postValue(new State(old==null?null:old.rates,false,true));}});
 }
 @Override protected void onCleared(){worker.shutdownNow();}
}
