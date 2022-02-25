package com.calculator.vaultlocker.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calculator.vaultlocker.Model.NotesFileDB_Pojo;
import com.calculator.vaultlocker.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NotesFilesGridViewAdapter extends BaseAdapter {
    public boolean isEdit;
    public int notesfilesCount;
    public Context mContext;
    public LayoutInflater inflater;
    public List<Integer> focusedPosition;
    public List<NotesFileDB_Pojo> notesFileDB_PojoList;

    public NotesFilesGridViewAdapter(Context context, List<NotesFileDB_Pojo> list) {
        this.isEdit = false;
        this.notesfilesCount = 0;
        this.mContext = context;
        this.notesFileDB_PojoList = list;
        this.notesfilesCount = list.size();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.focusedPosition = new ArrayList<>();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setFocusedPosition(int i) {
        if (!focusedPosition.contains(i)) {
            focusedPosition.add(i);
        }
    }

    public void removeFocusedPosition(int i) {
        if (focusedPosition.contains(i)) {
            focusedPosition.remove((Integer) i);
        }
    }

    public void removeAllFocusedPosition() {
        focusedPosition.clear();
    }

    public void setIsEdit(boolean z) {
        isEdit = z;
    }

    @Override
    public int getCount() {
        return notesFileDB_PojoList.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (notesfilesCount > 0) {
            String str = notesFileDB_PojoList.get(i).getNotesFileModifiedDate().split(",")[0];
            String str2 = notesFileDB_PojoList.get(i).getNotesFileModifiedDate().split(", ")[1];
            Holder holder = new Holder();

            if (view == null) {
                view = inflater.inflate(R.layout.list_item_notes_folder_listview, viewGroup, false);
                holder.tv_FileName = view.findViewById(R.id.tv_FolderName);
                holder.tv_NoteFileTime = view.findViewById(R.id.tv_NoteFolderTime);
                holder.tv_NoteFileDate = view.findViewById(R.id.tv_NoteFolderDate);
                holder.tv_noteFolder_size = view.findViewById(R.id.tv_noteFolder_size);
                holder.iv_notesFile = view.findViewById(R.id.iv_notesFolder);
                holder.ll_selection = view.findViewById(R.id.ll_selection);
                holder.tv_NotesCount = view.findViewById(R.id.tv_NotesCount);
                holder.colorBorder = view.findViewById(R.id.colorBorder);
                holder.tv_NoteContent = view.findViewById(R.id.tv_NoteContent);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            double notesFileSize = notesFileDB_PojoList.get(i).getNotesFileSize();
            holder.tv_FileName.setText(notesFileDB_PojoList.get(i).getNotesFileName());
            holder.tv_NoteFileDate.setText("Date: " + str);
            holder.tv_NoteFileTime.setText("Time: " + str2);
            holder.tv_NoteFileDate.setSelected(true);
            holder.tv_NoteFileTime.setSelected(true);
            holder.tv_NotesCount.setVisibility(View.INVISIBLE);
            holder.tv_noteFolder_size.setText("Size: " + readableFileSize(notesFileSize));
            holder.tv_NoteContent.setText(notesFileDB_PojoList.get(i).getNotesFileText().trim());
            holder.iv_notesFile.setBackgroundResource(R.drawable.ic_notesfolder_thumb_icon);
            holder.colorBorder.setBackgroundColor(Color.parseColor(notesFileDB_PojoList.get(i).getNotesfileColor()));

            if (!focusedPosition.contains(i) || !isEdit) {
                holder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
            } else {
                holder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
            }
        } else {
            view.setVisibility(View.GONE);
        }

        if (!isEdit) {
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_fade_in));
        }
        return view;
    }

    public String readableFileSize(double d) {
        if (d <= 0.0d) {
            return "0";
        }
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        return new DecimalFormat("#,##0.#").format(d / Math.pow(1024.0d, log10)) + " " + new String[]{"B", "kB", "MB", "GB", "TB"}[log10];
    }

    public static class Holder {
        public ImageView colorBorder;
        public ImageView iv_notesFile;
        public LinearLayout ll_selection;
        public TextView tv_NotesCount;
        public TextView tv_FileName;
        public TextView tv_NoteContent;
        public TextView tv_NoteFileDate;
        public TextView tv_NoteFileTime;
        public TextView tv_noteFolder_size;

        public Holder() {
        }
    }
}
