package cz.vspj.vizitka;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText jmeno = findViewById(R.id.jmeno);
        EditText vek = findViewById(R.id.vek);
        Spinner system = findViewById(R.id.system);
        CheckBox souhlas = findViewById(R.id.souhlas);
        Button odeslat = findViewById(R.id.odeslat);
        TextView vysledek = findViewById(R.id.vysledek);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.operacni_systemy, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        system.setAdapter(adapter);

        odeslat.setOnClickListener(view -> {
            vysledek.setText("");
            if (!souhlas.isChecked()) {
                Toast.makeText(this, R.string.chybi_souhlas, Toast.LENGTH_SHORT).show();
                return;
            }
            String zadaneJmeno = jmeno.getText().toString().trim();
            String zadanyVek = vek.getText().toString().trim();
            if (zadaneJmeno.isEmpty()) {
                jmeno.setError(getString(R.string.chybi_jmeno));
                return;
            }
            if (zadanyVek.isEmpty()) {
                vek.setError(getString(R.string.chybi_vek));
                return;
            }
            vysledek.setText(getString(R.string.vysledek_format, zadaneJmeno,
                    zadanyVek, system.getSelectedItem().toString()));
        });
    }
}
