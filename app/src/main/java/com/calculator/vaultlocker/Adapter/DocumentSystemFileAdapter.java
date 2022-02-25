package com.calculator.vaultlocker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calculator.vaultlocker.Model.DocumentsEnt;
import com.calculator.vaultlocker.R;

import java.util.ArrayList;

public class DocumentSystemFileAdapter extends ArrayAdapter<DocumentsEnt> {
    public ArrayList<DocumentsEnt> documentEntlist;
    public LayoutInflater layoutInflater;
    public Context con;

    public DocumentSystemFileAdapter(Context context, int i, ArrayList<DocumentsEnt> arrayList) {
        super(context, i, arrayList);
        this.con = context;
        this.documentEntlist = arrayList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_documents_detail, null);
            viewHolder = new ViewHolder();

            viewHolder.lbldocumenttitle = view.findViewById(R.id.textAlbumName);
            viewHolder.rl_thumimage = view.findViewById(R.id.rl_thumimage);
            viewHolder.ll_selection = view.findViewById(R.id.ll_selection);

            viewHolder.lbldocumenttitle.setText(documentEntlist.get(i).getDocumentName());
            if (documentEntlist.get(i).GetFileCheck()) {
                viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
            } else {
                viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
            }

            viewHolder.rl_thumimage.setOnClickListener(view2 -> {
                int intValue = (Integer) view2.getTag();
                if (documentEntlist.get(intValue).GetFileCheck()) {
                    viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
                    documentEntlist.get(intValue).SetFileCheck(false);
                    return;
                }
                viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
                documentEntlist.get(intValue).SetFileCheck(true);
            });

            view.setTag(viewHolder);
            view.setTag(R.id.textAlbumName, viewHolder.lbldocumenttitle);
            view.setTag(R.id.rl_thumimage, viewHolder.rl_thumimage);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.rl_thumimage.setTag(i);
        viewHolder.lbldocumenttitle.setText(documentEntlist.get(i).getDocumentName());
        if (documentEntlist.get(i).GetFileCheck()) {
            viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
        } else {
            viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
        }
        viewHolder.id = i;
        return view;
    }

    public static class ViewHolder {
        public int id;
        public TextView lbldocumenttitle;
        public LinearLayout ll_selection;
        public RelativeLayout rl_thumimage;

        public ViewHolder() {
        }
    }
}
