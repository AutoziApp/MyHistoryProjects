package cn.com.mapuni.gis.meshingtotal.model;

/**
 * Created by lenovo on 2017/4/19.
 */

public class GridRecordItem {
    private String gridName;
    private String gridPeople;
    public GridRecordItem(String gridName,String gridPeople ){
        this.gridName=gridName;
        this.gridPeople=gridPeople;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public String getGridPeople() {
        return gridPeople;
    }

    public void setGridPeople(String gridPeople) {
        this.gridPeople = gridPeople;
    }
}
