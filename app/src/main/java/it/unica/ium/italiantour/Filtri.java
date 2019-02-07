package it.unica.ium.italiantour;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class Filtri {
    public List<Filtro> ITEMS;

    //TODO: add icons
    public Filtri(Context context){
        ITEMS = new ArrayList<>();
        ITEMS.add(new Filtro("Monumenti", context.getResources().getDrawable(R.drawable.ic_broken_image_white_24dp)));
        ITEMS.add(new Filtro("Ristoranti", context.getResources().getDrawable(R.drawable.ic_broken_image_white_24dp)));
        ITEMS.add(new Filtro("Arte", context.getResources().getDrawable(R.drawable.ic_broken_image_white_24dp)));
        ITEMS.add(new Filtro("Sport", context.getResources().getDrawable(R.drawable.ic_broken_image_white_24dp)));
        ITEMS.add(new Filtro("Svago", context.getResources().getDrawable(R.drawable.ic_broken_image_white_24dp)));
        ITEMS.add(new Filtro("Natura", context.getResources().getDrawable(R.drawable.ic_broken_image_white_24dp)));
    }



    public static class Filtro{
        public String title;
        public Drawable icon;

        public Filtro(String title, Drawable icon) {
            this.title = title;
            this.icon = icon;
        }
    }
}
