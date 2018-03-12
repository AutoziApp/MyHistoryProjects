package com.jy.environment.model;

//"index_cy": "炎热",
//"index_cy_xs": "天气炎热，建议着短衫、短裙、短裤、薄型t恤衫等清凉夏季服装。",
//"index_uv": "弱",
//"index_uv_xs": "外出请采取带防护帽和太阳镜等防护措施",
//"index_tr": "较适宜",
//"index_tr_xs": "天气不错，可以出去旅游一下的",
//"index_co": "较不舒适",
//"index_co_xs": "这样的天气会让人感到很不舒适，尽量留在室内吧",
//"index_cl": "不适宜",
//"index_cl_xs": "今天的天气状况不适宜晨练",
//"index_ls": "不宜",
//"index_ls_xs": "今天的天气状况不适宜晾晒",
//"index_xc": "不宜",
//"index_xc_xs": "今天的天气状况不适宜洗车"
public class LifeItem {
	private String index_cy;
	private String index_cy_xs;
	private String index_uv;
	private String index_uv_xs;
	private String index_tr;
	private String index_tr_xs;
	private String index_co;
	private String index_co_xs;
	private String index_cl;
	private String index_cl_xs;
	private String index_ls;
	private String index_ls_xs;
	private String index_xc;
	private String index_xc_xs;

	public String getIndex_cy() {
		return index_cy;
	}

	public void setIndex_cy(String index_cy) {
		this.index_cy = index_cy;
	}

	public String getIndex_cy_xs() {
		return index_cy_xs;
	}

	public void setIndex_cy_xs(String index_cy_xs) {
		this.index_cy_xs = index_cy_xs;
	}

	public String getIndex_uv() {
		return index_uv;
	}

	public void setIndex_uv(String index_uv) {
		this.index_uv = index_uv;
	}

	public String getIndex_uv_xs() {
		return index_uv_xs;
	}

	public void setIndex_uv_xs(String index_uv_xs) {
		this.index_uv_xs = index_uv_xs;
	}

	public String getIndex_tr() {
		return index_tr;
	}

	public void setIndex_tr(String index_tr) {
		this.index_tr = index_tr;
	}

	public String getIndex_tr_xs() {
		return index_tr_xs;
	}

	public void setIndex_tr_xs(String index_tr_xs) {
		this.index_tr_xs = index_tr_xs;
	}

	public String getIndex_co() {
		return index_co;
	}

	public void setIndex_co(String index_co) {
		this.index_co = index_co;
	}

	public String getIndex_co_xs() {
		return index_co_xs;
	}

	public void setIndex_co_xs(String index_co_xs) {
		this.index_co_xs = index_co_xs;
	}

	public String getIndex_cl() {
		return index_cl;
	}

	public void setIndex_cl(String index_cl) {
		this.index_cl = index_cl;
	}

	public String getIndex_cl_xs() {
		return index_cl_xs;
	}

	public void setIndex_cl_xs(String index_cl_xs) {
		this.index_cl_xs = index_cl_xs;
	}

	public String getIndex_ls() {
		return index_ls;
	}

	public void setIndex_ls(String index_ls) {
		this.index_ls = index_ls;
	}

	public String getIndex_ls_xs() {
		return index_ls_xs;
	}

	public void setIndex_ls_xs(String index_ls_xs) {
		this.index_ls_xs = index_ls_xs;
	}

	public String getIndex_xc() {
		return index_xc;
	}

	public void setIndex_xc(String index_xc) {
		this.index_xc = index_xc;
	}

	public String getIndex_xc_xs() {
		return index_xc_xs;
	}

	public void setIndex_xc_xs(String index_xc_xs) {
		this.index_xc_xs = index_xc_xs;
	}

	@Override
	public String toString() {
		return "LifeItem [index_cy=" + index_cy + ", index_cy_xs="
				+ index_cy_xs + ", index_uv=" + index_uv + ", index_uv_xs="
				+ index_uv_xs + ", index_tr=" + index_tr + ", index_tr_xs="
				+ index_tr_xs + ", index_co=" + index_co + ", index_co_xs="
				+ index_co_xs + ", index_cl=" + index_cl + ", index_cl_xs="
				+ index_cl_xs + ", index_ls=" + index_ls + ", index_ls_xs="
				+ index_ls_xs + ", index_xc=" + index_xc + ", index_xc_xs="
				+ index_xc_xs + "]";
	}

}
