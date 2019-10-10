package c.liyueyun.mjmall.tv.tv.ui.activity.mall.mallEvent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.ItemDecorationAdapterHelper;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyCallback;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyErrorMessage;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallCell;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallEventProdsResult;
import c.liyueyun.mjmall.tv.tv.ui.activity.mall.mallProd.MallProdActivity;
import c.liyueyun.mjmall.tv.tv.ui.adapter.ProdsRecyAdapter;


public class MallEventActivity extends AppCompatActivity {
    private Context mContext;
    private String eventId;
    private RelativeLayout rlay_mallevent_main;
    private RecyclerView frecy_mallevent_prods;
    private ProdsRecyAdapter prodsRecyAdapter;

    private static final int INIT_FOCUS = 10010;
    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case INIT_FOCUS:
                    if(frecy_mallevent_prods != null && frecy_mallevent_prods.getChildCount() > 0){
                        frecy_mallevent_prods.getChildAt(0).requestFocus();
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_event);

        MyApplication.getInstance().addActivity(this);
        mContext = this;
        eventId = getIntent().getStringExtra("eventId");

        rlay_mallevent_main = (RelativeLayout)findViewById(R.id.rlay_mallevent_main);
        frecy_mallevent_prods = (RecyclerView)findViewById(R.id.frecy_mallevent_prods);

        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        frecy_mallevent_prods.setLayoutManager(manager);
        frecy_mallevent_prods.addItemDecoration(new ItemDecorationAdapterHelper(mContext, 0, 40, false, 0));
        prodsRecyAdapter = new ProdsRecyAdapter(mContext, new ProdsRecyAdapter.OnRecyFragmentListener() {
            @Override
            public void onItemClickListener(MallCell clickCell) {
                //显示商品详情页
                Intent intent = new Intent(mContext,MallProdActivity.class);
                intent.putExtra("prodId",clickCell.getId());
                startActivity(intent);
            }
        });
        frecy_mallevent_prods.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    myHandler.sendEmptyMessage(INIT_FOCUS);
                }
            }
        });
        frecy_mallevent_prods.setAdapter(prodsRecyAdapter);
        MyApplication.getInstance().getmApi().getMallTemplate().getEventProds(eventId, new MyCallback<MallEventProdsResult>() {
            @Override
            public void onError(MyErrorMessage msg) {

            }

            @Override
            public void onSuccess(final MallEventProdsResult response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    prodsRecyAdapter.setNewData(response.getItems());
                    myHandler.sendEmptyMessageDelayed(INIT_FOCUS,500);
                    Glide.with(mContext)
                            .load(response.getBg())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new SimpleTarget<GlideDrawable>() {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                    rlay_mallevent_main.setBackground(resource);
                                }
                            });
                    }
                });
            }

            @Override
            public void onFinish() {

            }
        });
//        String extaaa = "{\"id\":\"event1\",\"bg\":\"http://tbm.8843shop.com/img/evt/ev_bg.jpg\",\"items\":[{\"row\":1,\"high\":400,\"cells\":[{\"id\":\"item1\",\"type\":\"prod\",\"img\":\"http://tbm.8843shop.com/img/evt/ev_11.jpg\",\"cols\":3,\"title\":\"亿健跑步机\",\"price\":559,\"oriPrice\":659},{\"id\":\"item1\",\"type\":\"prod\",\"img\":\"http://tbm.8843shop.com/img/evt/ev_12.jpg\",\"cols\":3,\"title\":\"喜临门床垫\",\"price\":241,\"oriPrice\":281},{\"id\":\"item1\",\"type\":\"prod\",\"img\":\"http://tbm.8843shop.com/img/evt/ev_13.jpg\",\"cols\":3,\"title\":\"精致茶组\",\"price\":241,\"oriPrice\":261},{\"id\":\"item1\",\"type\":\"prod\",\"img\":\"http://tbm.8843shop.com/img/evt/ev_14.jpg\",\"cols\":3,\"title\":\"家用健身器材\",\"price\":399,\"oriPrice\":599}]},{\"row\":2,\"high\":400,\"cells\":[{\"id\":\"item1\",\"type\":\"prod\",\"img\":\"http://tbm.8843shop.com/img/evt/ev_11.jpg\",\"cols\":3,\"title\":\"亿健跑步机\",\"price\":559,\"oriPrice\":659},{\"id\":\"item1\",\"type\":\"prod\",\"img\":\"http://tbm.8843shop.com/img/evt/ev_12.jpg\",\"cols\":3,\"title\":\"喜临门床垫\",\"price\":241,\"oriPrice\":281},{\"id\":\"item1\",\"type\":\"prod\",\"img\":\"http://tbm.8843shop.com/img/evt/ev_13.jpg\",\"cols\":3,\"title\":\"精致茶组\",\"price\":241,\"oriPrice\":261},{\"id\":\"item1\",\"type\":\"prod\",\"img\":\"http://tbm.8843shop.com/img/evt/ev_14.jpg\",\"cols\":3,\"title\":\"家用健身器材\",\"price\":399,\"oriPrice\":599}]}]}";
//        MallEventProdsResult result = MyApplication.getInstance().getmGson().fromJson(extaaa,MallEventProdsResult.class);
//        prodsRecyAdapter.setNewData(result.getItems());
//        if(frecy_mallevent_prods.getChildCount() > 0){
//            frecy_mallevent_prods.getChildAt(0).requestFocus();
//        }
//        Glide.with(mContext)
//                .load(result.getBg())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        rlay_mallevent_main.setBackground(resource);
//                    }
//                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }
}
