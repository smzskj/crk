package com.smzskj.crk.net;

/**
 * Created by ztt on 2017/1/16.
 *
 * 方法名
 */

public class Method {



	/** webservice 登录 */
	public static String SERVICE_NAME_LOGIN = "loginBean";
	/** 获取数据库列表 */
	public static String GETDBLIST = "getDBList";
	/** 登录 */
	public static String LOGIN_LOGIN = "chk_login";
	/** 修改密码 */
	public static String XG_PSW = "xg_psw";
	/** 修改密码 */
	public static String GETVERSION = "getVersion";


	/** webservice 入库 */
	public static String SERVICE_NAME_RK = "rkBean";
	/** 获得单号 */
	public static String RK_GET_DH = "rk_get_dh";
	/** 获得库房 */
	public static String RK_GET_KFINFO = "rk_get_kfinfo";
	/** 获得商品信息 */
	public static String RK_GET_SPINFO = "rk_get_spinfo";
	/** 获得入库信息 */
	public static String RK_GET_RKINFO = "rk_get_rkinfo";
	/** 获得入库信息 */
	public static String RK_GET_RKINFO_DETAIL = "rk_get_rkinfo_detail";
	/** 入库 */
	public static String RK_RK = "rk_rk";
	/** 入库V2 */
	public static String RK_RK_V2 = "rk_rk_v2";
	/** 离线入库，上传入库 */
	public static String RK_RK_LX = "rk_rk_lx";

	/** webservice 出库 */
	public static String SERVICE_NAME_CK = "ckBean";
	/** 获得出库信息 */
	public static String CK_GET_CKINFO = "ck_get_ckinfo";
	/** 获得单号信息 */
	public static String CK_GET_DHINFO = "ck_get_dhinfo";
	/** 检查批次号 */
	public static String CK_CHK_PCH = "ck_chk_pch";
	/** 出库 */
	public static String CK_CK = "ck_ck";
	/** 离线出库 */
	public static String CK_CK_LX = "ck_ck_lx";

	/** 获得出库信息V2 */
	public static String CK_GET_DHINFO_V2 = "ck_get_dhinfo_v2";
	public static String CK_GET_DHINFO_V3 = "ck_get_dhinfo_v3";
	/** 出库 批次号查询 退货 V2 */
	public static String CK_CHK_PCH_TH_V2 = "ck_chk_pch_th_v2";
	/** 出库 批次号检查，选取商品 退货 V2 */
	public static String CK_CHK_PCH_TH_F_V2 = "ck_chk_pch_th_f_v2";
	/** 出库 批次号查询 正常 V2 */
	public static String CK_CHK_PCH_ZC_V2 = "ck_chk_pch_zc_v2";
	/** 出库 v2 */
	public static String CK_CK_V2 = "ck_ck_v2";





	/** webservice 盘点 */
	public static String SERVICE_NAME_PD = "pdBean";
	/** 检查盘点信息 */
	public static String PD_CHK_INFO = "pd_chk_info";
	/** 重置盘点信息 */
	public static String PD_CZ_INFO = "pd_cz_info";
	public static String PD_CZ_INFO_KFC = "pd_cz_info_kfc";
	/** 获得商品信息 */
	public static String PD_GET_SPINFO = "pd_get_spinfo";
	/** 盘点 */
	public static String PD_PD = "pd_pd";
	/** 盘点-添加盘点信息 */
	public static String PD_PD_ADD = "pd_pd_add";
	/** 盘点-添加盘点信息 */
	public static String PD_GET_PDINFO = "pd_get_pdinfo";
	/** 盘点-离线盘点上传 */
	public static String PD_PD_LX = "pd_pd_lx";
	/** 盘点-离线商品列表 */
	public static String PD_LX_SPLB = "pd_lx_splb";
}
