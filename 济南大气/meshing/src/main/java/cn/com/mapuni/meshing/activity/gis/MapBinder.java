package cn.com.mapuni.meshing.activity.gis;

public class MapBinder {
	private LocationService.MyBinder binder;
	private static MapBinder mapbinder;

	private MapBinder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static MapBinder getInstance() {
		return mapbinder == null ? mapbinder = new MapBinder() : mapbinder;
	}

	public LocationService.MyBinder getBinder() {
		return binder;
	}

	public void setBinder(LocationService.MyBinder binder) {
		this.binder = binder;
	}

}
