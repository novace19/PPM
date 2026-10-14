package cz.vspj.bmi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Locale;

/** Formulář s ošetřením prázdných vstupů a NumberFormatException. */
public class MainActivity extends Activity {
    private static final String STATE_BMI = "calculated_bmi";
    private EditText heightInput;
    private EditText weightInput;
    private TextView resultView;
    private double lastBmi = Double.NaN;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        ScreenInsets.apply(findViewById(R.id.screen_root));
        heightInput = findViewById(R.id.input_height);
        weightInput = findViewById(R.id.input_weight);
        resultView = findViewById(R.id.text_result);
        findViewById(R.id.btn_calculate).setOnClickListener(v -> calculate());
        findViewById(R.id.btn_info).setOnClickListener(v ->
                startActivity(new Intent(this, CategoriesActivity.class)));
        weightInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) { calculate(); return true; }
            return false;
        });
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastBmi = Double.NaN;
                resultView.setText(R.string.result_placeholder);
                heightInput.setError(null);
                weightInput.setError(null);
            }
            @Override public void afterTextChanged(Editable text) { }
        };
        heightInput.addTextChangedListener(watcher);
        weightInput.addTextChangedListener(watcher);
    }

    private void calculate() {
        String heightText = heightInput.getText().toString().trim();
        String weightText = weightInput.getText().toString().trim();
        heightInput.setError(null);
        weightInput.setError(null);
        lastBmi = Double.NaN;
        resultView.setText(R.string.result_placeholder);
        boolean emptyHeight = heightText.isEmpty();
        boolean emptyWeight = weightText.isEmpty();
        if (emptyHeight || emptyWeight) {
            if (emptyHeight) heightInput.setError(getString(R.string.error_empty));
            if (emptyWeight) weightInput.setError(getString(R.string.error_empty));
            (emptyHeight ? heightInput : weightInput).requestFocus();
            return;
        }
        double height;
        double weight;
        try {
            height = BmiCalculator.parseHeight(heightText);
        } catch (NumberFormatException e) {
            heightInput.setError(getString(R.string.error_height));
            heightInput.requestFocus();
            return;
        }
        try {
            weight = BmiCalculator.parseWeight(weightText);
        } catch (NumberFormatException e) {
            weightInput.setError(getString(R.string.error_invalid));
            weightInput.requestFocus();
            return;
        }
        try {
            lastBmi = BmiCalculator.calculate(height, weight);
            showResult();
        } catch (NumberFormatException e) {
            weightInput.setError(getString(R.string.error_invalid));
            weightInput.requestFocus();
        }
    }

    private void showResult() {
        NumberFormat number = NumberFormat.getNumberInstance(Locale.getDefault());
        number.setMinimumFractionDigits(2);
        number.setMaximumFractionDigits(2);
        number.setGroupingUsed(false);
        String[] categories = getResources().getStringArray(R.array.bmi_category_names);
        resultView.setText(getString(R.string.bmi_result, number.format(lastBmi),
                categories[BmiCalculator.categoryIndex(lastBmi)]));
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(STATE_BMI, lastBmi);
    }

    @Override protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        lastBmi = state.getDouble(STATE_BMI, Double.NaN);
        if (!Double.isNaN(lastBmi) && !Double.isInfinite(lastBmi) && lastBmi > 0) showResult();
    }
}
