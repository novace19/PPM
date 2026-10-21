package cz.vspj.smenarna;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.Locale;
public class RatesFragment extends Fragment {
 private LinearLayout list;
 @Override public View onCreateView(LayoutInflater i,ViewGroup parent,Bundle b){
  ScrollView scroll=new ScrollView(requireContext());list=new LinearLayout(requireContext());list.setOrientation(LinearLayout.VERTICAL);list.setPadding(12,12,12,12);scroll.addView(list);return scroll;
 }
 @Override public void onViewCreated(View v,Bundle b){
  RatesModel m=new ViewModelProvider(requireActivity()).get(RatesModel.class);
  m.data.observe(getViewLifecycleOwner(),s->{
   list.removeAllViews();add(getString(s.error?R.string.network_error:R.string.loading));
   if(s.rates!=null){list.removeAllViews();if(s.error)add(getString(R.string.network_error));add(getString(R.string.rate_date,s.rates.date));
    for(java.util.Map.Entry<String,Double> row:s.rates.values.entrySet())add(getString(R.string.rate_row,row.getKey(),"1",row.getKey(),String.format(Locale.forLanguageTag("cs-CZ"),"%.4f",row.getValue())));
   }
   Button retry=new Button(requireContext());retry.setText(R.string.refresh);retry.setEnabled(!s.loading);retry.setOnClickListener(x->m.load());list.addView(retry);
  });
  if(m.data.getValue()==null)m.load();
 }
 private void add(String value){TextView t=new TextView(requireContext());t.setText(value);t.setTextSize(17);t.setPadding(8,12,8,12);list.addView(t);}
 @Override public void onDestroyView(){super.onDestroyView();list=null;}
}
