package es.pmdm.filmoteca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;

    private static final String PREF_LAST_USERNAME = "last_username";
    private static final String PREF_ENABLE_REGISTER = "enable_register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        loadLastUsername();

        updateRegisterButtonVisibility();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateRegisterButtonVisibility();
    }

    private void loadLastUsername() {
        String lastUsername = prefs.getString(PREF_LAST_USERNAME, "");
        etUsername.setText(lastUsername);
    }

    private void saveLastUsername(String username) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREF_LAST_USERNAME, username);

        editor.apply();
    }

    private void updateRegisterButtonVisibility() {
        boolean enableRegister = prefs.getBoolean(PREF_ENABLE_REGISTER, true);

        btnRegister.setVisibility(enableRegister ? View.VISIBLE : View.GONE);
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            ToastHelper.showCustomToast(this, R.string.fill_all_login_fields);
            return;
        }

        if (dbHelper.validateUser(username, password)) {
            saveLastUsername(username);

            Intent intent = new Intent(LoginActivity.this, FilmListActivity.class);
            startActivity(intent);
            finish();
        } else {
            ToastHelper.showCustomToast(this, R.string.invalid_credentials);
        }
    }
}