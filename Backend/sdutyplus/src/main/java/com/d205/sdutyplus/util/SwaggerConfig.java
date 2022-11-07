package com.d205.sdutyplus.util;

import com.d205.sdutyplus.domain.jwt.support.JwtProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//http://localhost:8090/swagger-ui/index.html#/
@OpenAPIDefinition(
        info = @Info(title = "API 명세서",
                description = "API 명세서 테스트 입니다.",
                version = "v1"))
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public Docket api() {
        final ApiInfo apiInfo = new ApiInfoBuilder()
                .title("구미 자율2반 5팀")
                .description("<h3>김남희 김정윤 배시현 배한용 서재형 편예린</h3>")
                .contact(new Contact("D205", "https://edu.ssafy.com", "ssafy@ssafy.com"))
                .license("MIT License")
                .version("1.0")
                .build();

        Server serverLocal = new Server("local", "http://localhost:8090", "for local usages", Collections.emptyList(), Collections.emptyList());
        Server testServer = new Server("test", "https://d205.kro.kr/api", "for testing", Collections.emptyList(), Collections.emptyList());
        //Server testServer2 = new Server("test2", "http://k7d205.p.ssafy.io:8081", "for testing", Collections.emptyList(), Collections.emptyList());

        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .servers(serverLocal, testServer)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(JwtProperties.JWT_ACCESS_NAME, JwtProperties.JWT_ACCESS_NAME, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference(JwtProperties.JWT_ACCESS_NAME, authorizationScopes));
    }

}
