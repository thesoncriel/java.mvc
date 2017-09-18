package com.jway.coupon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jway.BaseController;

public class CouponController extends BaseController {

	private CouponModel model;

	public CouponController(HttpServletRequest req, HttpServletResponse res,
			HttpSession session) throws IOException, ClassCastException,
			Exception {
		super(req, res, session);
		model = new CouponModel(db);
	}

	public void usedCouponList_before() {
		List<String> ipList = new ArrayList<String>();
		ipList.add("211.43.189.211");
		ipList.add("211.43.189.226");
		ipList.add("1.233.82.156");
		ipList.add("127.0.0.1");
		ipList.add("61.105.116.38");
		ipList.add("61.105.116.30");

		if (ipList.contains(request.getRemoteAddr())) {
			withoutAuth();
		}
	}

	public void usedCouponList() {

		json = new JSONResponse().setCouponList(model
				.selectUsedCouponList(param));

		model.empty();
	}

	public class JSONResponse extends com.jway.JSONResponse {
		private Object couponList;

		public Object getCouponList() {
			return couponList;

		}

		public JSONResponse setCouponList(Object couponList) {
			this.couponList = couponList;
			return this;
		}
	}

}
