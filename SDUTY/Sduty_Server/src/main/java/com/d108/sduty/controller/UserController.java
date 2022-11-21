package com.d108.sduty.controller;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d108.sduty.config.WebSecurityConfig;
import com.d108.sduty.dto.AuthInfo;
import com.d108.sduty.dto.User;
import com.d108.sduty.service.KakaoLoginService;
import com.d108.sduty.service.NaverLoginService;
import com.d108.sduty.service.UserService;
import com.d108.sduty.utils.AES256Util;
import com.d108.sduty.utils.TimeCompare;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "User")
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private KakaoLoginService kService;
	@Autowired
	private NaverLoginService nService;
	@Autowired
	private WebSecurityConfig security = new WebSecurityConfig();
	
	@Autowired
	private AES256Util aes256Util;
	
	@ApiOperation(value = "로그인 > id, pass 확인 > User 리턴", response = User.class)
	@PostMapping("")
	public ResponseEntity<?> selectUser(@RequestBody User user) throws Exception {
		Optional<User> maybeUser = userService.selectUserById(user.getId());
		if(maybeUser.isPresent()) {
			User selectedUser = maybeUser.get();
			//System.out.println(selectedUser);
			//암호화 - 복호화
			if(selectedUser.getPass().equals(user.getPass())){
				selectedUser.setPass("");
				return new ResponseEntity<User>(selectedUser, HttpStatus.OK);
			}
			//암호화 확인 주석
//			if(security.passwordEncoder().matches(user.getPass(), selectedUser.getPass())) {
//				selectedUser.setPass("");
//				return new ResponseEntity<User>(selectedUser, HttpStatus.OK);
//			}
			
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}

	@ApiOperation(value = "회원가입 > id 중복확인 > 200/401 리턴", response = HttpStatus.class)
	@GetMapping("/join/{id}")
	public ResponseEntity<?> isUsedId(@PathVariable String id) throws Exception {
		boolean result = userService.isUsedId(id);
		if(result) {			
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value = "회원가입 > User 리턴", response = HttpStatus.class)
	@PostMapping("/join")
	public ResponseEntity<?> insertUser(@RequestBody User user) throws Exception {
		user.setPass(security.passwordEncoder().encode(user.getPass()));
		User result = userService.insertUser(user);
		if(result != null) {
			result.setPass("");
			return new ResponseEntity<User>(result, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "카카오 로그인 : token > User 리턴", response = HttpStatus.class)
	@PostMapping("/kakao")
	public ResponseEntity<?> kakaoLogin(@RequestBody String token) throws Exception {
		Map<String, Object> userInfo = kService.getUserInfo(token);
		String email = userInfo.get("email").toString();		
		
		Optional<User> maybeUser = userService.selectUserById(email);
		if(maybeUser.isPresent()) {
			User selectedUser = maybeUser.get();
			return new ResponseEntity<User>(selectedUser, HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
	}

	@ApiOperation(value = "네이버 로그인 : token > User 리턴", response = HttpStatus.class)
	@PostMapping("/naver")
	public ResponseEntity<?> naverLogin(@RequestBody String token) throws Exception {
		Map<String, Object> userInfo = nService.getUserInfo(token);
		String email = userInfo.get("email").toString();	
		
		Optional<User> maybeUser = userService.selectUserById(email);
		if(maybeUser.isPresent()) {
			User selectedUser = maybeUser.get();
			return new ResponseEntity<User>(selectedUser, HttpStatus.OK);
		}else {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}		
	}

	@ApiOperation(value = "카카오 회원가입 : token > User 리턴", response = HttpStatus.class)
	@PostMapping("/kakao/join")
	public ResponseEntity<?> kakaoJoin(@RequestBody String token) throws Exception {
		Map<String, Object> userInfo = kService.getUserInfo(token);
		String email = userInfo.get("email").toString();
		String name = userInfo.get("name").toString();
		User user = new User();
		user.setId(email);
		user.setPass("");
		user.setName(name);
		user.setEmail(email);
		//User result = userService.insertUser(user);  // User 정보만 보내주고 /join으로 카카오, 네이버 둘 다 가입
		if(user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);	
	}

	@ApiOperation(value = "네이버 회원가입 : token > User 리턴", response = HttpStatus.class)
	@PostMapping("/naver/join")
	public ResponseEntity<?> naverJoin(@RequestBody String token) throws Exception {
		Map<String, Object> userInfo = nService.getUserInfo(token);
		String email = userInfo.get("email").toString();
		String name = userInfo.get("name").toString();
		User user = new User();
		user.setId(email);
		user.setPass("");
		user.setName(name);
		user.setEmail(email);
		//User result = userService.insertUser(user);
		if(user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);		
	}
	
	@ApiOperation(value = "회원정보 수정 > User/401 리턴", response = HttpStatus.class)
	@PutMapping("")
	public ResponseEntity<?> updateUserInfo(@RequestBody User user) throws Exception {
		User selectUser = userService.selectUser(user.getSeq()).get();

		// 비밀번호 변경 안했을 때 (FCM 토큰 update)
		if(!user.getPass().equals("")) {
			user.setPass(security.passwordEncoder().encode(user.getPass()));
		}else {
			user.setPass(selectUser.getPass());
		}
		
		if(userService.updateUser(user) != null)
			return new ResponseEntity<Void>(HttpStatus.OK);
		else
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value = "회원정보 조회 > User/401 리턴", response = HttpStatus.class)
	@GetMapping("/{seq}")
	public ResponseEntity<?> getUserInfo(@PathVariable int seq) throws Exception {
		Optional<User> maybeUser = userService.selectUser(seq);
		if(maybeUser.isPresent()) {
			User user = maybeUser.get();
			user.setFcmToken("");
			user.setPass("");
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else 
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value = "회원정보 탈퇴 > 200/401 리턴", response = HttpStatus.class)
	@DeleteMapping("/{seq}")
	public ResponseEntity<?> deleteUser(@PathVariable int seq) throws Exception {
		try {
			userService.deleteUser(seq);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<Void>(HttpStatus.OK); 
		
	}
	
	//TODO : 이름과 아이디 같이 받아야 됨
	@ApiOperation(value = "아이디 찾기 > String/401 리턴", response = HttpStatus.class)
	@GetMapping("/id/{name}/{tel}")
	public ResponseEntity<?> findIdByTel(@PathVariable String name, @PathVariable String tel) throws Exception {
		User user = userService.findId(name, tel);
		if(user!=null)
			return new ResponseEntity<String>(user.getId(), HttpStatus.OK);
		else
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	
	@ApiOperation(value = "비밀번호 변경 > 200/401 리턴", response = HttpStatus.class)
	@PutMapping("/pwd")
	public ResponseEntity<?> setPwdById(@RequestBody User user) throws Exception {
		//인증이 안되면 수정이 안되므로 거의 not null 확실		
		User selected_user = userService.selectUserById(user.getId()).get();
		//암호화
		selected_user.setPass(security.passwordEncoder().encode(user.getPass()));
		User result = userService.updatePassword(selected_user);
		if(result != null) {
			return new ResponseEntity<User>(result, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); 
	}

	
	@ApiOperation(value = "인증정보 저장 > 200/401 리턴", response = HttpStatus.class)
	@PostMapping("/auth")
	public ResponseEntity<?> authTest(@RequestBody AuthInfo authInfo) throws Exception {
		userService.deleteAuthInfo(authInfo);
		int result = userService.insertAuthInfo(authInfo);
		if(result > 0) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT); 
	}

	@ApiOperation(value = "인증정보 확인 > 200/401 리턴", response = HttpStatus.class)
	@PostMapping("/auth/check")
	public ResponseEntity<?> getAuthCode(@RequestBody AuthInfo authInfo) throws Exception {
		AuthInfo selectedCode = userService.selectAuthInfo(authInfo.getTel());
		System.out.println(selectedCode);
		System.out.println(new Date(System.currentTimeMillis()));
		if (selectedCode != null) {
			if (selectedCode.getCode().equals(authInfo.getCode())) { // 인증코드 비교
				if (TimeCompare.compare(selectedCode.getExpire())) { // 인증 만료시간 확인
					userService.deleteAuthInfo(authInfo);
					return new ResponseEntity<Void>(HttpStatus.OK); // 인증완료
				} else {
					return new ResponseEntity<Void>(HttpStatus.GONE); // 인증시간 만료
				}
			}
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); // 인증번호 불일치
	}
	
	@GetMapping("/info/{id}")
	public ResponseEntity<?> test(@PathVariable String id) throws Exception {
		User selected_user = userService.selectUserById(id).get();
		if(selected_user != null) {
			return new ResponseEntity<User>(selected_user, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); 
	}
	
	@GetMapping("/test/{id}/{pw}")
	public ResponseEntity<?> test(@PathVariable String id, @PathVariable String pw) throws Exception {
		//인증이 안되면 수정이 안되므로 거의 not null 확실		
		User selected_user = userService.selectUserById(id).get();
		System.out.println(selected_user);
		//암호화
		selected_user.setPass(security.passwordEncoder().encode(pw));
		selected_user.setTel(aes256Util.encrypt(selected_user.getTel()));
		User result = userService.updatePassword(selected_user);
		if(result != null) {
			return new ResponseEntity<User>(result, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); 
	}
	
}


