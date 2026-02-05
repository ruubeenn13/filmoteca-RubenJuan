package es.pmdm.filmoteca;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FilmEditActivity extends AppCompatActivity {

    private int position;
    private Film film;

    private ImageView ivFilmImage;
    private EditText etFilmTitle;
    private EditText etFilmDirector;
    private EditText etFilmYear;
    private EditText etFilmUrl;
    private EditText etFilmComments;
    private Spinner spFilmGenre;
    private Spinner spFilmFormat;
    private Button btnCaptureImage;
    private Button btnSelectImage;
    private Button btnCancel;
    private Button btnSave;
    private static final int CAMERA_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_edit);

        if (savedInstanceState != null && savedInstanceState.containsKey("position")) {
            position = savedInstanceState.getInt("position");
        } else {
            position = getIntent().getIntExtra("FILM_POSITION", 0);
        }

        film = FilmDataSource.films.get(position);

        ivFilmImage = findViewById(R.id.ivFilmImage);
        etFilmTitle = findViewById(R.id.etFilmTitle);
        etFilmDirector = findViewById(R.id.etFilmDirector);
        etFilmYear = findViewById(R.id.etFilmYear);
        etFilmUrl = findViewById(R.id.etFilmUrl);
        etFilmComments = findViewById(R.id.etFilmComments);
        spFilmGenre = findViewById(R.id.spFilmGenre);
        spFilmFormat = findViewById(R.id.spFilmFormat);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        setupSpinners();

        if (savedInstanceState == null) {
            loadFilmData();
        } else {
            restoreState(savedInstanceState);
        }

        setupListeners();
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(
                this, R.array.genres, android.R.layout.simple_spinner_item);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilmGenre.setAdapter(genreAdapter);

        ArrayAdapter<CharSequence> formatAdapter = ArrayAdapter.createFromResource(
                this, R.array.formats, android.R.layout.simple_spinner_item);
        formatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilmFormat.setAdapter(formatAdapter);
    }

    private void loadFilmData() {
        ivFilmImage.setImageResource(film.getImageResId());
        etFilmTitle.setText(film.getTitle());
        etFilmDirector.setText(film.getDirector());
        etFilmYear.setText(String.valueOf(film.getYear()));
        etFilmUrl.setText(film.getImbdURL());
        etFilmComments.setText(film.getComments());
        spFilmGenre.setSelection(film.getGenre());
        spFilmFormat.setSelection(film.getFormat());
    }

    private void setupListeners() {
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermissionAndOpen();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FilmEditActivity.this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FilmEditActivity.this, R.string.changes_cancelled, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void checkCameraPermissionAndOpen() {
        int estado = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (estado != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.permission_conceded_capture, Toast.LENGTH_SHORT).show();
            openCamera();
        } else {
            Toast.makeText(this, R.string.permission_denied_capture, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveChanges() {
        String title = etFilmTitle.getText().toString().trim();
        String director = etFilmDirector.getText().toString().trim();
        String yearStr = etFilmYear.getText().toString().trim();
        String url = etFilmUrl.getText().toString().trim();
        String comments = etFilmComments.getText().toString().trim();

        if (title.isEmpty() || director.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int year = Integer.parseInt(yearStr);
        int genre = spFilmGenre.getSelectedItemPosition();
        int format = spFilmFormat.getSelectedItemPosition();

        film.setTitle(title);
        film.setDirector(director);
        film.setYear(year);
        film.setImbdURL(url);
        film.setComments(comments);
        film.setGenre(genre);
        film.setFormat(format);

        Toast.makeText(this, R.string.changes_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
        outState.putString("title", etFilmTitle.getText().toString());
        outState.putString("director", etFilmDirector.getText().toString());
        outState.putString("year", etFilmYear.getText().toString());
        outState.putString("url", etFilmUrl.getText().toString());
        outState.putString("comments", etFilmComments.getText().toString());
        outState.putInt("genre", spFilmGenre.getSelectedItemPosition());
        outState.putInt("format", spFilmFormat.getSelectedItemPosition());
    }

    private void restoreState(Bundle savedInstanceState) {
        ivFilmImage.setImageResource(film.getImageResId());
        etFilmTitle.setText(savedInstanceState.getString("title"));
        etFilmDirector.setText(savedInstanceState.getString("director"));
        etFilmYear.setText(savedInstanceState.getString("year"));
        etFilmUrl.setText(savedInstanceState.getString("url"));
        etFilmComments.setText(savedInstanceState.getString("comments"));
        spFilmGenre.setSelection(savedInstanceState.getInt("genre"));
        spFilmFormat.setSelection(savedInstanceState.getInt("format"));
    }
}