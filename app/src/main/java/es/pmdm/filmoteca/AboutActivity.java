package es.pmdm.filmoteca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        String uriText = "mailto:" + Uri.encode("filmoteca@pmdm.es") +
                "?subject=" + Uri.encode("Soporte Filmoteca") +
                "&body=" + Uri.encode("Texto del correo de soporte");

        Uri uri = Uri.parse(uriText);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

        try {
            startActivity(Intent.createChooser(intent, "Enviar email con:"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No hay aplicaciones de correo instaladas", Toast.LENGTH_LONG).show();
        }
    }
}