package es.pmdm.filmoteca;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox cbEnableRegister;
    private SharedPreferences prefs;
    private static final String PREF_ENABLE_REGISTER = "enable_register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        cbEnableRegister = findViewById(R.id.cbEnableRegister);

        boolean isEnabled = prefs.getBoolean(PREF_ENABLE_REGISTER, true);
        cbEnableRegister.setChecked(isEnabled);

        cbEnableRegister.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(PREF_ENABLE_REGISTER, isChecked);
                editor.apply();

                ToastHelper.showCustomToast(SettingsActivity.this,
                        isChecked ? R.string.enable_register : R.string.disable_register);
            }
        });
    }
}