package com.calculator.vaultlocker.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.calculator.vaultlocker.Model.Video;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.utilities.Common;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

public class AppGalleryVideoAdapter extends ArrayAdapter<Video> {
    public boolean isEdit;
    public Context con;
    public List<Video> listItems;
    public LayoutInflater layoutInflater;
    public DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_video_empty_icon).showImageForEmptyUri(R.drawable.ic_video_empty_icon).showImageOnFail(R.drawable.ic_video_empty_icon).cacheInMemory(false).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    public AppGalleryVideoAdapter(Context context, int i, List<Video> list, boolean z) {
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
            Video video = listItems.get(i);
            viewHolder = new ViewHolder();

            view2 = layoutInflater.inflate(R.layout.item_custom_gallery, null);
            viewHolder.ll_custom_gallery = view2.findViewById(R.id.ll_custom_gallery);
            viewHolder.ll_dark_on_click = view2.findViewById(R.id.ll_dark_on_click);
            viewHolder.iv_tick = view2.findViewById(R.id.iv_tick);
            viewHolder.imageview = view2.findViewById(R.id.thumbImage);
            viewHolder.playthumbImage = view2.findViewById(R.id.playthumbImage);
            viewHolder.iv_tick.setId(i);
            viewHolder.imageview.setId(i);
            viewHolder.playthumbImage.setBackgroundResource(R.drawable.play_video_btn);

            if (video.GetFileCheck()) {
                viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
                viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
                viewHolder.iv_tick.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
                viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
                viewHolder.iv_tick.setVisibility(View.INVISIBLE);
            }

            if (isEdit) {
                viewHolder.imageview.setOnClickListener(view3 -> {
                    int intValue = (Integer) view3.getTag();
                    if (listItems.get(intValue).GetFileCheck()) {
                        viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
                        viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
                        listItems.get(intValue).SetFileCheck(false);
                        viewHolder.iv_tick.setVisibility(View.INVISIBLE);
                        return;
                    }
                    viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
                    viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
                    listItems.get(intValue).SetFileCheck(true);
                    viewHolder.iv_tick.setVisibility(View.VISIBLE);

                });
            }
            view2.setTag(viewHolder);
            view2.setTag(R.id.thumbImage, viewHolder.imageview);
            view2.setTag(R.id.iv_tick, viewHolder.iv_tick);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            view2 = view;
        }
        viewHolder.imageview.setTag(i);
        viewHolder.iv_tick.setTag(i);

        Common.imageLoader.displayImage("file:///" + listItems.get(i).getthumbnail_video_location(), viewHolder.imageview, options);

        if (listItems.get(i).GetFileCheck()) {
            viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
            viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
            viewHolder.iv_tick.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
            viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
            viewHolder.iv_tick.setVisibility(View.INVISIBLE);
        }

        viewHolder.id = i;
        return view2;
    }

    static class ViewHolder {
        public int id;
        public ImageView imageview;
        public ImageView iv_tick;
        public ImageView playthumbImage;
        public RelativeLayout ll_custom_gallery;
        public LinearLayout ll_dark_on_click;

        ViewHolder() {
        }
    }
}
