package com.mapuni.android.base.business;

public class ContactBean {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((QYDM == null) ? 0 : QYDM.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactBean other = (ContactBean) obj;
		if (QYDM == null) {
			if (other.QYDM != null)
				return false;
		} else if (!QYDM.equals(other.QYDM))
			return false;
		return true;
	}

	private int selected = 0;

	private String Guid;

	public String getGuid() {
		return Guid;
	}

	public void setGuid(String guid) {
		Guid = guid;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	private String QYDM;
	private String QYMC;

	public String getQYDM() {
		return QYDM;
	}

	public void setQYDM(String qYDM) {
		QYDM = qYDM;
	}

	public String getQYMC() {
		return QYMC;
	}

	public void setQYMC(String qYMC) {
		QYMC = qYMC;
	}

}
