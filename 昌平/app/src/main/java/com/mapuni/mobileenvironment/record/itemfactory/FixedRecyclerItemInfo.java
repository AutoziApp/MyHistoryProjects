package com.mapuni.mobileenvironment.record.itemfactory;


import com.mapuni.mobileenvironment.record.AssemblyRecyclerAdapter;

/**
 * AssemblyRecyclerAdapter专用的固定位置Item管理器
 */
public class FixedRecyclerItemInfo {
    private AssemblyRecyclerItemFactory itemFactory;
    private Object data;
    private boolean enabled;
    private int position;
    private boolean header;

    @SuppressWarnings("WeakerAccess")
    public FixedRecyclerItemInfo(AssemblyRecyclerItemFactory itemFactory, Object data, boolean header) {
        this.data = data;
        this.itemFactory = itemFactory;
        this.enabled = true;
        this.header = header;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;

        AssemblyRecyclerAdapter adapter = itemFactory.getAdapter();
        if (adapter.isNotifyOnChange()) {
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("unused")
    public AssemblyRecyclerItemFactory getItemFactory() {
        return itemFactory;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @SuppressWarnings("unused")
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        enableChanged();
    }
    protected void enableChanged() {
        if (header) {
            itemFactory.getAdapter().headerEnabledChanged(this);
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @SuppressWarnings("unused")
    public boolean isHeader() {
        return header;
    }
}
