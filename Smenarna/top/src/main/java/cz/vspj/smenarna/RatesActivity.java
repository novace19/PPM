package cz.vspj.smenarna;
import android.os.Bundle;
import androidx.fragment.app.*;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.*;
public class RatesActivity extends FragmentActivity {
 @Override public void onCreate(Bundle state){
  super.onCreate(state);setContentView(R.layout.activity_rates);
  findViewById(R.id.back).setOnClickListener(v->finish());
  findViewById(R.id.close).setOnClickListener(v->finishAffinity());
  ViewPager2 pages=findViewById(R.id.pages);
  pages.setAdapter(new FragmentStateAdapter(this){
   @Override public int getItemCount(){return 2;}
   @Override public Fragment createFragment(int pos){return pos==0?new RatesFragment():new ChartFragment();}
  });
  new TabLayoutMediator(findViewById(R.id.tabs),pages,(tab,pos)->tab.setText(pos==0?R.string.tab_rates:R.string.tab_chart)).attach();
 }
}
