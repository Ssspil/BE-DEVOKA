package com.jspp.devoka.common.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
    info = @Info(title = "모르는 용어는 없다, IT 용어 백과사전",
            description = "승필, 민우, 해원이 만든 IT 용어 백과사전 프로젝트입니다.",
            version = "v0.0.1"),
    servers = {
            @Server(url = "http://localhost:8080", description = "로컬 서버"),
            @Server(url = "https://aoxx.co.kr", description = "운영 서버"),
    }
)
@Configuration
public class SwaggerConfig {

}
