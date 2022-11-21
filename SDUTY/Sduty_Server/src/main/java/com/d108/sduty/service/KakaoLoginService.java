package com.d108.sduty.service;

import java.util.Map;

public interface KakaoLoginService {
	Map<String, Object> getUserInfo(String access_token);
}
