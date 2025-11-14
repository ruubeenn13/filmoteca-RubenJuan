package es.pmdm.filmoteca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {

    private Button btnGoToWebsite;
    private Button btnGetSupport;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.about_activity);

        btnGoToWebsite = findViewById(R.id.btnGoToWebsite);
        btnGetSupport = findViewById(R.id.btnGetSupport);
        btnBack = findViewById(R.id.btnBack);

        setupListeners();
    }

    private void setupListeners() {
        btnGoToWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite();
            }
        });

        btnGetSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSupportEmail();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://github.com"));
        startActivity(intent);
    }

    private void sendSupportEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.support_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, getString(R.string.email_chooser)));
        }
    }
}