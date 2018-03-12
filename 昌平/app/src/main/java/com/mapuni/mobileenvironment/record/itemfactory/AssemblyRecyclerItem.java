package com.mapuni.mobileenvironment.record.itemfactory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Item负责创建、初始化View并设置数据
 * @param <DATA> 指定需要的数据的类型
 */
public abstract class AssemblyRecyclerItem<DATA> extends RecyclerView.ViewHolder {
    private DATA data;

    public AssemblyRecyclerItem(int itemLayoutId, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    @SuppressWarnings("WeakerAccess")
    public AssemblyRecyclerItem(View itemView) {
        super(itemView);

        onFindViews();
        onConfigViews(itemView.getContext());
    }
    public  interface DataChangeListener{
        void onDataChange(List data);
    };
    /**
     * 设置数据，这个方法由Adapter调用
     *
     * @param position 位置
     * @param data     数据
     */
    public void setData(int position, DATA data) {
        this.data = data;
        onSetData(position, data);
    }

    /**
     * 根据id查找View
     */
    public View findViewById(int id) {
        return itemView.findViewById(id);
    }

    /**
     * 根据tag查找View
     */
    @SuppressWarnings("unused")
    public View findViewWithTag(Object tag) {
        return itemView.findViewWithTag(tag);
    }

    /**
     * 专门用来find view，只会执行一次
     */
    protected abstract void onFindViews();

    /**
     * 专门用来配置View，你可在在这里设置View的样式以及尺寸，只会执行一次
     */
    protected abstract void onConfigViews(Context context);
    public abstract void onChangeListener(DataChangeListener listener);
    /**
     * 设置数据
     *
     * @param position 位置
     * @param data     数据
     */
    protected abstract void onSetData(int position, DATA data);

    /**
     * 获取当前Item的View
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public final View getItemView() {
        return itemView;
    }

    /**
     * 获取当前Item的数据
     */
    public DATA getData() {
        return data;
    }
}