package cz.vspj.bmi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/** Druhá aktivita načítá všechny popisky tabulky ze seznamů v resources. */
public class CategoriesActivity extends Activity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_categories);
        ScreenInsets.apply(findViewById(R.id.screen_root));
        findViewById(R.id.btn_close).setOnClickListener(v -> finish());
        String[] names = getResources().getStringArray(R.array.bmi_category_names);
        String[] ranges = getResources().getStringArray(R.array.bmi_category_ranges);
        TableLayout table = findViewById(R.id.table_categories);
        for (int i = 0; i < names.length; i++) {
            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.item_category, table, false);
            ((TextView) row.findViewById(R.id.category_name)).setText(names[i]);
            ((TextView) row.findViewById(R.id.category_range)).setText(ranges[i]);
            table.addView(row);
        }
    }
}
