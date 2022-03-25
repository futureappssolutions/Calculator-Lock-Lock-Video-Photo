package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ImportEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;

import java.util.ArrayList;

public class GalleryVideoAdapter extends ArrayAdapter<ImportEnt> {
    public Context con;
    public ArrayList<ImportEnt> importEntList;
    public LayoutInflater layoutInflater;

    public GalleryVideoAdapter(Context context, int i, ArrayList<ImportEnt> arrayList) {
        super(context, i, arrayList);
        this.con = context;
        this.importEntList = arrayList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        View view2;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = this.layoutInflater.inflate(R.layout.item_custom_gallery, null);
            viewHolder.imageview = view2.findViewById(R.id.thumbImage);
            viewHolder.ll_custom_gallery = view2.findViewById(R.id.ll_custom_gallery);
            viewHolder.ll_dark_on_click = view2.findViewById(R.id.ll_dark_on_click);
            viewHolder.playimageAlbum = view2.findViewById(R.id.playthumbImage);
            viewHolder.iv_tick = view2.findViewById(R.id.iv_tick);

            final ImportEnt importEnt = this.importEntList.get(i);
            new Thread() {
                @Override
                public void run() {
                    if (importEnt.GetThumbnail() == null) {
                        importEnt.SetThumbnail(MediaStore.Video.Thumbnails.getThumbnail(GalleryVideoAdapter.this.getContext().getContentResolver(), importEnt.GetId(), 3, null));
                    }
                    viewHolder.imageview.setImageBitmap(importEnt.GetThumbnail());
                }
            }.start();

            viewHolder.playimageAlbum.setBackgroundResource(R.drawable.play_video_btn);
            if (importEnt.GetThumbnailSelection()) {
                viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
                viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
                viewHolder.iv_tick.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
                viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
                viewHolder.iv_tick.setVisibility(View.INVISIBLE);
            }

            viewHolder.imageview.setBackgroundColor(0);
            viewHolder.ll_custom_gallery.setId(i);
            viewHolder.imageview.setId(i);
            viewHolder.iv_tick.setId(i);

            viewHolder.imageview.setOnClickListener(view3 -> {
                int intValue = (Integer) view3.getTag();
                if (GalleryVideoAdapter.this.importEntList.get(intValue).GetThumbnailSelection()) {
                    viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
                    GalleryVideoAdapter.this.importEntList.get(intValue).SetThumbnailSelection(false);
                    viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
                    viewHolder.iv_tick.setVisibility(View.INVISIBLE);
                    return;
                }
                viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
                GalleryVideoAdapter.this.importEntList.get(intValue).SetThumbnailSelection(true);
                viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
                viewHolder.iv_tick.setVisibility(View.VISIBLE);
            });
            view2.setTag(viewHolder);
            view2.setTag(viewHolder);
            view2.setTag(R.id.thumbImage, viewHolder.imageview);
            view2.setTag(R.id.iv_tick, viewHolder.iv_tick);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            view2 = view;
        }
        viewHolder.imageview.setTag(i);
        viewHolder.iv_tick.setTag(i);
        if (this.importEntList.get(i).GetThumbnail() == null) {
            this.importEntList.get(i).SetThumbnail(MediaStore.Video.Thumbnails.getThumbnail(getContext().getContentResolver(), this.importEntList.get(i).GetId(), 3, null));
            viewHolder.imageview.setImageBitmap(this.importEntList.get(i).GetThumbnail());
        } else {
            viewHolder.imageview.setImageBitmap(this.importEntList.get(i).GetThumbnail());
        }
        if (this.importEntList.get(i).GetThumbnailSelection()) {
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
        public RelativeLayout ll_custom_gallery;
        public LinearLayout ll_dark_on_click;
        public ImageView playimageAlbum;

        ViewHolder() {
        }
    }
}
