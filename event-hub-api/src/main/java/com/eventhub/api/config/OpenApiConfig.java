package com.eventhub.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Event Hub API")
                        .version("1.0.0")
                        .description("""
                                API para gestão de eventos e venda de ingressos.
                                
                                **Destaques:**
                                - Documentação automática via SpringDoc
                                - Paginação e Ordenação nativas
                                - Tratamento de Erros RFC 7807
                                """));
    }
}