package com.jway.coupon;

import java.util.List;
import java.util.Map;

import com.jway.BaseModel;
import com.jway.IDBExecute;

/**
 * 쿠폰관련업무
 * 
 * @author sslee
 *
 */
public class CouponModel extends BaseModel {

	public CouponModel(IDBExecute db) {
		super(db);
	}

	/*******************************************************************************
	 * 쿠폰 조회 및 설정부
	 *******************************************************************************/
	/**
	 * 
	 * <pre>
	 * 요청범위안의 쿠폰사용내역을 돌려준다.
	 * </pre>
	 * 
	 * @return
	 */
	public List<Map<String, String>> selectUsedCouponList(
			Map<String, String> param) {
		this.keyLowerCase = false;

		StringBuffer query = new StringBuffer();
		query.append(/*query*/);

		return select(query.toString(), param, "DT_START", "DT_END");
	}
}
