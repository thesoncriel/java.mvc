package com.jway;

import java.io.OutputStream;
import java.util.*;

public abstract class BaseView extends Util implements IEmptiable {
	public abstract BaseView setParam(Map<String, String> param);
	public abstract BaseView setData(List<Map<String, String>> data);
	public abstract boolean render(OutputStream out);
}
