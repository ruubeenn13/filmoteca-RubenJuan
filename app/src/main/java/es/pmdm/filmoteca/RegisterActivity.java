package es.pmdm.filmoteca;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterUsername;
    private EditText etRegisterPassword;
    private Button btnSaveUser;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnSaveUser = findViewById(R.id.btnSaveUser);

        btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = etRegisterUsername.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            ToastHelper.showCustomToast(this, R.string.fill_all_login_fields);
            return;
        }

        if (dbHelper.userExists(username)) {
            ToastHelper.showCustomToast(this, R.string.user_exists);
            return;
        }

        boolean success = dbHelper.registerUser(username, password);

        if (success) {
            ToastHelper.showCustomToast(this, R.string.user_registered);
            finish();
        } else {
            ToastHelper.showCustomToast(this, R.string.user_exists);
        }
    }
}