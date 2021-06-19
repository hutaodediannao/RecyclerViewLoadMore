package com.app.recyclerviewdemo;

import android.content.Context;
import android.view.View;

import com.app.recyclerviewdemo.base.BaseRecyclerAdapter;
import com.app.recyclerviewdemo.base.BaseViewHolder;

import java.util.List;

/**
 * 作者:胡涛
 * 日期:2021-6-19
 * 时间:15:40
 * 功能:adapter
 */
public class ModelAdapter extends BaseRecyclerAdapter<Model> {

    public ModelAdapter(List<Model> mList, Context mContext) {
        super(mList, mContext);
    }

    @Override
    public int getRecyclerItemViewType(int position) {
        return this.mList.get(position).getViewType();
    }

    @Override
    protected int getLayout(int viewType) {
        switch (viewType) {
            case HEADER_TYPE:
                return R.layout.item_header_lay;
            case NORMAL_TYPE:
                return R.layout.item_normal_lay;
            case FOOTER_TYPE:
                return R.layout.item_footer_lay;
            default:
                return R.layout.item_footer_lay;
        }
    }

    @Override
    protected void bindHolder(BaseViewHolder holder, final int position, final Model model) {
        int viewType = model.getViewType();
        switch (viewType) {
            case HEADER_TYPE:
                //此处设置头部需要的展示互动
                holder.setText(model.getContent(), R.id.tv);
                break;
            case NORMAL_TYPE:
                //普通内容
                holder.setText(model.getContent(), R.id.tv);
                break;
            case FOOTER_TYPE:
                //底部内容修改(加载后者显示无更多数据)
                holder.setText(model.getContent(), R.id.tv)
                        .setVisible(model.getContent().equals("加载更多中..."), R.id.progressbar);
                break;
            default:
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, model);
                }
            }
        });
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T>{
        void onItemClick(int position, T t);
    }
}
