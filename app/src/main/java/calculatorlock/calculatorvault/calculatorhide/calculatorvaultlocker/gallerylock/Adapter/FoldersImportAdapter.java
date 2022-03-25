package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
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

import java.io.File;
import java.util.List;

public class FoldersImportAdapter extends RecyclerView.Adapter<FoldersImportAdapter.ViewHolder> {
    public boolean IsAlbumSelect;
    public Context con;
    public List<ImportAlbumEnt> importAlbumEnts;
    public OnItemClickListener listener;

    public FoldersImportAdapter(Context context, List<ImportAlbumEnt> list, boolean z, OnItemClickListener listener) {
        this.IsAlbumSelect = false;
        this.importAlbumEnts = list;
        this.con = context;
        this.IsAlbumSelect = z;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio_file_import, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.imageAlbum.setImageResource(R.drawable.ic_document_list_empty_icon);

        if (!this.IsAlbumSelect) {
            for (int i2 = 0; i2 < this.importAlbumEnts.size(); i2++) {
                this.importAlbumEnts.get(i2).SetAlbumFileCheck(false);
            }
        }

        viewHolder.checkbox.setOnCheckedChangeListener((compoundButton, z) -> FoldersImportAdapter.this.importAlbumEnts.get(position).SetAlbumFileCheck(compoundButton.isChecked()));
        viewHolder.textAlbumName.setText(new File(this.importAlbumEnts.get(position).GetAlbumName()).getName());
        viewHolder.textAlbumName.setSelected(true);
        viewHolder.textAlbumName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.textAlbumName.setSingleLine(true);

        viewHolder.itemView.setOnClickListener(view -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return importAlbumEnts.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;
        public ImageView imageAlbum;
        public TextView textAlbumName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.cb_import_album_item);
            textAlbumName = itemView.findViewById(R.id.lbl_import_album_item);
            imageAlbum = itemView.findViewById(R.id.thumbnil_import_album_titem);
        }
    }
}
