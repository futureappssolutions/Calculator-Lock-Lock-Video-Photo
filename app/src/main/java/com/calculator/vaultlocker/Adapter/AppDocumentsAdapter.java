package com.calculator.vaultlocker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calculator.vaultlocker.Model.DocumentsEnt;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.utilities.Utilities;

import java.util.List;

public class AppDocumentsAdapter extends ArrayAdapter<DocumentsEnt> {
    public boolean isEdit;
    public Context con;
    public List<DocumentsEnt> listItems;
    public LayoutInflater layoutInflater;

    public AppDocumentsAdapter(Context context, int i, List<DocumentsEnt> list, boolean z) {
        super(context, i, list);
        this.con = context;
        this.listItems = list;
        this.isEdit = z;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        View view2;
        if (view == null) {
            viewHolder = new ViewHolder();

            view2 = layoutInflater.inflate(R.layout.item_documents_detail, null);
            viewHolder.textAlbumName = view2.findViewById(R.id.textAlbumName);
            viewHolder.rl_thumimage = view2.findViewById(R.id.rl_thumimage);
            viewHolder.ll_selection = view2.findViewById(R.id.ll_selection);
            viewHolder.lbl_Date = view2.findViewById(R.id.lbl_Date);
            viewHolder.lbl_Time = view2.findViewById(R.id.lbl_Time);
            viewHolder.lbl_Size = view2.findViewById(R.id.lbl_Size);
            viewHolder.imageview = view2.findViewById(R.id.thumbImage);

            if (listItems.get(i).GetFileCheck()) {
                viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
            } else {
                viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
            }

            if (isEdit) {
                viewHolder.rl_thumimage.setOnClickListener(view3 -> {
                    int intValue = (Integer) view3.getTag();
                    if (listItems.get(intValue).GetFileCheck()) {
                        viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
                        listItems.get(intValue).SetFileCheck(false);
                        return;
                    }
                    viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
                    listItems.get(intValue).SetFileCheck(true);
                });
            }

            view2.setTag(viewHolder);
            view2.setTag(R.id.thumbImage, viewHolder.imageview);
            view2.setTag(R.id.rl_thumimage, viewHolder.rl_thumimage);
            view = view2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.rl_thumimage.setTag(i);
        
        viewHolder.textAlbumName.setText(listItems.get(i).getDocumentName());
        String str = listItems.get(i).get_modifiedDateTime().split(",")[0];
        String str2 = listItems.get(i).get_modifiedDateTime().split(", ")[1];
        viewHolder.lbl_Date.setText(str);
        viewHolder.lbl_Time.setText(str2);
        viewHolder.lbl_Size.setText(Utilities.FileSize(listItems.get(i).getFolderLockDocumentLocation()));

        if (listItems.get(i).GetFileCheck()) {
            viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
        } else {
            viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
        }
        viewHolder.id = i;
        return view;
    }
    
    static class ViewHolder {
        public int id;
        public ImageView imageview;
        public TextView lbl_Date;
        public TextView lbl_Size;
        public TextView lbl_Time;
        public TextView textAlbumName;
        public LinearLayout ll_selection;
        public RelativeLayout rl_thumimage;
        
        ViewHolder() {
        }
    }
}
