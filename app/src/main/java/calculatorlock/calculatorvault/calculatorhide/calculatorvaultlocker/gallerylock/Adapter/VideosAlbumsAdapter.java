package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.VideoAlbum;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

public class VideosAlbumsAdapter extends ArrayAdapter<VideoAlbum> {
    public boolean _isEdit;
    public int focusedPosition;
    public Context con;
    public LayoutInflater layoutInflater;
    public ArrayList<VideoAlbum> list;
    public DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    public VideosAlbumsAdapter(Context context, int i, ArrayList<VideoAlbum> arrayList, int i2, boolean z) {
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

        ImageView imageView = view2.findViewById(R.id.thumbImage);
        ImageView imageView2 = view2.findViewById(R.id.iv_album_thumbnil);
        ImageView imageView3 = view2.findViewById(R.id.playthumbImage);
        LinearLayout linearLayout = view2.findViewById(R.id.ll_selection);
        TextView textView = view2.findViewById(R.id.lbl_Date);

        VideoAlbum videoAlbum = list.get(i);
        String str = videoAlbum.get_modifiedDateTime().split(",")[0];
        String str2 = videoAlbum.get_modifiedDateTime().split(", ")[1];
        textView.setSelected(true);
        textView.setText("Date: " + str);
        ((TextView) view2.findViewById(R.id.lbl_Time)).setText("Time: " + str2);
        ((TextView) view2.findViewById(R.id.textAlbumName)).setText(videoAlbum.getAlbumName());
        ((TextView) view2.findViewById(R.id.lbl_Count)).setText(Integer.toString(videoAlbum.getVideoCount()));
        String albumCoverLocation = videoAlbum.getAlbumCoverLocation();

        if (focusedPosition != i || !_isEdit) {
            linearLayout.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
        } else {
            linearLayout.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
        }
        if (albumCoverLocation != null) {
            imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
            Common.imageLoader.displayImage("file:///" + list.get(i).getAlbumCoverLocation(), imageView2, options);
            imageView.setVisibility(View.INVISIBLE);
            imageView3.setBackgroundResource(R.drawable.play_video_btn);
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setBackgroundResource(R.drawable.ic_video_thumb_empty_icon);
        }
        return view2;
    }
}
