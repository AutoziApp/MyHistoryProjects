package com.mapuni.mobileenvironment.record.itemfactory;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.mapuni.mobileenvironment.record.AssemblyRecyclerAdapter;

/**
 * ItemFactory负责匹配数据和创建Item
 *
 * @param <ITEM> 指定Item类型，防止createAssemblyItem()方法返回错误的类型
 */
public abstract class AssemblyRecyclerItemFactory<ITEM extends AssemblyRecyclerItem> {
    private int itemType;
    private int spanSize = 1;
    private AssemblyRecyclerAdapter adapter;
    private boolean fullSpanInStaggeredGrid;

    /**
     * 获取Item类型
     */
    @SuppressWarnings("WeakerAccess")
    public int getItemType() {
        return itemType;
    }

    /**
     * 设置Item类型，此方法由Adapter调用
     */
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    /**
     * 获取Adapter
     */
    public AssemblyRecyclerAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置Adapter，此方法由Adapter调用
     */
    public void setAdapter(AssemblyRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 获取在GridLayoutManager里所占的列数
     */
    @SuppressWarnings("WeakerAccess")
    public int getSpanSize() {
        return spanSize;
    }

    /**
     * 设置在GridLayoutManager里所占的列数，不能小于1
     */
    @SuppressWarnings("WeakerAccess")
    public AssemblyRecyclerItemFactory<ITEM> setSpanSize(int spanSize) {
        if (spanSize > 0) {
            this.spanSize = spanSize;
        }
        return this;
    }

    /**
     * 在GridLayoutManager里占满一行
     *
     * @param recyclerView 需要从RecyclerView中取出GridLayoutManager在取出SpanCount
     */
    @SuppressWarnings("unused")
    public AssemblyRecyclerItemFactory<ITEM> fullSpan(RecyclerView recyclerView) {
        setSpanSize(1);
        fullSpanInStaggeredGrid = false;

        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                setSpanSize(gridLayoutManager.getSpanCount());
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                fullSpanInStaggeredGrid = true;
            }
        }
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public ITEM dispatchCreateAssemblyItem(ViewGroup parent) {
        ITEM item = createAssemblyItem(parent);
        item.onChangeListener(adapter);
        if (fullSpanInStaggeredGrid) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) item.getItemView().getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutParams = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                staggeredGridLayoutParams.setFullSpan(true);
                item.getItemView().setLayoutParams(layoutParams);
            }
        }
        return item;
    }

    /**
     * 匹配数据
     *
     * @param data 待匹配的数据，通常是使用instanceof关键字匹配类型
     * @return 如果返回true，Adapter将会使用此ItemFactory来处理当前这条数据
     */
    public abstract boolean isTarget(Object data);

    /**
     * 创建Item
     */
    public abstract ITEM createAssemblyItem(ViewGroup parent);
}
