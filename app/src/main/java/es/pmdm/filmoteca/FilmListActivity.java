package es.pmdm.filmoteca;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class FilmListActivity extends AppCompatActivity {

    private ListView lvFilms;
    private FilmAdapter adapter;
    private boolean showingFavoritesOnly = false;
    private ArrayList<Film> allFilms;
    private ArrayList<Film> favoriteFilms;
    private static final int NOTIFICATION_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        FilmDataSource.Initialize();

        lvFilms = findViewById(R.id.lvFilms);

        allFilms = FilmDataSource.films;
        favoriteFilms = new ArrayList<>();

        adapter = new FilmAdapter(getLayoutInflater(), allFilms);
        lvFilms.setAdapter(adapter);

        lvFilms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Film selectedFilm = (Film) adapter.getItem(position);
                int realPosition = FilmDataSource.films.indexOf(selectedFilm);

                Intent intent = new Intent(FilmListActivity.this, FilmDataActivity.class);
                intent.putExtra("FILM_POSITION", realPosition);
                startActivity(intent);
            }
        });

        registerForContextMenu(lvFilms);

        requestNotificationPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, R.id.menu_filter_favorites, 0, R.string.show_favorites);
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
        } else if (id == R.id.menu_filter_favorites) {
            toggleFavoritesFilter();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.lvFilms) {
            getMenuInflater().inflate(R.menu.menu_context_film, menu);
            menu.setHeaderTitle(R.string.delete_confirmation);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.menu_delete) {
            Film filmToDelete = (Film) adapter.getItem(info.position);
            deleteFilm(filmToDelete);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void toggleFavoritesFilter() {
        showingFavoritesOnly = !showingFavoritesOnly;

        if (showingFavoritesOnly) {
            favoriteFilms.clear();
            for (Film film : allFilms) {
                if (film.isFavorite()) {
                    favoriteFilms.add(film);
                }
            }

            if (favoriteFilms.isEmpty()) {
                Toast.makeText(this, R.string.no_favorites, Toast.LENGTH_SHORT).show();
                showingFavoritesOnly = false;
            } else {
                adapter = new FilmAdapter(getLayoutInflater(), favoriteFilms);
                lvFilms.setAdapter(adapter);
                Toast.makeText(this, R.string.showing_favorites, Toast.LENGTH_SHORT).show();
            }
        } else {
            adapter = new FilmAdapter(getLayoutInflater(), allFilms);
            lvFilms.setAdapter(adapter);
            Toast.makeText(this, R.string.showing_all, Toast.LENGTH_SHORT).show();
        }

        invalidateOptionsMenu();
    }

    private void deleteFilm(Film film) {
        FilmDataSource.films.remove(film);

        if (showingFavoritesOnly) {
            favoriteFilms.remove(film);
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.film_deleted, Toast.LENGTH_SHORT).show();
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

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            if (showingFavoritesOnly) {
                toggleFavoritesFilter();
                toggleFavoritesFilter();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }
}