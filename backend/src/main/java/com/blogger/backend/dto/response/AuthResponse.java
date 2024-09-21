package com.blogger.backend.dto.response;

import com.blogger.backend.model.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse implements Serializable {

    GetUserByEmailResponse user;
    Token token;

}
