package es.pmdm.filmoteca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FilmDataActivity extends AppCompatActivity {

    private int position;
    private Film film;

    private ImageView ivFilmImage;
    private TextView tvFilmTitle;
    private TextView tvFilmDirector;
    private TextView tvFilmYear;
    private TextView tvFilmFormatGenre;
    private TextView tvFilmComments;
    private Button btnViewImdb;
    private Button btnBackToMain;
    private Button btnEditFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_data);

        Intent intent = getIntent();
        position = intent.getIntExtra("FILM_POSITION", 0);

        film = FilmDataSource.films.get(position);

        ivFilmImage = findViewById(R.id.ivFilmImage);
        tvFilmTitle = findViewById(R.id.tvFilmTitle);
        tvFilmDirector = findViewById(R.id.tvFilmDirector);
        tvFilmYear = findViewById(R.id.tvFilmYear);
        tvFilmFormatGenre = findViewById(R.id.tvFilmFormatGenre);
        tvFilmComments = findViewById(R.id.tvFilmComments);
        btnViewImdb = findViewById(R.id.btnViewImdb);
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnEditFilm = findViewById(R.id.btnEditFilm);

        loadFilmData();
        setupListeners();
    }

    private void loadFilmData() {
        ivFilmImage.setImageResource(film.getImageResId());
        tvFilmTitle.setText(film.getTitle());
        tvFilmDirector.setText(film.getDirector());
        tvFilmYear.setText(String.valueOf(film.getYear()));

        String[] formats = getResources().getStringArray(R.array.formats);
        String[] genres = getResources().getStringArray(R.array.genres);
        String formatGenre = formats[film.getFormat()] + ", " + genres[film.getGenre()];
        tvFilmFormatGenre.setText(formatGenre);

        tvFilmComments.setText(film.getComments());
    }

    private void setupListeners() {
        btnViewImdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImdb();
            }
        });

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEditFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditFilm();
            }
        });
    }

    private void openImdb() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(film.getImbdURL()));
        startActivity(intent);
    }

    private void openEditFilm() {
        Intent intent = new Intent(FilmDataActivity.this, FilmEditActivity.class);
        intent.putExtra("FILM_POSITION", position);
        startActivity(intent);
    }
}