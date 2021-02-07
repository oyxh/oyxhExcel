package com.oyxhExcel;

import java.util.List;

public class DRDAFrame {
	private DRDADDM ddm;
	private List<DRDAParameter> parameters;
	public DRDADDM getDdm() {
		return ddm;
	}
	public void setDdm(DRDADDM ddm) {
		this.ddm = ddm;
	}
	public List<DRDAParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<DRDAParameter> parameters) {
		this.parameters = parameters;
	}

}
