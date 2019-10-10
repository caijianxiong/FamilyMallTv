package c.liyueyun.mjmall.tv.tv.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;


import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallCell;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallRowItem;
import c.liyueyun.mjmall.tv.tv.manager.ImageHandler;

import static android.view.Gravity.CENTER_VERTICAL;

public class ProdsRecyAdapter extends RecyclerView.Adapter<ProdsRecyAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private List<MallRowItem> allDataList;
    private OnRecyFragmentListener fragmentListener;

    public ProdsRecyAdapter(Context context, OnRecyFragmentListener listener) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        allDataList = new ArrayList<>();
        fragmentListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LinearLayout mainLayout = new LinearLayout(mContext);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setGravity(CENTER_VERTICAL);
        mainLayout.setPadding(Tool.getDimenWidth(mContext,42),0,Tool.getDimenWidth(mContext,42), 0);
        viewGroup.addView(mainLayout);
        return new ViewHolder(mainLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MallRowItem mallMenuItem = allDataList.get(position);
        holder.itemView.getLayoutParams().height = Tool.getDimenhight(mContext,mallMenuItem.getHigh());
        for(final MallCell cell:mallMenuItem.getCells()){
            View itemView = inflater.inflate(R.layout.mallfragment_adapter_item,null);
            ((LinearLayout)holder.itemView).addView(itemView,new ViewGroup.LayoutParams(Tool.getDimenWidth(mContext,cell.getCols()*153),ViewGroup.LayoutParams.MATCH_PARENT));
            if(cell.getType().equals("event") || cell.getType().equals("item")){
                itemView.findViewById(R.id.rlay_mallfragmentitem_prod).setVisibility(View.GONE);
                itemView.findViewById(R.id.iv_mallfragmentitem_img).setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(ImageHandler.getFit360(Tool.getYun2winImg(cell.getImg())))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into((ImageView)itemView.findViewById(R.id.iv_mallfragmentitem_img));
            }else{
                itemView.findViewById(R.id.iv_mallfragmentitem_img).setVisibility(View.GONE);
                itemView.findViewById(R.id.rlay_mallfragmentitem_prod).setVisibility(View.VISIBLE);
                ((TextView)itemView.findViewById(R.id.tv_mallfragmentitem_name)).setText(cell.getTitle());
                ((TextView)itemView.findViewById(R.id.tv_mallfragmentitem_prise)).setText("¥"+cell.getPrice());
                ((TextView)itemView.findViewById(R.id.tv_mallfragmentitem_rebate)).setText("原价"+cell.getOriPrice());
                ((TextView)itemView.findViewById(R.id.tv_mallfragmentitem_rebate)).getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG); //加上这个属性，字体更清晰一些
                Glide.with(mContext)
                        .load(ImageHandler.getFit360(Tool.getYun2winImg(cell.getImg())))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into((ImageView)itemView.findViewById(R.id.iv_mallfragmentitem_prodimg));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fragmentListener != null){
                        fragmentListener.onItemClickListener(cell);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return allDataList.size();
    }

    /**
     * 设置新数据
     */
    public void setNewData(List<MallRowItem> newData){
        allDataList.clear();
        if(newData != null)
            allDataList.addAll(newData);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //在布局中找到所含有的UI组件
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnRecyFragmentListener {
        void onItemClickListener(MallCell clickCell);
    }
}
