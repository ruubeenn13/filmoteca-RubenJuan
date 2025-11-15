package es.pmdm.filmoteca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FilmAdapter extends BaseAdapter {

    private ArrayList<Film> films;
    private LayoutInflater inflater;

    public FilmAdapter(LayoutInflater inflater, ArrayList<Film> films) {
        this.inflater = inflater;
        this.films = films;
    }

    @Override
    public int getCount() {
        return films.size();
    }

    @Override
    public Film getItem(int position) {
        return films.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_film, parent, false);
        }

        ImageView ivFilmImage = convertView.findViewById(R.id.ivFilmImage);
        TextView tvFilmTitle = convertView.findViewById(R.id.tvFilmTitle);
        TextView tvFilmDirector = convertView.findViewById(R.id.tvFilmDirector);

        Film film = getItem(position);

        ivFilmImage.setImageResource(film.getImageResId());
        tvFilmTitle.setText(film.getTitle());
        tvFilmDirector.setText(film.getDirector());

        return convertView;
    }
}