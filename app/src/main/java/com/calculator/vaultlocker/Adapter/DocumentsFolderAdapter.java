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

import com.calculator.vaultlocker.Model.DocumentFolder;
import com.calculator.vaultlocker.R;

import java.util.ArrayList;

public class DocumentsFolderAdapter extends ArrayAdapter<DocumentFolder> {
    public boolean _isEdit;
    public int focusedPosition;
    public Context con;
    public LayoutInflater layoutInflater;
    public ArrayList<DocumentFolder> list;

    public DocumentsFolderAdapter(Context context, int i, ArrayList<DocumentFolder> arrayList, int i2, boolean z) {
        super(context, i, arrayList);
        this._isEdit = false;
        this.list = arrayList;
        this.con = context;
        this.focusedPosition = i2;
        this._isEdit = z;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;

        view2 = layoutInflater.inflate(R.layout.item_grid_layout, null);

        TextView textView = view2.findViewById(R.id.textAlbumName);
        TextView textView2 = view2.findViewById(R.id.lbl_Count);
        TextView textView3 = view2.findViewById(R.id.lbl_Date);
        ImageView imageView = view2.findViewById(R.id.thumbImage);
        LinearLayout linearLayout = view2.findViewById(R.id.ll_selection);

        DocumentFolder documentFolder = list.get(i);
        String str = documentFolder.get_modifiedDateTime().split(",")[0];
        String str2 = documentFolder.get_modifiedDateTime().split(", ")[1];

        textView3.setSelected(true);
        textView3.setText("Date: " + str);
        ((TextView) view2.findViewById(R.id.lbl_Time)).setText("Time: " + str2);
        textView.setText(documentFolder.getFolderName());
        textView.setSelected(true);

        if (focusedPosition != i || !_isEdit) {
            linearLayout.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
        } else {
            linearLayout.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
        }

        int i2 = documentFolder.get_fileCount();
        textView2.setText(Integer.toString(i2));
        if (i2 > 0) {
            imageView.setBackgroundResource(R.drawable.ic_notesfolder_thumb_icon);
        } else {
            imageView.setBackgroundResource(R.drawable.ic_documentsfolder_empty_thumb_icon);
        }
        return view2;
    }
}
