package com.smzskj.crk.offline.bean;

import com.smzskj.crk.offline.db.OfflinePdHelper;

/**
 * Created by ztt on 2017/2/25.
 */

public class OfflineBean {

	private long id;
	private String sjk;
	private String ry;
	private String kf;
	private String rq;
	private String sph;
	private String pch;
	private String zt;
	private String lrrq;
	private String sjk_dm;
	private String ry_dm;
	private String kf_dm;

	public OfflineBean() {
	}

	public OfflineBean(long id, String sjk, String ry, String kf, String rq, String sph, String
			pch, String zt, String lrrq, String sjk_dm, String ry_dm, String kf_dm) {
		this.id = id;
		this.sjk = sjk;
		this.ry = ry;
		this.kf = kf;
		this.rq = rq;
		this.sph = sph;
		this.pch = pch;
		this.zt = zt;
		this.lrrq = lrrq;
		this.sjk_dm = sjk_dm;
		this.ry_dm = ry_dm;
		this.kf_dm = kf_dm;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSjk() {
		return sjk;
	}

	public void setSjk(String sjk) {
		this.sjk = sjk;
	}

	public String getRy() {
		return ry;
	}

	public void setRy(String ry) {
		this.ry = ry;
	}

	public String getKf() {
		return kf;
	}

	public void setKf(String kf) {
		this.kf = kf;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getSph() {
		return sph;
	}

	public void setSph(String sph) {
		this.sph = sph;
	}

	public String getPch() {
		return pch;
	}

	public void setPch(String pch) {
		this.pch = pch;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getLrrq() {
		return lrrq;
	}

	public void setLrrq(String lrrq) {
		this.lrrq = lrrq;
	}

	public String getSjk_dm() {
		return sjk_dm;
	}

	public void setSjk_dm(String sjk_dm) {
		this.sjk_dm = sjk_dm;
	}

	public String getRy_dm() {
		return ry_dm;
	}

	public void setRy_dm(String ry_dm) {
		this.ry_dm = ry_dm;
	}

	public String getKf_dm() {
		return kf_dm;
	}

	public void setKf_dm(String kf_dm) {
		this.kf_dm = kf_dm;
	}

	public String getZtStr(){
		String ztStr = "";
		if (OfflinePdHelper.SUCCESS.equals(zt)) {
			ztStr = "成功";
		} else if (OfflinePdHelper.RQ_NULL.equals(zt)) {
			ztStr = "日期不能为空";
		} else if (OfflinePdHelper.KF_NULL.equals(zt)) {
			ztStr = "库房名称不能为空";
		} else if (OfflinePdHelper.RY_NULL.equals(zt)) {
			ztStr = "人员不能为空";
		} else if (OfflinePdHelper.SPH_NULL.equals(zt)) {
			ztStr = "商品号不能为空";
		} else if (OfflinePdHelper.PCH_NULL.equals(zt)) {
			ztStr = "批次号不能为空";
		} else if (OfflinePdHelper.SP_NULL.equals(zt)) {
			ztStr = "离线盘点-商品号或条形码查询商品不存在!";
		}  else if (OfflinePdHelper.NOT_UP.equals(zt)) {
			ztStr = "未上传";
		} else {
			ztStr = "未知状态";
		}
		return ztStr;
	}
}
