package com.d205.sdutyplus.domain.jwt.support;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(JwtProperties.JWT_ACCESS_NAME);
        //prefix확인
        if(jwt == null){//|| !jwt.startsWith(JwtProperties.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        //prefix제거X => prefix안함
        String token = jwt;//.replace(JwtProperties.TOKEN_PREFIX, "");
        if(JwtUtils.validateToken(token)) {//인증안되면 exception발생
            System.out.println("인증완료");
            String userSeq = JwtUtils.getUserSeq(token);
            //뽑아낸 정보를 인증 정보에 저장
            Authentication auth = new UsernamePasswordAuthenticationToken(Long.parseLong(userSeq), null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

}
