package c.liyueyun.mjmall.tv.base.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.base.base.Tool;


/**
 * Created by SongJie on 05/15 0015.
 */

public class GlideCache implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置图片的显示格式ARGB_8888(指图片大小为32bit)
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //设置磁盘缓存目录（和创建的缓存目录相同）
        String downloadDirectoryPath= Tool.getSavePath(MyConstant.folderImageCache);
        //设置缓存的大小为100M
        int cacheSize = 100*1000*1000;
        builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize));
        ViewTarget.setTagId(R.id.image_tag);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}