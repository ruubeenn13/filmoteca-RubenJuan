package es.pmdm.filmoteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FilmListActivity extends AppCompatActivity {

    private ListView lvFilms;
    private FilmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        FilmDataSource.Initialize();

        lvFilms = findViewById(R.id.lvFilms);

        adapter = new FilmAdapter(getLayoutInflater(), FilmDataSource.films);
        lvFilms.setAdapter(adapter);

        lvFilms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FilmListActivity.this, FilmDataActivity.class);
                intent.putExtra("FILM_POSITION", position);
                startActivity(intent);
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

    private void openAboutActivity() {
        Intent intent = new Intent(FilmListActivity.this, AboutActivity.class);
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
        newFilm.setComments("Película añadida desde el menú. Edita sus datos.");
        newFilm.setImageResId(R.drawable.ic_launcher_foreground);

        FilmDataSource.films.add(newFilm);

        adapter.notifyDataSetChanged();

        Toast.makeText(this, R.string.film_added, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}