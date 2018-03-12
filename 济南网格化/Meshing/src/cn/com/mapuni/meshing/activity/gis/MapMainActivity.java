package cn.com.mapuni.meshing.activity.gis;

import com.mapuni.android.base.util.DisplayUitl;
import com.tianditu.android.maps.MapView;

import android.widget.FrameLayout;
import cn.com.mapuni.meshing.activity.db_activity.DbMainActivity;
import cn.com.mapuni.meshing.activity.gis.BaseMapActivityTDT.ViewCallback;
import cn.com.mapuni.meshing.activity.xc_activity.XcMainActivity;

public class MapMainActivity extends BaseMapActivityTDT implements ViewCallback {
	private FrameLayout view;
	private MapView mapView;
	public static MapMainActivity MAPACTIVITY;

	@Override
	public void onCreate(FrameLayout view, MapView mapView) {
		this.mapView = mapView;
		this.view = view;
		// changeView(0);
		// //////////////////////////////////////不具有巡检功能的角色屏蔽巡检页面
		String havePatrolRole = DisplayUitl.readPreferences(this, "lastuser", "havePatrolRole");
		String haveAdminRole = DisplayUitl.readPreferences(this, "lastuser", "haveAdminRole");
		String haveLiaisonRole = DisplayUitl.readPreferences(this, "lastuser", "haveLiaisonRole");
		String organization_code = DisplayUitl.readPreferences(this, "lastuser", "organization_code");
		if ((!"1".equals(havePatrolRole) && !"1".equals(haveAdminRole) && !"1".equals(haveLiaisonRole))
				|| (organization_code.length() == 10)) {
			changeView(0);
		} else {
			changeView(1);
		}
		// ////////////////////////////////////
		MAPACTIVITY = this;
	}

	public void changeView(int id) {
		try {
			switch (id) {
			case 0:
				mapView.removeAllOverlay();// 清空所有覆盖物
				new XcMainActivity(this, view, mapView).showView();
				break;
			case 1:
				mapView.removeAllOverlay();// 清空所有覆盖物
				mapView.removeView(DbMainActivity.mPopView);
				new DbMainActivity(this, view, mapView).showView();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
