package com.calculator.vaultlocker.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calculator.vaultlocker.Model.PhotoAlbum;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.utilities.Common;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

public class PhotosAlbumsAdapter extends ArrayAdapter<PhotoAlbum> {
    public boolean isEdit;
    public int focusedPosition;
    public Context con;
    public LayoutInflater layoutInflater;
    public ArrayList<PhotoAlbum> list;
    public Resources res;
    public DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    public PhotosAlbumsAdapter(Context context, int i, ArrayList<PhotoAlbum> arrayList, int i2, boolean z) {
        super(context, i, arrayList);
        this.isEdit = false;
        this.res = context.getResources();
        this.list = arrayList;
        this.con = context;
        this.focusedPosition = i2;
        this.isEdit = z;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        view2 = layoutInflater.inflate(R.layout.item_grid_layout, null);

        TextView textAlbumName = view2.findViewById(R.id.textAlbumName);
        TextView lbl_Count = view2.findViewById(R.id.lbl_Count);
        TextView lbl_Date = view2.findViewById(R.id.lbl_Date);
        TextView lbl_Time = view2.findViewById(R.id.lbl_Time);
        ImageView thumbImage = view2.findViewById(R.id.thumbImage);
        ImageView iv_album_thumbnil = view2.findViewById(R.id.iv_album_thumbnil);
        LinearLayout ll_selection = view2.findViewById(R.id.ll_selection);

        String str = list.get(i).get_modifiedDateTime().split(",")[0];
        String str2 = list.get(i).get_modifiedDateTime().split(", ")[1];
        lbl_Date.setSelected(true);
        lbl_Date.setText("Date: " + str);
        lbl_Time.setText("Time: " + str2);
        textAlbumName.setText(list.get(i).getAlbumName());
        textAlbumName.setSelected(true);

        if (focusedPosition != i || !isEdit) {
            ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
        } else {
            ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
        }

        lbl_Count.setText(Integer.toString(list.get(i).getPhotoCount()));

        thumbImage.setBackgroundResource(R.drawable.ic_photo_thumb_empty_icon);

        if (list.get(i).getAlbumCoverLocation() != null) {
            Common.imageLoader.displayImage("file:///" + list.get(i).getAlbumCoverLocation(), iv_album_thumbnil, options);
            thumbImage.setVisibility(View.INVISIBLE);
        }
        return view2;
    }
}
