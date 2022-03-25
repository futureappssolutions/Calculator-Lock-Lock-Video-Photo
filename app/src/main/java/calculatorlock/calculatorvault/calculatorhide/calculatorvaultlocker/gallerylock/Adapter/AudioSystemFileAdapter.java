package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.AudiosImportActivity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.AudioEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;

import java.util.ArrayList;

public class AudioSystemFileAdapter extends ArrayAdapter<AudioEnt> {
    public boolean _isAllCheck = false;
    public Context con;
    public LayoutInflater layoutInflater;
    public ArrayList<AudioEnt> audioEntlist;
    public AudiosImportActivity audioImportActivity;

    public AudioSystemFileAdapter(AudiosImportActivity audiosImportActivity, int i, ArrayList<AudioEnt> arrayList) {
        super(audiosImportActivity, i, arrayList);
        this.audioImportActivity = audiosImportActivity;
        this.con = audiosImportActivity;
        this.audioEntlist = arrayList;
        this.layoutInflater = (LayoutInflater) audiosImportActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.layoutInflater.inflate(R.layout.item_audio_system_file, null);
            viewHolder = new ViewHolder();
            viewHolder.lblaudioname = view.findViewById(R.id.lbladdmiscellaneoustitleitem);
            viewHolder.cbaddaudioitem = view.findViewById(R.id.cbaddmiscellaneousitem);
            AppCompatImageView appCompatImageView = view.findViewById(R.id.imageaddmiscellaneousitem);
            AudioEnt audioEnt = this.audioEntlist.get(i);
            viewHolder.lblaudioname.setText(audioEnt.getAudioName());
            if (audioEnt.GetFileImage() != null) {
                appCompatImageView.setImageBitmap(audioEnt.GetFileImage());
            }
            if (this._isAllCheck) {
                viewHolder.cbaddaudioitem.setChecked(true);
            }
            if (Common.IsSelectAll) {
                this.audioImportActivity.SelectedItemsCount(Common.SelectedCount);
            }

            viewHolder.cbaddaudioitem.setOnCheckedChangeListener((compoundButton, z) -> {
                AudioSystemFileAdapter.this.audioEntlist.get((Integer) compoundButton.getTag()).SetFileCheck(compoundButton.isChecked());
                if (!Common.IsSelectAll) {
                    if (compoundButton.isChecked()) {
                        Common.SelectedCount++;
                        AudioSystemFileAdapter.this.audioImportActivity.SelectedItemsCount(Common.SelectedCount);
                        return;
                    }
                    Common.SelectedCount--;
                    AudioSystemFileAdapter.this.audioImportActivity.SelectedItemsCount(Common.SelectedCount);
                    Common.IsSelectAll = false;
                }
            });

            view.setTag(viewHolder);
            view.setTag(R.id.lbladdmiscellaneoustitleitem, viewHolder.lblaudioname);
            view.setTag(R.id.cbaddmiscellaneousitem, viewHolder.cbaddaudioitem);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.cbaddaudioitem.setTag(i);
        viewHolder.lblaudioname.setText(this.audioEntlist.get(i).getAudioName());
        viewHolder.cbaddaudioitem.setChecked(this.audioEntlist.get(i).GetFileCheck());
        return view;
    }

    public static class ViewHolder {
        public CheckBox cbaddaudioitem;
        public TextView lblaudioname;

        public ViewHolder() {
        }
    }
}