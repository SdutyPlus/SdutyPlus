package com.d108.sduty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d108.sduty.config.WebSecurityConfig;
import com.d108.sduty.dto.AuthInfo;
import com.d108.sduty.dto.User;
import com.d108.sduty.repo.AuthInfoRepo;
import com.d108.sduty.repo.UserRepo;
import com.d108.sduty.utils.AES256Util;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	AuthInfoRepo authInfoRepo;
	@Autowired
	private AES256Util aes256Util;

	@Transactional
	@Override
	public User insertUser(User user) throws Exception {
		user.setTel(aes256Util.encrypt(user.getTel()));
		return userRepo.save(user);
	}

	@Override
	public Optional<User> selectUserById(String id) throws Exception {
		Optional<User> userOp = userRepo.findByid(id);
		if(userOp.isPresent()) {
			User user = userOp.get();
			user.setTel(aes256Util.decrypt(user.getTel()));
		}
		return userOp;
	}

	@Override
	public boolean isUsedId(String id) throws Exception {
		return userRepo.existsByid(id);
	}
	
	@Transactional
	@Override
	public User updateUser(User user) throws Exception {
		user.setTel(aes256Util.encrypt(user.getTel()));
		return userRepo.save(user);
	}
	
	@Override
	public Optional<User> selectUser(int seq) throws Exception {
		Optional<User> userOp = userRepo.findById(seq);
		if(userOp.isPresent()) {
			User user = userOp.get();
			user.setTel(aes256Util.decrypt(user.getTel()));
		}
		return userOp;
	}
	
	@Override
	public User findId(String name, String tel) throws Exception {
		List<User> userList = userRepo.findByName(name);
		for(User user : userList) {
			user.setTel(aes256Util.decrypt(user.getTel()));
			if(user.getTel().equals(tel)) {
				return user;
			}
		}
		return null;
	}
	
	@Override
	public Optional<User> selectByTel(String tel) throws Exception {
		return userRepo.findByTel(tel);
	}

	@Transactional
	@Override
	public User updatePassword(User user) throws Exception {
		if(user.getTel()!=null) {
			user.setTel(aes256Util.decrypt(user.getTel()));
		}
		return userRepo.save(user);
	}
	
	@Transactional
	@Override
	public void deleteUser(int seq) throws Exception {
		userRepo.deleteById(seq);
	}

	@Override
	public int insertAuthInfo(AuthInfo authInfo) throws Exception {
		return authInfoRepo.insertAuthInfo(authInfo);
	}

	@Transactional
	@Override
	public int updateAuthInfo(AuthInfo authInfo) throws Exception {
		return authInfoRepo.updateAuthInfo(authInfo);
	}

	@Override
	public AuthInfo selectAuthInfo(String tel) throws Exception {
		return authInfoRepo.selectAuthInfo(tel);
	}

	@Transactional
	@Override
	public void deleteAuthInfo(AuthInfo authInfo) throws Exception {
		authInfoRepo.deleteAuthInfo(authInfo);
	}

}
