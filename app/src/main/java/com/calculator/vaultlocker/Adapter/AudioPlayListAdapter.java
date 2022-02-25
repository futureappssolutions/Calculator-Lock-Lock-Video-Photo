package com.calculator.vaultlocker.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calculator.vaultlocker.Model.AudioPlayListEnt;
import com.calculator.vaultlocker.R;

import java.util.ArrayList;

public class AudioPlayListAdapter extends ArrayAdapter<AudioPlayListEnt> {
    public boolean _isEdit;
    public boolean _isGridView;
    public int focusedPosition;
    public Context con;
    public LayoutInflater layoutInflater;
    public ArrayList<AudioPlayListEnt> list;

    public AudioPlayListAdapter(Context context, int i, ArrayList<AudioPlayListEnt> arrayList, int i2, boolean z, boolean z2) {
        super(context, i, arrayList);
        this._isEdit = false;
        this._isGridView = false;
        this.list = arrayList;
        this.con = context;
        this.focusedPosition = i2;
        this._isEdit = z;
        this._isGridView = z2;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        if (_isGridView) {
            view2 = layoutInflater.inflate(R.layout.item_grid_layout, null);
        } else {
            view2 = layoutInflater.inflate(R.layout.item_list_layout, null);
        }

        TextView textView = view2.findViewById(R.id.textAlbumName);
        TextView textView2 = view2.findViewById(R.id.lbl_Count);
        ImageView imageView = view2.findViewById(R.id.thumbImage);
        LinearLayout linearLayout = view2.findViewById(R.id.ll_selection);
        TextView textView3 = view2.findViewById(R.id.lbl_Date);

        AudioPlayListEnt audioPlayListEnt = list.get(i);
        String str = audioPlayListEnt.get_modifiedDateTime().split(",")[0];
        String str2 = audioPlayListEnt.get_modifiedDateTime().split(", ")[1];
        textView3.setSelected(true);
        textView3.setText("Date: " + str);
        ((TextView) view2.findViewById(R.id.lbl_Time)).setText("Time: " + str2);
        textView.setText(audioPlayListEnt.getPlayListName());
        textView.setSelected(true);

        if (focusedPosition != i || !_isEdit) {
            linearLayout.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
        } else {
            linearLayout.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
        }

        textView2.setText(Integer.toString(audioPlayListEnt.get_fileCount()));

        if (_isGridView) {
            if (list.get(i).get_fileCount() > 0) {
                imageView.setBackgroundResource(R.drawable.ic_audiosfolder_thumb_icon);
            } else {
                imageView.setBackgroundResource(R.drawable.ic_audiosfolder_empty_thumb_icon);
            }
        } else if (audioPlayListEnt.get_fileCount() > 0) {
            imageView.setBackgroundResource(R.drawable.ic_audiosfolder_thumb_icon);
        } else {
            imageView.setBackgroundResource(R.drawable.ic_audiosfolder_empty_thumb_icon);
        }

        return view2;
    }
}
