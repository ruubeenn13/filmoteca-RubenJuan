package es.pmdm.filmoteca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FilmDataActivity extends AppCompatActivity {

    private ImageView ivFilmImage;
    private ImageView ivFavorite;
    private TextView tvFilmTitle;
    private TextView tvFilmDirector;
    private TextView tvFilmYear;
    private TextView tvFilmFormatGenre;
    private TextView tvFilmComments;
    private Button btnViewImdb;
    private Button btnBackToMain;
    private Button btnEditFilm;

    private Film currentFilm;
    private int filmPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_data);

        ivFilmImage = findViewById(R.id.ivFilmImage);
        ivFavorite = findViewById(R.id.ivFavorite);
        tvFilmTitle = findViewById(R.id.tvFilmTitle);
        tvFilmDirector = findViewById(R.id.tvFilmDirector);
        tvFilmYear = findViewById(R.id.tvFilmYear);
        tvFilmFormatGenre = findViewById(R.id.tvFilmFormatGenre);
        tvFilmComments = findViewById(R.id.tvFilmComments);
        btnViewImdb = findViewById(R.id.btnViewImdb);
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnEditFilm = findViewById(R.id.btnEditFilm);

        filmPosition = getIntent().getIntExtra("FILM_POSITION", -1);

        if (filmPosition != -1 && filmPosition < FilmDataSource.films.size()) {
            currentFilm = FilmDataSource.films.get(filmPosition);
            displayFilmData();
        }

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_about) {
            openAboutActivity();
            return true;
        } else if (id == R.id.menu_add_film) {
            addNewFilm();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleFavorite() {
        currentFilm.setFavorite(!currentFilm.isFavorite());
        updateFavoriteIcon();

        FilmDataSource.updateFilm(currentFilm);

        String message = currentFilm.isFavorite() ?
                getString(R.string.added_to_favorites) :
                getString(R.string.removed_from_favorites);
        ToastHelper.showCustomToast(this, message);
    }

    private void updateFavoriteIcon() {
        if (currentFilm.isFavorite()) {
            ivFavorite.setImageResource(android.R.drawable.star_big_on);
        } else {
            ivFavorite.setImageResource(android.R.drawable.star_big_off);
        }
    }

    private void openAboutActivity() {
        Intent intent = new Intent(FilmDataActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    private void addNewFilm() {
        Film newFilm = new Film();
        newFilm.setTitle("Nueva Película");
        newFilm.setDirector("Director Desconocido");
        newFilm.setYear(2025);
        newFilm.setImbdURL("http://www.imdb.com");
        newFilm.setFormat(Film.FORMAT_DIGITAL);
        newFilm.setGenre(Film.GENRE_SCIFI);
        newFilm.setComments(getString(R.string.comments_text));
        newFilm.setImageResId(R.drawable.ic_launcher_foreground);

        FilmDataSource.addFilm(newFilm);

        int newFilmPosition = FilmDataSource.films.size() - 1;

        ToastHelper.showCustomToast(this, R.string.film_added);

        NotificationHelper.showFilmAddedNotification(this, newFilm, newFilmPosition);
    }

    private void displayFilmData() {
        ivFilmImage.setImageResource(currentFilm.getImageResId());
        tvFilmTitle.setText(currentFilm.getTitle());
        tvFilmDirector.setText(currentFilm.getDirector());
        tvFilmYear.setText(String.valueOf(currentFilm.getYear()));

        String[] formats = getResources().getStringArray(R.array.formats);
        String[] genres = getResources().getStringArray(R.array.genres);

        String formatGenre = formats[currentFilm.getFormat()] + ", " + genres[currentFilm.getGenre()];
        tvFilmFormatGenre.setText(formatGenre);

        tvFilmComments.setText(currentFilm.getComments());

        updateFavoriteIcon();
    }

    private void openImdb() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(currentFilm.getImbdURL()));
        startActivity(intent);
    }

    private void openEditFilm() {
        Intent intent = new Intent(FilmDataActivity.this, FilmEditActivity.class);
        intent.putExtra("FILM_POSITION", filmPosition);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (filmPosition != -1 && filmPosition < FilmDataSource.films.size()) {
            currentFilm = FilmDataSource.films.get(filmPosition);
            displayFilmData();
        }
    }
}