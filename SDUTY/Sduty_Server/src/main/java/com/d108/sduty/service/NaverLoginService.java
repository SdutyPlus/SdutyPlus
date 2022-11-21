package com.d108.sduty.service;

import java.util.Map;

public interface NaverLoginService {
	Map<String, Object> getUserInfo(String token);
}
