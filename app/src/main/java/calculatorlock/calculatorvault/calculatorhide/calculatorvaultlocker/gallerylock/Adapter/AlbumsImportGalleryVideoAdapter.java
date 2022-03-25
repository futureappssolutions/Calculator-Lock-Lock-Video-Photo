package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
import android.provider.MediaStore;
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
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.OnItemClickListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import java.io.File;
import java.util.ArrayList;

public class AlbumsImportGalleryVideoAdapter extends RecyclerView.Adapter<AlbumsImportGalleryVideoAdapter.ViewHolder> {
    public boolean IsAlbumSelect;
    public boolean IsVideo;
    public Context con;
    public OnItemClickListener listener;
    public ArrayList<ImportAlbumEnt> importAlbumEnts;

    public AlbumsImportGalleryVideoAdapter(Context context, ArrayList<ImportAlbumEnt> arrayList, boolean z, boolean z2, OnItemClickListener listener) {
        this.IsAlbumSelect = false;
        this.IsVideo = false;
        this.con = context;
        this.importAlbumEnts = arrayList;
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
        if (!this.IsAlbumSelect) {
            for (int i2 = 0; i2 < this.importAlbumEnts.size(); i2++) {
                this.importAlbumEnts.get(i2).SetAlbumFileCheck(false);
            }
        }

        viewHolder.checkbox.setOnCheckedChangeListener((compoundButton, z) -> AlbumsImportGalleryVideoAdapter.this.importAlbumEnts.get((Integer) compoundButton.getTag()).SetAlbumFileCheck(compoundButton.isChecked()));

        viewHolder.textAlbumName.setText(new File(this.importAlbumEnts.get(position).GetAlbumName()).getName());
        viewHolder.textAlbumName.setSelected(true);
        viewHolder.textAlbumName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.textAlbumName.setSingleLine(true);

        if (this.IsVideo) {
            viewHolder.imageAlbum.setImageBitmap(MediaStore.Video.Thumbnails.getThumbnail(con.getContentResolver(), this.importAlbumEnts.get(position).GetId(), 3, null));
            viewHolder.playimageAlbum.setVisibility(View.VISIBLE);
            viewHolder.playimageAlbum.setImageResource(R.drawable.play_video_btn);
        } else {
            viewHolder.playimageAlbum.setVisibility(View.INVISIBLE);
            viewHolder.imageAlbum.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.imageAlbum.setImageBitmap(Utilities.DecodeFile(new File(this.importAlbumEnts.get(position).GetPath())));
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
            textAlbumName = itemView.findViewById(R.id.lbl_import_album_item);
            imageAlbum = itemView.findViewById(R.id.thumbnil_import_album_titem);
            playimageAlbum = itemView.findViewById(R.id.playimageAlbum);
        }
    }
}
