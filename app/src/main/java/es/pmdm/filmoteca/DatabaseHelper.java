package es.pmdm.filmoteca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "filmoteca.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FILMS = "films";
    private static final String COLUMN_FILM_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DIRECTOR = "director";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_IMDB_URL = "imdb_url";
    private static final String COLUMN_FORMAT = "format";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_COMMENTS = "comments";
    private static final String COLUMN_IMAGE_RES_ID = "image_res_id";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createFilmsTable = "CREATE TABLE " + TABLE_FILMS + " (" +
                COLUMN_FILM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DIRECTOR + " TEXT, " +
                COLUMN_YEAR + " INTEGER, " +
                COLUMN_IMDB_URL + " TEXT, " +
                COLUMN_FORMAT + " INTEGER, " +
                COLUMN_GENRE + " INTEGER, " +
                COLUMN_COMMENTS + " TEXT, " +
                COLUMN_IMAGE_RES_ID + " INTEGER, " +
                COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0)";
        db.execSQL(createFilmsTable);

        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
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
        db.close();
        return id;
    }

    public Cursor getAllFilms() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_FILMS + " ORDER BY " + COLUMN_FILM_ID + " ASC", null);
    }

    public Cursor getFilmById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FILMS, null, COLUMN_FILM_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
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

        int rowsAffected = db.update(TABLE_FILMS, values, COLUMN_FILM_ID + " = ?",
                new String[]{String.valueOf(film.getId())});
        db.close();
        return rowsAffected;
    }

    public int deleteFilm(long filmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_FILMS, COLUMN_FILM_ID + " = ?",
                new String[]{String.valueOf(filmId)});
        db.close();
        return rowsDeleted;
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

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + " = ?",
                new String[]{username},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }
}