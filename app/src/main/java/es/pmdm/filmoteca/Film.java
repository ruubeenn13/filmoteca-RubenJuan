package es.pmdm.filmoteca;

public class Film {
    public static final int FORMAT_DVD = 0;
    public static final int FORMAT_BLURAY = 1;
    public static final int FORMAT_DIGITAL = 2;

    public static final int GENRE_ACTION = 0;
    public static final int GENRE_COMEDY = 1;
    public static final int GENRE_DRAMA = 2;
    public static final int GENRE_SCIFI = 3;
    public static final int GENRE_HORROR = 4;

    private long id;
    private String title;
    private String director;
    private int year;
    private String imbdURL;
    private int format;
    private int genre;
    private String comments;
    private int imageResId;
    private boolean isFavorite;

    public Film() {
        this.id = -1;
        this.isFavorite = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImbdURL() {
        return imbdURL;
    }

    public void setImbdURL(String imbdURL) {
        this.imbdURL = imbdURL;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}