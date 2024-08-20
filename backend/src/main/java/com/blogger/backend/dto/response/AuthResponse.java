package com.blogger.backend.dto.response;

import com.blogger.backend.model.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {

    GetUserByEmailResponse user;
    Token token;

}
