package es.pmdm.filmoteca;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

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
    private static final int SMS_PERMISSION_CODE = 102;
    private Film filmToShare = null;
    private static final String MY_PHONE_NUMBER = "+34722794304";

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
        NotificationHelper.createNotificationChannel(this);
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
        final Film selectedFilm = (Film) adapter.getItem(info.position);

        if (item.getItemId() == R.id.menu_delete) {
            showDeleteDialog(selectedFilm);
            return true;
        } else if (item.getItemId() == R.id.menu_share) {
            filmToShare = selectedFilm;
            checkSmsPermissionAndShare();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void showDeleteDialog(final Film filmToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_title_dialog);
        builder.setMessage(getString(R.string.delete_confirmation_dialog, filmToDelete.getTitle()));
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filmTitle = filmToDelete.getTitle();
                deleteFilm(filmToDelete);
                ToastHelper.showCustomToast(FilmListActivity.this, R.string.film_deleted);
                NotificationHelper.showFilmDeletedNotification(FilmListActivity.this, filmTitle);
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastHelper.showCustomToast(FilmListActivity.this, R.string.delete_canceled_dialog);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void checkSmsPermissionAndShare() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_CODE);
        } else {
            showShareDialog();
        }
    }

    private void showShareDialog() {
        if (filmToShare == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.share_dialog_title);
        builder.setIcon(android.R.drawable.ic_menu_share);

        String[] options = {
                getString(R.string.share_via_sms),
                getString(R.string.share_via_whatsapp)
        };

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = getString(R.string.share_film_message, filmToShare.getTitle());

                if (which == 0) {
                    enviarViaAppMensajes(MY_PHONE_NUMBER, message);
                } else if (which == 1) {
                    enviarViaWhatsApp(MY_PHONE_NUMBER, message);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void enviarViaWhatsApp(String phoneNumber, String message) {
        try {
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);
            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(whatsappIntent);
        } catch (ActivityNotFoundException e) {
            ToastHelper.showCustomToast(this, R.string.whatsapp_not_installed);
        }
    }

    private void enviarViaAppMensajes(String phoneNumber, String message) {
        Uri smsUri = Uri.parse("smsto:" + phoneNumber);
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
        smsIntent.putExtra("sms_body", message);
        try {
            startActivity(smsIntent);
        } catch (ActivityNotFoundException e) {
            ToastHelper.showCustomToast(this, R.string.no_messaging_app);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ToastHelper.showCustomToast(this, R.string.permission_notification_granted);
            } else {
                ToastHelper.showCustomToast(this, R.string.permission_notification_denied);
            }
        } else if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ToastHelper.showCustomToast(this, R.string.permission_sms_granted);
                showShareDialog();
            } else {
                ToastHelper.showCustomToast(this, R.string.permission_sms_denied);
            }
        }
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
                ToastHelper.showCustomToast(this, R.string.no_favorites);
                showingFavoritesOnly = false;
            } else {
                adapter = new FilmAdapter(getLayoutInflater(), favoriteFilms);
                lvFilms.setAdapter(adapter);
                ToastHelper.showCustomToast(this, R.string.showing_favorites);
            }
        } else {
            adapter = new FilmAdapter(getLayoutInflater(), allFilms);
            lvFilms.setAdapter(adapter);
            ToastHelper.showCustomToast(this, R.string.showing_all);
        }

        invalidateOptionsMenu();
    }

    private void deleteFilm(Film film) {
        FilmDataSource.films.remove(film);

        if (showingFavoritesOnly) {
            favoriteFilms.remove(film);
        }

        adapter.notifyDataSetChanged();
    }

    private void openAboutActivity() {
        Intent intent = new Intent(FilmListActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    private void addNewFilm() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_film, null);

        final EditText etDialogTitle = dialogView.findViewById(R.id.etDialogTitle);
        final EditText etDialogDirector = dialogView.findViewById(R.id.etDialogDirector);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_film_dialog_title);
        builder.setIcon(android.R.drawable.ic_input_add);
        builder.setView(dialogView);

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = etDialogTitle.getText().toString().trim();
                String director = etDialogDirector.getText().toString().trim();

                if (title.isEmpty() || director.isEmpty()) {
                    ToastHelper.showCustomToast(FilmListActivity.this, R.string.fill_all_fields);
                    return;
                }

                Film newFilm = new Film();
                newFilm.setTitle(title);
                newFilm.setDirector(director);
                newFilm.setYear(2025);
                newFilm.setImbdURL("http://www.imdb.com");
                newFilm.setFormat(Film.FORMAT_DIGITAL);
                newFilm.setGenre(Film.GENRE_SCIFI);
                newFilm.setComments(getString(R.string.comments_text));
                newFilm.setImageResId(R.drawable.ic_launcher_foreground);

                FilmDataSource.films.add(newFilm);

                int newFilmPosition = FilmDataSource.films.size() - 1;

                adapter.notifyDataSetChanged();

                ToastHelper.showCustomToast(FilmListActivity.this, R.string.film_added);

                NotificationHelper.showFilmAddedNotification(FilmListActivity.this, newFilm, newFilmPosition);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastHelper.showCustomToast(FilmListActivity.this, R.string.operation_canceled);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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