package cz.vspj.smenarna;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
public class ChartFragment extends Fragment {
 private TextView status;private ChartView graph;private HistoryModel model;private Spinner spinner;
 @Override public View onCreateView(LayoutInflater i,ViewGroup p,Bundle b){
  LinearLayout box=new LinearLayout(requireContext());box.setOrientation(LinearLayout.VERTICAL);box.setPadding(12,12,12,12);
  spinner=new Spinner(requireContext());spinner.setAdapter(new ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.currencies)));box.addView(spinner);
  status=new TextView(requireContext());status.setTextSize(16);box.addView(status);
  Button retry=new Button(requireContext());retry.setText(R.string.refresh);retry.setOnClickListener(v->model.load(spinner.getSelectedItem().toString()));box.addView(retry);
  graph=new ChartView(requireContext());graph.setContentDescription(getString(R.string.chart_description));box.addView(graph,new LinearLayout.LayoutParams(-1,0,1));return box;
 }
 @Override public void onViewCreated(View v,Bundle b){
  model=new ViewModelProvider(this).get(HistoryModel.class);
  HistoryModel.State old=model.data.getValue();
  if(old!=null){String[] codes=getResources().getStringArray(R.array.currencies);for(int n=0;n<codes.length;n++)if(codes[n].equals(old.code))spinner.setSelection(n);}
  model.data.observe(getViewLifecycleOwner(),s->{
   status.setText(s.loading?getString(R.string.loading):s.error?getString(R.string.network_error):s.points.size()<2?getString(R.string.no_history):getString(R.string.graph_title,s.code)+"\n"+getString(R.string.graph_range,s.points.get(0).date,s.points.get(s.points.size()-1).date,s.points.size()));
   graph.setPoints(s.points);
  });
  spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
   public void onNothingSelected(AdapterView<?> p){}
   public void onItemSelected(AdapterView<?> p,View view,int pos,long id){String code=p.getItemAtPosition(pos).toString();HistoryModel.State s=model.data.getValue();if(s==null||!s.code.equals(code))model.load(code);}
  });
 }
 @Override public void onDestroyView(){super.onDestroyView();status=null;graph=null;spinner=null;}
}
