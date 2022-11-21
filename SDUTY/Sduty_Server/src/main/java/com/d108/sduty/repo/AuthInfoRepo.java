package com.d108.sduty.repo;

import com.d108.sduty.dto.AuthInfo;

public interface AuthInfoRepo {
	
	public int insertAuthInfo(AuthInfo authInfo);
	
	public int updateAuthInfo(AuthInfo authInfo);
	
	public AuthInfo selectAuthInfo(String user_tel);
	
	public int deleteAuthInfo(AuthInfo authInfo);
}
