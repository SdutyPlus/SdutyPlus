package com.d205.sdutyplus.domain.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {

    private String accessToken;

    private String refreshToken;
}
