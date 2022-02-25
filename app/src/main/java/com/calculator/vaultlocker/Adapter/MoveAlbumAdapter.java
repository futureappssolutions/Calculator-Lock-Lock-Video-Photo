package com.calculator.vaultlocker.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.calculator.vaultlocker.R;

import java.util.List;

public class MoveAlbumAdapter extends ArrayAdapter<String> {
    public int path;
    public Context con;
    public List<String> Albumlist;
    public LayoutInflater layoutInflater;

    public MoveAlbumAdapter(Context context, int i, List<String> list, int i2) {
        super(context, i, list);
        this.Albumlist = list;
        this.con = context;
        this.path = i2;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R.layout.item_move_list, null);
        TextView textView = inflate.findViewById(R.id.lblmoveitem);
        ((ImageView) inflate.findViewById(R.id.imgurlitem)).setImageResource(path);
        textView.setText(Albumlist.get(i));
        return inflate;
    }
}
