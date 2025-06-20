package com.xp.store.controllers;

import com.xp.store.utils.Validations.BasicInfoLogin;
import com.xp.store.utils.Validations.BasicInfoToken;
import com.xp.store.dto.LoginDTO;
import com.xp.store.dto.TokenDTO;
import com.xp.store.utils.Exception.LoginException;
import com.xp.store.service.ClienteService;
import com.xp.store.model.Cliente;
import com.xp.store.security.jwt.JWTUtil;
import com.xp.store.utils.Logger.UtilsLogger;
import com.xp.store.utils.response.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
public class LoginController implements UtilsLogger {

    JWTUtil jwtUtil = new JWTUtil();
    private final ClienteService userService;
    private final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final ResponseUtils responseUtils = new ResponseUtils();

    @Operation(summary = "Método que realiza o acesso", description = "Recebe as credenciais de acesso a XP Store. Bearer ${token fixo}")
    @PostMapping(path = "")
    public ResponseEntity<Object> login(@Validated({BasicInfoLogin.class}) @RequestBody LoginDTO loginDTO) {
        try {
            bodyLoginValidated(loginDTO);
            Optional<Cliente> findUser = userService.getClientByEmail(loginDTO.email());
            if (findUser.isEmpty()) {
                return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Cliente não encontrado!", null);
            }
            Cliente user = findUser.get();
            if (!loginDTO.senha().trim().equals(user.getSenha())) {
                return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Senha incorreta!", null);
            }
            String jwt = jwtUtil.createToken(user.getEmail());
            return responseUtils.successResponse(HttpStatus.OK, 200, "Login realizado!", jwt);
        } catch (Exception e) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, e.getMessage(), e.toString());
        }
    }


    @Operation(summary = "Método que atualiza o token de acesso", description = "Atualiza as credenciais de acesso a XP Store. Bearer ${token fixo}")
    @PostMapping(path = "/token")
    public ResponseEntity<Object> refreshToken(@Validated({BasicInfoToken.class}) @RequestBody TokenDTO token) {
        try {
            bodyTokenValidated(token);
            String userEmail = jwtUtil.getEmailFromJwtToken(token.token());
            Optional<Cliente> user = userService.getClientByEmail(userEmail);
            if (user.isEmpty()) {
                return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Usuário não encontrado na base de dados", null);
            }
            String newToken = jwtUtil.refreshToken(token.token());
            return responseUtils.successResponse(HttpStatus.OK, 200, "Token atualizado com sucesso!", newToken);
        } catch (Exception e) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Falha ao atualizar o token", e);
        }
    }

    private void bodyTokenValidated(TokenDTO token) {
        if (token == null || token.token().isEmpty() || token.token().isBlank()) {
            throw new LoginException("Informe o Token!");
        }
    }
    private void bodyLoginValidated(LoginDTO loginDTO) {
        if (loginDTO == null) {
            throw new LoginException("Informações de login não pode ser nula.");
        }

        if (loginDTO.email() == null || loginDTO.email().isEmpty()) {
            throw new LoginException("Email é obrigatório.");
        }

        if (loginDTO.senha() == null || loginDTO.senha().isBlank()) {
            throw new LoginException("Senha é obrigatória.");
        }

    }

}
