package com.d108.sduty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d108.sduty.dto.AuthInfo;
import com.d108.sduty.dto.User;
import com.d108.sduty.repo.TestRepo;


@Service
public class TestServiceImpl implements TestService {

	@Autowired
	TestRepo testRepo;

	@Override
	public int insertUser(User user) {
		return testRepo.insertUser(user);
	}

	@Override
	public User selectUser(String id) {
		return testRepo.selectUser(id);
	}

	@Override
	public int isUsedId(String id) {
		return testRepo.isUsedId(id);
	}

	@Override
	public int insertAuthInfo(AuthInfo authInfo) {
		return testRepo.insertAuthInfo(authInfo);
	}

	@Override
	public int updateAuthInfo(AuthInfo authInfo) {
		return testRepo.updateAuthInfo(authInfo);
	}

	@Override
	public AuthInfo selectAuthInfo(String phone) {
		return testRepo.selectAuthInfo(phone);
	}

	@Override
	public int deleteAuthInfo(AuthInfo authInfo) {
		return testRepo.deleteAuthInfo(authInfo);
	}


}
