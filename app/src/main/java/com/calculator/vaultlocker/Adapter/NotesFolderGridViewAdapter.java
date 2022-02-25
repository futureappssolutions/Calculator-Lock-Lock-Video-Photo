package com.calculator.vaultlocker.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calculator.vaultlocker.DB.NotesFilesDAL;
import com.calculator.vaultlocker.Model.NotesFolderDB_Pojo;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.common.Constants;

import java.text.DecimalFormat;
import java.util.List;

public class NotesFolderGridViewAdapter extends BaseAdapter {
    public int focusedPosition;
    public int foldersCount;
    public int[] notesCount;
    public double[] folderSize;
    public boolean isEdit;
    public boolean isGridView;
    public Context mContext;
    public Constants constants;
    public LayoutInflater inflater;
    public NotesFilesDAL notesFilesDAL;
    public List<NotesFolderDB_Pojo> notesFolderPojoList;

    public NotesFolderGridViewAdapter(Context context, List<NotesFolderDB_Pojo> list) {
        int i = 0;
        this.focusedPosition = 0;
        this.isEdit = false;
        this.isGridView = true;
        this.mContext = context;
        this.notesFolderPojoList = list;
        this.foldersCount = list.size();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.notesCount = new int[list.size()];
        this.folderSize = new double[list.size()];
        this.constants = new Constants();
        this.notesFilesDAL = new NotesFilesDAL(context);

        while (true) {
            int[] iArr = notesCount;
            if (i < iArr.length) {
                constants.getClass();
                StringBuilder sb = new StringBuilder();
                constants.getClass();
                sb.append("NotesFolderId");
                sb.append(" = ");
                sb.append(list.get(i).getNotesFolderId());
                iArr[i] = notesFilesDAL.getNotesFilesCount("SELECT \t\tCOUNT(*) \t\t\t\t   FROM TableNotesFile WHERE ".concat(sb.toString()));
                double[] dArr = folderSize;
                constants.getClass();
                StringBuilder sb2 = new StringBuilder();
                constants.getClass();
                sb2.append("NotesFolderId");
                sb2.append(" = ");
                sb2.append(list.get(i).getNotesFolderId());
                dArr[i] = notesFilesDAL.GetNotesFileRealEntity("SELECT SUM (NotesFileSize) FROM  TableNotesFile WHERE ".concat(sb2.toString()));
                i++;
            } else {
                return;
            }
        }
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
        focusedPosition = i;
    }

    public void setIsEdit(boolean z) {
        isEdit = z;
    }

    public void setIsGridview(boolean z) {
        isGridView = z;
    }

    @Override
    public int getCount() {
        return notesFolderPojoList.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (foldersCount > 0) {
            String str = notesFolderPojoList.get(i).getNotesFolderModifiedDate().split(",")[0];
            String str2 = notesFolderPojoList.get(i).getNotesFolderModifiedDate().split(", ")[1];
            Holder holder = new Holder();
            if (view == null) {

                view = inflater.inflate(R.layout.list_item_notes_folder_listview, viewGroup, false);

                holder.tv_FolderName = view.findViewById(R.id.tv_FolderName);
                holder.tv_NoteFolderDate = view.findViewById(R.id.tv_NoteFolderDate);
                holder.tv_NoteFolderTime = view.findViewById(R.id.tv_NoteFolderTime);
                holder.tv_noteFolder_size = view.findViewById(R.id.tv_noteFolder_size);
                holder.tv_NotesCount = view.findViewById(R.id.tv_NotesCount);
                holder.iv_notesFolder = view.findViewById(R.id.iv_notesFolder);
                holder.ll_selection = view.findViewById(R.id.ll_selection);
                holder.colorBorder = view.findViewById(R.id.colorBorder);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            holder.tv_FolderName.setText(notesFolderPojoList.get(i).getNotesFolderName());
            holder.tv_NoteFolderDate.setText("Date: " + str);
            holder.tv_NoteFolderTime.setText("Time: " + str2);
            holder.tv_NoteFolderDate.setSelected(true);
            holder.tv_NoteFolderTime.setSelected(true);
            holder.tv_NotesCount.setText(String.valueOf(notesCount[i]));

            if (notesCount[i] > 0) {
                holder.iv_notesFolder.setBackgroundResource(R.drawable.ic_notesfolder_thumb_icon);
            } else {
                holder.iv_notesFolder.setBackgroundResource(R.drawable.ic_notesfolder_empty_thumb_icon);
            }
            holder.tv_noteFolder_size.setText("Size: " + readableFileSize(folderSize[i]));


            if (notesFolderPojoList.get(i).getNotesFolderName().equals("My Notes")) {
                holder.colorBorder.setBackgroundColor(mContext.getResources().getColor(R.color.app_color));
            } else {
                holder.colorBorder.setBackgroundColor(Integer.parseInt(notesFolderPojoList.get(i).getNotesFolderColor()));
            }

            if (focusedPosition != i || !isEdit) {
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
            return "0 B";
        }
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        return new DecimalFormat("#,##0.#").format(d / Math.pow(1024.0d, log10)) + " " + new String[]{"B", "KB", "MB", "GB", "TB"}[log10];
    }


    public static class Holder {
        public ImageView colorBorder;
        public ImageView iv_notesFolder;
        public LinearLayout ll_selection;
        public TextView tv_FolderName;
        public TextView tv_NoteFolderDate;
        public TextView tv_NoteFolderTime;
        public TextView tv_NotesCount;
        public TextView tv_noteFolder_size;

        public Holder() {
        }
    }
}
