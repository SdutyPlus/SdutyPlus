package com.d108.sduty.service;


import java.util.Optional;

import com.d108.sduty.dto.AuthInfo;
import com.d108.sduty.dto.User;

public interface UserService {
	public User insertUser(User user) throws Exception;		
	public Optional<User> selectUserById(String id) throws Exception;
	public boolean isUsedId(String id) throws Exception;
	public User updateUser(User user) throws Exception;
	public Optional<User> selectUser(int seq) throws Exception;
	public void deleteUser(int seq) throws Exception;
	
	public User findId(String name, String tel) throws Exception;
	public Optional<User> selectByTel(String tel) throws Exception;
	public User updatePassword(User user) throws Exception;
	
	public int insertAuthInfo(AuthInfo authInfo) throws Exception;
	public int updateAuthInfo(AuthInfo authInfo) throws Exception;
	public AuthInfo selectAuthInfo(String tel) throws Exception;
	public void deleteAuthInfo(AuthInfo authInfo) throws Exception;
}