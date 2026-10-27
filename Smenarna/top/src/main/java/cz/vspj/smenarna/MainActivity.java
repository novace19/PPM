package cz.vspj.smenarna;
import android.os.Bundle;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import java.util.Locale;
public class MainActivity extends FragmentActivity {
 private EditText amount; private TextView result;
 private String[] codes;
 private RatesModel model;
 @Override public void onCreate(Bundle state) {
  super.onCreate(state); setContentView(R.layout.activity_main);
  amount=findViewById(R.id.amount); result=findViewById(R.id.result);
  codes=getResources().getStringArray(R.array.currencies);
  if(state!=null) result.setText(state.getString("result",getString(R.string.placeholder)));
  findViewById(R.id.convert).setOnClickListener(v->convert());
  model=new androidx.lifecycle.ViewModelProvider(this).get(RatesModel.class);
  model.data.observe(this,s->{
   ((TextView)findViewById(R.id.status)).setText(s.error?getString(R.string.network_error):s.rates==null?getString(R.string.loading):getString(R.string.rate_date,s.rates.date));
   findViewById(R.id.convert).setEnabled(s.rates!=null);
   findViewById(R.id.refresh).setEnabled(!s.loading);
  });
  findViewById(R.id.refresh).setOnClickListener(v->model.load());
  findViewById(R.id.rates).setOnClickListener(v->startActivity(new android.content.Intent(this,RatesActivity.class)));
  findViewById(R.id.close).setOnClickListener(v->finishAffinity());
  if(model.data.getValue()==null)model.load();
 }
 private void convert() {
  if(amount.getText().toString().trim().isEmpty()){amount.setError(getString(R.string.error_empty));return;}
  try {
   double value=Money.parse(amount.getText().toString());
   int index=((Spinner)findViewById(R.id.currency)).getSelectedItemPosition();
   boolean forward=((RadioGroup)findViewById(R.id.direction)).getCheckedRadioButtonId()==R.id.toForeign;
   double rate=liveRate(index);
   double output=Money.convert(value,rate,forward);
   amount.setError(null);
   ((android.view.inputmethod.InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(amount.getWindowToken(),0);
   result.setText(getString(R.string.result_currency,String.format(Locale.forLanguageTag("cs-CZ"),"%.2f",output),forward?codes[index]:getString(R.string.czk)));
  } catch(NumberFormatException e){amount.setError(getString(R.string.error_invalid));}
 }
 @Override public void onSaveInstanceState(Bundle out){super.onSaveInstanceState(out);out.putString("result",result.getText().toString());}
 private double liveRate(int index){
  RatesModel.State s=model.data.getValue();
  if(s==null||s.rates==null||!s.rates.values.containsKey(codes[index]))throw new NumberFormatException();
  return s.rates.values.get(codes[index]);
 }
}
