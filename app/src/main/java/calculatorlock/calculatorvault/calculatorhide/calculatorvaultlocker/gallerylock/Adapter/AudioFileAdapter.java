package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.AudioActivity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.AudioEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import java.util.List;

public class AudioFileAdapter extends ArrayAdapter<AudioEnt> {
    public Context con;
    public AudioActivity audioActivity;
    public List<AudioEnt> listItems;
    public LayoutInflater layoutInflater;
    public boolean isEdit;

    public AudioFileAdapter(AudioActivity audioActivity, Context context, int i, List<AudioEnt> list, Boolean bool, int i2) {
        super(context, i, list);
        this.audioActivity = audioActivity;
        this.con = context;
        this.listItems = list;
        this.isEdit = bool;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        View view2;
        if (view == null) {
            viewHolder = new ViewHolder();

            view2 = layoutInflater.inflate(R.layout.item_audio_files, null);
            viewHolder.textAlbumName = view2.findViewById(R.id.textAlbumName);
            viewHolder.rl_thumimage = view2.findViewById(R.id.rl_thumimage);
            viewHolder.ll_selection = view2.findViewById(R.id.ll_selection);
            viewHolder.lbl_Date = view2.findViewById(R.id.lbl_Date);
            viewHolder.lbl_Time = view2.findViewById(R.id.lbl_Time);
            viewHolder.lbl_Size = view2.findViewById(R.id.lbl_Size);
            viewHolder.imageview = view2.findViewById(R.id.thumbImage);
            
            if (listItems.get(i).GetFileCheck()) {
                viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
                if (!Common.IsSelectAll) {
                    Common.SelectedCount++;
                    audioActivity.SelectedItemsCount(Common.SelectedCount);
                }
            } else {
                viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
            }

            if (isEdit) {
                viewHolder.rl_thumimage.setOnClickListener(view3 -> {
                    int intValue = (Integer) view3.getTag();
                    if (listItems.get(intValue).GetFileCheck()) {
                        viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
                        listItems.get(intValue).SetFileCheck(false);
                        Common.SelectedCount--;
                        audioActivity.SelectedItemsCount(Common.SelectedCount);
                        Common.IsSelectAll = false;
                        return;
                    }
                    viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
                    listItems.get(intValue).SetFileCheck(true);
                    if (!Common.IsSelectAll) {
                        Common.SelectedCount++;
                        audioActivity.SelectedItemsCount(Common.SelectedCount);
                    }
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
        
        viewHolder.textAlbumName.setText(listItems.get(i).getAudioName());
        String str = listItems.get(i).get_modifiedDateTime().split(",")[0];
        String str2 = listItems.get(i).get_modifiedDateTime().split(", ")[1];
        viewHolder.lbl_Date.setText(str);
        viewHolder.lbl_Time.setText(str2);
        viewHolder.lbl_Size.setText(Utilities.FileSize(listItems.get(i).getFolderLockAudioLocation()));

        if (listItems.get(i).GetFileCheck()) {
            viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
            audioActivity.SelectedItemsCount(Common.SelectedCount);
        } else {
            viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
        }
        viewHolder.id = i;
        return view;
    }

    public static class ViewHolder {
        int id;
        ImageView imageview;
        TextView lbl_Date;
        TextView lbl_Size;
        TextView lbl_Time;
        TextView textAlbumName;
        LinearLayout ll_selection;
        RelativeLayout rl_thumimage;

        public ViewHolder() {
        }
    }
}
