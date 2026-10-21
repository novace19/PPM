package cz.vspj.smenarna;
import android.os.Bundle;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import java.util.Locale;
public class MainActivity extends FragmentActivity {
 private EditText amount; private TextView result;
 private final String[] codes={"EUR","USD","GBP","PLN"};
 private final double[] fixed={25.0,23.0,29.5,5.8};
 
 @Override public void onCreate(Bundle state) {
  super.onCreate(state); setContentView(R.layout.activity_main);
  amount=findViewById(R.id.amount); result=findViewById(R.id.result);
  if(state!=null) result.setText(state.getString("result",getString(R.string.placeholder)));
  findViewById(R.id.convert).setOnClickListener(v->convert());
  
 }
 private void convert() {
  if(amount.getText().toString().trim().isEmpty()){amount.setError(getString(R.string.error_empty));return;}
  try {
   double value=Money.parse(amount.getText().toString());
   int index=0;
   boolean forward=((RadioGroup)findViewById(R.id.direction)).getCheckedRadioButtonId()==R.id.toForeign;
   double rate=fixed[index];
   double output=Money.convert(value,rate,forward);
   amount.setError(null);
   ((android.view.inputmethod.InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(amount.getWindowToken(),0);
   result.setText(getString(R.string.result_currency,String.format(Locale.forLanguageTag("cs-CZ"),"%.2f",output),forward?codes[index]:getString(R.string.czk)));
  } catch(NumberFormatException e){amount.setError(getString(R.string.error_invalid));}
 }
 @Override public void onSaveInstanceState(Bundle out){super.onSaveInstanceState(out);out.putString("result",result.getText().toString());}
 
}
