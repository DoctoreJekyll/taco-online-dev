package com.lm2a.tacoonline.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason="El taco no existe")
public class TacoNotFoundException extends RuntimeException {

}
