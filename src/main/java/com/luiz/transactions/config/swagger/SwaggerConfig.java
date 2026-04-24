package com.luiz.transactions.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Transactions API")
                                .version("v1.0")
                                .description("""
                                    API REST para gerenciamento de transações financeiras com classificação e análise via IA local (Ollama).
                                    
                                    Funcionalidades:
                                    - Depósitos e transferências com suporte a idempotência
                                    - Classificação automática de transações por IA
                                    - Análise de risco com explicação gerada por IA
                                    - Resumo financeiro inteligente por categoria
                                """)
                                .contact(
                                        new Contact()
                                        .name("Luiz Fernando")
                                        .url("https://www.linkedin.com/in/luizfernando-java-developer/")
                                )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}