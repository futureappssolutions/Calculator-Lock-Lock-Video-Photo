package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Photo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.List;

public class PhoneGalleryPhotoAdapter extends ArrayAdapter<Photo> {
    public boolean isEdit;
    public Context con;
    public List<Photo> listItems;
    public LayoutInflater layoutInflater;
    public DisplayImageOptions options;

    public PhoneGalleryPhotoAdapter(Context context, int i, List<Photo> list, boolean z) {
        super(context, i, list);
        this.con = context;
        this.listItems = list;
        this.isEdit = z;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_photo_empty_icon).showImageForEmptyUri(R.drawable.ic_photo_empty_icon).showImageOnFail(R.drawable.ic_photo_empty_icon).cacheInMemory(false).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        View view2;
        if (view == null) {
            Photo photo = listItems.get(i);
            viewHolder = new ViewHolder();

            view2 = layoutInflater.inflate(R.layout.item_custom_gallery, null);
            viewHolder.ll_custom_gallery = view2.findViewById(R.id.ll_custom_gallery);
            viewHolder.ll_dark_on_click = view2.findViewById(R.id.ll_dark_on_click);
            viewHolder.iv_tick = view2.findViewById(R.id.iv_tick);
            viewHolder.imageview = view2.findViewById(R.id.thumbImage);
            viewHolder.iv_tick.setId(i);

            viewHolder.imageview.setId(i);
            if (photo.GetFileCheck()) {
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

    //    Common.imageLoader.displayImage("file:///" + listItems.get(i).getFolderLockPhotoLocation(), viewHolder.imageview);

        File imgFile = new File("file://" + listItems.get(i).getFolderLockPhotoLocation());

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

           // ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

            viewHolder.imageview.setImageBitmap(myBitmap);

        }

        Log.e("file","file://" + listItems.get(i).getFolderLockPhotoLocation());
      //          Glide.with(getContext()).load("file://" + listItems.get(i).getFolderLockPhotoLocation()).into(viewHolder.imageview);

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
        public RelativeLayout ll_custom_gallery;
        public LinearLayout ll_dark_on_click;

        ViewHolder() {
        }
    }
}
