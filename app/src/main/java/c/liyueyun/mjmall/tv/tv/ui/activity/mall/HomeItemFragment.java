package c.liyueyun.mjmall.tv.tv.ui.activity.mall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import c.liyueyun.mjmall.tv.base.base.ItemDecorationAdapterHelper;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyCallback;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyErrorMessage;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallCell;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallRowItem;
import c.liyueyun.mjmall.tv.tv.ui.activity.mall.mallEvent.MallEventActivity;
import c.liyueyun.mjmall.tv.tv.ui.activity.mall.mallProd.MallProdActivity;
import c.liyueyun.mjmall.tv.tv.ui.adapter.ProdsRecyAdapter;

/**
 * Created by songjie on 2019-03-27
 */
public class HomeItemFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private ProdsRecyAdapter prodsRecyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String menuId = getArguments().getString("menuId");
        String menuName = getArguments().getString("menuName");

        final RecyclerView recyclerView = new RecyclerView(mContext);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new ItemDecorationAdapterHelper(mContext, 0, Tool.getDimenhight(mContext,40), false, 0));
        prodsRecyAdapter = new ProdsRecyAdapter(mContext, new ProdsRecyAdapter.OnRecyFragmentListener() {
            @Override
            public void onItemClickListener(MallCell clickCell) {
                //显示商品详情页
                if(clickCell.getType().equals("event")){
                    Intent intent = new Intent(mContext,MallEventActivity.class);
                    intent.putExtra("eventId",clickCell.getId());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(mContext,MallProdActivity.class);
                    intent.putExtra("prodId",clickCell.getId());
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(prodsRecyAdapter);
        MyApplication.getInstance().getmApi().getMallTemplate().getMenuItem(menuId, new MyCallback<List<MallRowItem>>() {
            @Override
            public void onError(MyErrorMessage msg) {
                if(getActivity() instanceof MallHomeActivity){
                    ((MallHomeActivity)getActivity()).showErrorDialog(Tool.getApiErrorMsg(msg.getMessage()),false);
                }
            }

            @Override
            public void onSuccess(List<MallRowItem> response) {
                prodsRecyAdapter.setNewData(response);
            }

            @Override
            public void onFinish() {

            }
        });
        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && recyclerView.getAdapter().getItemCount()>0){
                    recyclerView.getChildAt(0).requestFocus();
                }
            }
        });
        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
