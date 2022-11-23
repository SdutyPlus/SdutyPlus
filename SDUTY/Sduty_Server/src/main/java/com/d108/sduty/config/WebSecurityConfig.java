package com.d108.sduty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	//encoding
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();//72bytes char(60) 권장
		//return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
		.csrf().disable()//csrf
		.formLogin().disable()//기본 로그인 삭제
		.headers().frameOptions().disable();
//		http
//		.cors().disable()//cors = 같은 프로토콜 https, 같은 도메인, 같은 포트에서 온 요청만 허용
//		.csrf().disable()//csrf 검사 제거 => 옥션 해킹 사고 => 악성 코드를 막자
//		.formLogin().disable()//기본 로그인 삭제
//		.headers().frameOptions().disable();
	}
	
//	@Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/v2/api-docs",
//                "/swagger-resources",
//                "/swagger-resources/**",
//                "/configuration/ui",
//                "/configuration/security",
//                "/swagger-ui.html",
//                "/webjars/**",
//                "/swagger/**");
//    }
}
