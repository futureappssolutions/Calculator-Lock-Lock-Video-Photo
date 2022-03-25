package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ImportAlbumEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.OnItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

public class AlbumsImportGalleryPhotoAdapter extends RecyclerView.Adapter<AlbumsImportGalleryPhotoAdapter.ViewHolder> {
    public boolean IsAlbumSelect;
    public boolean IsVideo;
    public Context con;
    public ArrayList<ImportAlbumEnt> importAlbumEnts;
    public OnItemClickListener listener;
    public DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.empty_folder_album_icon).showImageForEmptyUri(R.drawable.empty_folder_album_icon).showImageOnFail(R.drawable.empty_folder_album_icon).cacheInMemory(false).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    public AlbumsImportGalleryPhotoAdapter(Context context, ArrayList<ImportAlbumEnt> arrayList, boolean z, boolean z2, OnItemClickListener listener) {
        this.IsAlbumSelect = false;
        this.IsVideo = false;
        this.importAlbumEnts = arrayList;
        this.con = context;
        this.IsAlbumSelect = z;
        this.IsVideo = z2;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_import_album_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.imageAlbum.setBackgroundColor(0);
        viewHolder.checkbox.setId(position);
        viewHolder.imageAlbum.setId(position);

        if (!IsAlbumSelect) {
            for (int i2 = 0; i2 < importAlbumEnts.size(); i2++) {
                importAlbumEnts.get(i2).SetAlbumFileCheck(false);
            }
        }

        viewHolder.checkbox.setOnCheckedChangeListener((compoundButton, z) -> importAlbumEnts.get(position).SetAlbumFileCheck(compoundButton.isChecked()));

        viewHolder.textAlbumName.setText(new File(importAlbumEnts.get(position).GetAlbumName()).getName());
        viewHolder.textAlbumName.setSelected(true);
        viewHolder.textAlbumName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.textAlbumName.setSingleLine(true);
        if (IsVideo) {
            ImageLoader imageLoader = Common.imageLoader;
            imageLoader.displayImage("file:///" + importAlbumEnts.get(position).GetPath(), viewHolder.imageAlbum, options);
            viewHolder.playimageAlbum.setVisibility(View.VISIBLE);
            viewHolder.playimageAlbum.setImageResource(R.drawable.play_video_btn);
        } else {
            viewHolder.playimageAlbum.setVisibility(View.INVISIBLE);
            ImageLoader imageLoader2 = Common.imageLoader;
            imageLoader2.displayImage("file:///" + importAlbumEnts.get(position).GetPath(), viewHolder.imageAlbum, options);
        }

        viewHolder.itemView.setOnClickListener(view -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return importAlbumEnts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        ImageView imageAlbum;
        ImageView playimageAlbum;
        TextView textAlbumName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.cb_import_album_item);
            imageAlbum = itemView.findViewById(R.id.thumbnil_import_album_titem);
            playimageAlbum = itemView.findViewById(R.id.playimageAlbum);
            textAlbumName = itemView.findViewById(R.id.lbl_import_album_item);
        }
    }
}
