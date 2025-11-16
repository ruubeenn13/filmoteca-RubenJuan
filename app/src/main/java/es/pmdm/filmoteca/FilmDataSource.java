package es.pmdm.filmoteca;

import java.util.ArrayList;

public class FilmDataSource {

    public static ArrayList<Film> films;

    public static void Initialize() {
        films = new ArrayList<Film>();

        // Interstellar
        Film f = new Film();
        f.setTitle("Interstellar");
        f.setDirector("Christopher Nolan");
        f.setImageResId(R.drawable.interstellar);
        f.setComments("A team of explorers travel through a wormhole in spacee in an attempt to ensure humanity's survival.");
        f.setFormat(Film.FORMAT_DIGITAL);
        f.setGenre(Film.GENRE_SCIFI);
        f.setImbdURL("http://www.imdb.com/title/tt0816692");
        f.setYear(2014);
        films.add(f);

        // Back to the Future
        f = new Film();
        f.setTitle("Back to the Future");
        f.setDirector("Robert Zemeckis");
        f.setImageResId(R.drawable.back_to_the_future);
        f.setComments("Marty McFly is sent 30 years into the past in a time-travelling DeLorean.");
        f.setFormat(Film.FORMAT_DIGITAL);
        f.setGenre(Film.GENRE_SCIFI);
        f.setImbdURL("http://www.imdb.com/title/tt0088763");
        f.setYear(1985);
        films.add(f);

        // The Shawshank Redemption
        f = new Film();
        f.setTitle("The Shawshank Redemption");
        f.setDirector("Frank Darabont");
        f.setImageResId(R.drawable.the_shawshank_redemption);
        f.setComments("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.");
        f.setFormat(Film.FORMAT_BLURAY);
        f.setGenre(Film.GENRE_DRAMA);
        f.setImbdURL("http://www.imdb.com/title/tt0111161");
        f.setYear(1994);
        films.add(f);

        // The Dark Knight
        f = new Film();
        f.setTitle("The Dark Knight");
        f.setDirector("Christopher Nolan");
        f.setImageResId(R.drawable.the_dark_knight);
        f.setComments("When the menace known as the Joker wreaks havoc and chaos on the people of Gotam, Batman must accepts one of the greatest psychological and physical tests.");
        f.setFormat(Film.FORMAT_BLURAY);
        f.setGenre(Film.GENRE_ACTION);
        f.setImbdURL("http://www.imdb.com/title/tt0468569");
        f.setYear(2008);
        films.add(f);

        // Forrest Gump
        f = new Film();
        f.setTitle("Forrest Gump");
        f.setDirector("Robert Zemeckis");
        f.setImageResId(R.drawable.forrest_gump);
        f.setComments("The presidencies of Keennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man");
        f.setFormat(Film.FORMAT_DVD);
        f.setGenre(Film.GENRE_DRAMA);
        f.setImbdURL("http://www.imdb.com/title/tt0109830");
        f.setYear(1994);
        films.add(f);

        // Inception
        f = new Film();
        f.setTitle("Inception");
        f.setDirector("Christopher Nolan");
        f.setImageResId(R.drawable.inception);
        f.setComments("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea");
        f.setFormat(Film.FORMAT_DIGITAL);
        f.setGenre(Film.GENRE_SCIFI);
        f.setImbdURL("http://www.imdb.com/title/tt1375666");
        f.setYear(2010);
        films.add(f);

        // The Matrix
        f = new Film();
        f.setTitle("The Matrix");
        f.setDirector("Lana Wachowski, Lily Wachowski");
        f.setImageResId(R.drawable.the_matrix);
        f.setComments("A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.");
        f.setFormat(Film.FORMAT_DVD);
        f.setGenre(Film.GENRE_SCIFI);
        f.setImbdURL("http://www.imdb.com/title/tt0133093");
        f.setYear(1999);
        films.add(f);

        // Gladiator
        f = new Film();
        f.setTitle("Gladiator");
        f.setDirector("Ridley Scott");
        f.setImageResId(R.drawable.gladiator);
        f.setComments("A former Roman General sets out to exact vengance against the corrupt emperor who murdered his family and sent him into slavery.");
        f.setFormat(Film.FORMAT_BLURAY);
        f.setGenre(Film.GENRE_ACTION);
        f.setImbdURL("http://www.imdb.com/title/tt0172495");
        f.setYear(2000);
        films.add(f);

        // The Silence of the Lambs
        f = new Film();
        f.setTitle("The Silence of the Lambs");
        f.setDirector("Jonathan Demme");
        f.setImageResId(R.drawable.the_silence_of_the_lambs);
        f.setComments("A youg F.B.I. cadet must recieve the help of an incarcerated and manipulative canibal killer to help catch another serial killer.");
        f.setFormat(Film.FORMAT_DVD);
        f.setGenre(Film.GENRE_HORROR);
        f.setImbdURL("http://www.imdb.com/title/tt0102926");
        f.setYear(1991);
        films.add(f);

        // Pulp Fiction
        f = new Film();
        f.setTitle("Pulp Fiction");
        f.setDirector("Quentin Tarantino");
        f.setImageResId(R.drawable.pulp_fiction);
        f.setComments("The lives of two mob hitmen, a boxer, a gangster, and his wife interwine in four tales of violence and redemption.");
        f.setFormat(Film.FORMAT_BLURAY);
        f.setGenre(Film.GENRE_DRAMA);
        f.setImbdURL("http://www.imdb.com/title/tt0110912");
        f.setYear(1994);
        films.add(f);

        // The Hangover
        f = new Film();
        f.setTitle("The Hangover");
        f.setDirector("Todd Philips");
        f.setImageResId(R.drawable.the_hangover);
        f.setComments("Three buddies wake up from a bachelor party in Las Vegas, with no memory of the previous night and the bachelor missing.");
        f.setFormat(Film.FORMAT_DVD);
        f.setGenre(Film.GENRE_COMEDY);
        f.setImbdURL("http://www.imdb.com/title/tt1119646");
        f.setYear(2009);
        films.add(f);

        // The Avengers
        f = new Film();
        f.setTitle("The Avengers");
        f.setDirector("Joss Whedon");
        f.setImageResId(R.drawable.the_avengers);
        f.setComments("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army.");
        f.setFormat(Film.FORMAT_DIGITAL);
        f.setGenre(Film.GENRE_ACTION);
        f.setImbdURL("http://www.imdb.com/title/tt0848228");
        f.setYear(2012);
        films.add(f);

        // Superbad
        f = new Film();
        f.setTitle("Superbad");
        f.setDirector("Greg Mottola");
        f.setImageResId(R.drawable.superbad);
        f.setComments("Two co-dependent high school seniors are forced to deal with separation anxiety after their plan to stage a booze-soaked party goes awry.");
        f.setFormat(Film.FORMAT_DVD);
        f.setGenre(Film.GENRE_COMEDY);
        f.setImbdURL("http://www.imdb.com/title/tt0829482");
        f.setYear(2007);
        films.add(f);

        // The Shining
        f = new Film();
        f.setTitle("The Shining");
        f.setDirector("Stanley Kubrick");
        f.setImageResId(R.drawable.the_shining);
        f.setComments("A family leads to an isolated hotel for the winter where a sinister presence influences the father into violence.");
        f.setFormat(Film.FORMAT_BLURAY);
        f.setGenre(Film.GENRE_HORROR);
        f.setImbdURL("http://www.imdb.com/title/tt0081505");
        f.setYear(1980);
        films.add(f);

        // Blade Runner
        f = new Film();
        f.setTitle("Blade Runner");
        f.setDirector("Ridley Scott");
        f.setImageResId(R.drawable.blade_runner);
        f.setComments("A blade runner must pursue and terminate four replicants who stole a ship in space and have returned to Earth to find their creator.");
        f.setFormat(Film.FORMAT_DIGITAL);
        f.setGenre(Film.GENRE_SCIFI);
        f.setImbdURL("http://www.imdb.com/title/tt0083658");
        f.setYear(1982);
        films.add(f);

    }
}
