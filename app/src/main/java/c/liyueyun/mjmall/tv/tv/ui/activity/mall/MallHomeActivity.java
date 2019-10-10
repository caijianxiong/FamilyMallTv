package c.liyueyun.mjmall.tv.tv.ui.activity.mall;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallHomeMenuResult;
import c.liyueyun.mjmall.tv.base.httpApi.response.UpdateResult;
import c.liyueyun.mjmall.tv.base.manage.UpdateManager;
import c.liyueyun.mjmall.tv.tv.manager.FamilyGroupUpDateTimeManage;
import c.liyueyun.mjmall.tv.tv.ui.base.BaseActivity;
import c.liyueyun.mjmall.tv.tv.ui.widget.dialog.DialogChoice;
import c.liyueyun.mjmall.tv.tv.ui.widget.dialog.DialogUpDate;
import c.liyueyun.mjmall.tv.tv.util.TimeUtils;


public class MallHomeActivity extends BaseActivity<MallHomePresenter, MallHomeView> implements MallHomeView {
    private String TAG = this.getClass().getSimpleName();
    private MallHomePagerAdapter mallHomePagerAdapter;
    private List<MallHomeMenuResult.MenuItem> menuItemList;

    private LinearLayout rlay_mallhome_main;
    private ImageView iv_mallhome_logo;
    private MallBarView mbv_mallhome_menu;
    private ViewPager vpage_mallhome_show;
    private DialogUpDate dialogUpDate;

    private boolean hideUpdateDialog = false;
    private boolean isUpdating = false;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_mall_home;
    }

    @Override
    protected MallHomePresenter initPresenter() {
        return new MallHomePresenter(this);
    }

    @Override
    protected void initViews() {
        MyApplication.getInstance().addActivity(this);
        rlay_mallhome_main = (LinearLayout) findViewById(R.id.rlay_mallhome_main);
        iv_mallhome_logo = (ImageView) findViewById(R.id.iv_mallhome_logo);
        mbv_mallhome_menu = (MallBarView) findViewById(R.id.mbv_mallhome_menu);
        vpage_mallhome_show = (ViewPager) findViewById(R.id.vpage_mallhome_show);

        mbv_mallhome_menu.setItemSelectListener(new MallBarView.OnItemSelectListener() {
            @Override
            public void onItemSelect(int pos) {
                if (vpage_mallhome_show.getAdapter().getCount() > pos)
                    vpage_mallhome_show.setCurrentItem(pos);
            }
        });
        mallHomePagerAdapter = new MallHomePagerAdapter(getSupportFragmentManager());
        vpage_mallhome_show.setAdapter(mallHomePagerAdapter);
        vpage_mallhome_show.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mbv_mallhome_menu.selectItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        showLoading("加载中...", true);
        presenter.initData(getIntent());

        if (dialogUpDate == null) {
            DialogUpDate.Builder builder = new DialogUpDate.Builder(mContext);
            dialogUpDate = builder.create();
        }
        String hasCheckVer = FamilyGroupUpDateTimeManage.getInstance().getToDayHasCheckUpdate(TimeUtils.timestampToSatndard02(System.currentTimeMillis()));
        if (!Tool.isEmpty(hasCheckVer)) return;
        UpdateManager.getInstance().checkAppUpdate(mContext);
        UpdateManager.getInstance().setOnUpDateStatusListener(new UpdateManager.onUpDateStatusListener() {
            @Override
            public void hasUpDate(UpdateResult updateResult) {
                if (updateResult != null) {
                    if (Tool.getVersionCode(mContext) < updateResult.getVersionCode()) {
                        if (dialogUpDate == null) {
                            DialogUpDate.Builder builder = new DialogUpDate.Builder(mContext);
                            dialogUpDate = builder.create();
                        }
                        dialogUpDate.setMessage("发现「每家优选」新版本", "立即更新", "下次再说");
                        dialogUpDate.discoverNewVersion();
                        dialogUpDate.show();
                    }
                }
            }

            @Override
            public void onProccess(int current, int total) {
                if (!hideUpdateDialog ) {
                    showLoading("当前进度：" + FormetFileSize(current) + "/" + FormetFileSize(total), true);
                    logUtil.d_2(TAG, "hideUpdateDialog=" + hideUpdateDialog);
                } else {
                    hideLoading();
                }
                if (current >= total) {
                    hideLoading();
                }
            }
        });

        dialogUpDate.setOnUpdateDialogListener(new DialogUpDate.onUpdateDialogListener() {
            @Override
            public void onButtonListener(String text) {
                if (text.equals("立即更新")) {
                    UpdateManager.getInstance().toUpdate();
                    isUpdating = true;
                    FamilyGroupUpDateTimeManage.getInstance().saveCheckUpdateTime(TimeUtils.timestampToSatndard02(System.currentTimeMillis()), "has");

                } else if (text.equals("下次再说")) {
                    isUpdating = false;
                    FamilyGroupUpDateTimeManage.getInstance().saveCheckUpdateTime(TimeUtils.timestampToSatndard02(System.currentTimeMillis()), "has");
                }
            }
        });

    }


    @Override
    public void loadingCanelListener() {
        //取消更新
        if (isUpdating) {
            hideUpdateDialog = true;
            UpdateManager.getInstance().cancelTask();
            isUpdating = false;
        }
    }

    @Override
    public void errorBtnListener() {
        finish();
    }

    @Override
    public void refresh(final MallHomeMenuResult newData) {
        hideLoading();
        Glide.with(mContext)
                .load(Tool.getYun2winImg(newData.getLogo()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_mallhome_logo);
        if (!Tool.isEmpty(newData.getBg())) {
            Glide.with(mContext)
                    .load(Tool.getYun2winImg(newData.getBg()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            rlay_mallhome_main.setBackground(resource);
                        }
                    });
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                menuItemList = newData.getMenus();
                mallHomePagerAdapter.notifyDataSetChanged();
                mbv_mallhome_menu.refreshData(menuItemList);
                mbv_mallhome_menu.initFocus();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    class MallHomePagerAdapter extends FragmentPagerAdapter {
        private List<HomeItemFragment> listfragment;

        public MallHomePagerAdapter(FragmentManager fm) {
            super(fm);
            this.listfragment = new ArrayList<>();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
        }

        @Override
        public Fragment getItem(int position) {
//        logUtil.d_3(TAG,"getItem:pos="+position);
            HomeItemFragment pageFragment = null;
            if (position < listfragment.size()) {
                pageFragment = listfragment.get(position);
            }
            if (pageFragment == null) {
                pageFragment = new HomeItemFragment();
                Bundle args = new Bundle();
                args.putString("menuId", menuItemList.get(position).getId());
                args.putString("menuName", menuItemList.get(position).getName());
                pageFragment.setArguments(args);
                listfragment.add(position, pageFragment);
            }
            return pageFragment;
        }

        @Override
        public int getCount() {
            return menuItemList == null ? 0 : menuItemList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    /**
     * 退出对话框
     */
    private void showExitDialog() {
        DialogChoice.Builder builder = new DialogChoice.Builder(mContext);
        builder.setTitle("即将退出「每家优选」");
        builder.setPositiveButton("残忍离开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.getInstance().exit();
            }
        });
        builder.setNegativeButton("留下看看", null);
        builder.create().show();
    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }


}
