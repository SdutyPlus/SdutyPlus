package com.d205.sdutyplus.domain.jwt.support;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter{

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (ExpiredJwtException e){
            log.error("jwt expired exception handler filter");
            response.setContentType("application/json;charset=UTF-8");
            String result = objectMapper.writeValueAsString(ResponseEntity.ok(ErrorResponseDto.of(ErrorCode.AUTHENTICATION_EXPIRED)));
            response.getWriter().write(result);
        } catch (JwtException e2){
            log.error("JWT exception handler filter");
            response.setContentType("application/json;charset=utf-8");
            String result = objectMapper.writeValueAsString(ResponseEntity.ok(ErrorResponseDto.of(ErrorCode.AUTHENTICATION_FAIL)));
            log.error(result);
            response.getWriter().write(result);
        }
    }

}