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
        ImageView ivFilmImage;
        TextView tvFilmTitle;
        TextView tvFilmDirector;
        ImageView ivFavorite;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_film, parent, false);

            ivFilmImage = convertView.findViewById(R.id.ivFilmImage);
            tvFilmTitle = convertView.findViewById(R.id.tvFilmTitle);
            tvFilmDirector = convertView.findViewById(R.id.tvFilmDirector);
            ivFavorite = convertView.findViewById(R.id.ivFavorite);

            convertView.setTag(R.id.ivFilmImage, ivFilmImage);
            convertView.setTag(R.id.tvFilmTitle, tvFilmTitle);
            convertView.setTag(R.id.tvFilmDirector, tvFilmDirector);
            convertView.setTag(R.id.ivFavorite, ivFavorite);
        } else {
            ivFilmImage = (ImageView) convertView.getTag(R.id.ivFilmImage);
            tvFilmTitle = (TextView) convertView.getTag(R.id.tvFilmTitle);
            tvFilmDirector = (TextView) convertView.getTag(R.id.tvFilmDirector);
            ivFavorite = (ImageView) convertView.getTag(R.id.ivFavorite);
        }

        final Film film = getItem(position);

        ivFilmImage.setImageResource(film.getImageResId());
        tvFilmTitle.setText(film.getTitle());
        tvFilmDirector.setText(film.getDirector());

        if (film.isFavorite()) {
            ivFavorite.setImageResource(android.R.drawable.star_big_on);
        } else {
            ivFavorite.setImageResource(android.R.drawable.star_big_off);
        }

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                film.setFavorite(!film.isFavorite());
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}