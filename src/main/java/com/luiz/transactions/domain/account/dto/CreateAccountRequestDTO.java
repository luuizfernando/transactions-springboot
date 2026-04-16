package com.luiz.transactions.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Corpo opcional (pode ser enviado como objeto vazio {})")
public record CreateAccountRequestDTO(

) {}