package com.xp.store.utils.Exception;


import lombok.Getter;

@Getter
public class LoginException extends RuntimeException {

    public LoginException(String message) {
        super(message);
    }
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

}
