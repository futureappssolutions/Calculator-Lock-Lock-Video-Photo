package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.NotesFolderDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;

import java.util.List;

public class MoveNoteAdapter extends BaseAdapter {
    public Context con;
    public LayoutInflater layoutInflater;
    public List<NotesFolderDB_Pojo> noteFolderlist;

    public MoveNoteAdapter(Context context, List<NotesFolderDB_Pojo> list) {
        this.noteFolderlist = list;
        this.con = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            view = this.layoutInflater.inflate(R.layout.item_move_list, viewGroup, false);
            viewHolder.lblmoveitem = view.findViewById(R.id.lblmoveitem);
            viewHolder.imgurlitem = view.findViewById(R.id.imgurlitem);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.lblmoveitem.setText(this.noteFolderlist.get(i).getNotesFolderName());
        viewHolder.imgurlitem.setVisibility(View.GONE);
        return view;
    }

    @Override
    public int getCount() {
        return this.noteFolderlist.size();
    }

    public static class ViewHolder {
        public ImageView imgurlitem;
        public TextView lblmoveitem;

        public ViewHolder() {
        }
    }
}
