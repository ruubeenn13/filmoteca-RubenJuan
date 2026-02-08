package es.pmdm.filmoteca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "filmoteca.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FILMS = "films";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DIRECTOR = "director";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_IMDB_URL = "imdb_url";
    private static final String COLUMN_FORMAT = "format";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_COMMENTS = "comments";
    private static final String COLUMN_IMAGE_RES_ID = "image_res_id";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_FILMS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DIRECTOR + " TEXT, " +
                COLUMN_YEAR + " INTEGER, " +
                COLUMN_IMDB_URL + " TEXT, " +
                COLUMN_FORMAT + " INTEGER, " +
                COLUMN_GENRE + " INTEGER, " +
                COLUMN_COMMENTS + " TEXT, " +
                COLUMN_IMAGE_RES_ID + " INTEGER, " +
                COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMS);
        onCreate(db);
    }

    public long insertFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, film.getTitle());
        values.put(COLUMN_DIRECTOR, film.getDirector());
        values.put(COLUMN_YEAR, film.getYear());
        values.put(COLUMN_IMDB_URL, film.getImbdURL());
        values.put(COLUMN_FORMAT, film.getFormat());
        values.put(COLUMN_GENRE, film.getGenre());
        values.put(COLUMN_COMMENTS, film.getComments());
        values.put(COLUMN_IMAGE_RES_ID, film.getImageResId());
        values.put(COLUMN_IS_FAVORITE, film.isFavorite() ? 1 : 0);

        long id = db.insert(TABLE_FILMS, null, values);
        film.setId(id);
        db.close();
        return id;
    }

    public int updateFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, film.getTitle());
        values.put(COLUMN_DIRECTOR, film.getDirector());
        values.put(COLUMN_YEAR, film.getYear());
        values.put(COLUMN_IMDB_URL, film.getImbdURL());
        values.put(COLUMN_FORMAT, film.getFormat());
        values.put(COLUMN_GENRE, film.getGenre());
        values.put(COLUMN_COMMENTS, film.getComments());
        values.put(COLUMN_IMAGE_RES_ID, film.getImageResId());
        values.put(COLUMN_IS_FAVORITE, film.isFavorite() ? 1 : 0);

        int rowsAffected = db.update(TABLE_FILMS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(film.getId())});
        db.close();
        return rowsAffected;
    }

    public void deleteFilm(long filmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILMS, COLUMN_ID + " = ?", new String[]{String.valueOf(filmId)});
        db.close();
    }

    public ArrayList<Film> getAllFilms() {
        ArrayList<Film> filmList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FILMS, null, null, null, null, null, COLUMN_ID + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Film film = new Film();
                film.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                film.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                film.setDirector(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTOR)));
                film.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR)));
                film.setImbdURL(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMDB_URL)));
                film.setFormat(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FORMAT)));
                film.setGenre(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GENRE)));
                film.setComments(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENTS)));
                film.setImageResId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_RES_ID)));
                film.setFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1);

                filmList.add(film);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return filmList;
    }

    public Film getFilmById(long filmId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Film film = null;

        Cursor cursor = db.query(TABLE_FILMS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(filmId)}, null, null, null);

        if (cursor.moveToFirst()) {
            film = new Film();
            film.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            film.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
            film.setDirector(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTOR)));
            film.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR)));
            film.setImbdURL(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMDB_URL)));
            film.setFormat(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FORMAT)));
            film.setGenre(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GENRE)));
            film.setComments(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENTS)));
            film.setImageResId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_RES_ID)));
            film.setFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1);
        }

        cursor.close();
        db.close();
        return film;
    }

    public boolean isDatabaseEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_FILMS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count == 0;
    }
}
