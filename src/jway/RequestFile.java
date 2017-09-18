package com.jway;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface RequestFile extends IEmptiable {
	public RequestFile setRequest(HttpServletRequest request);
	public Map<String, File> get(String... keys);
}
