package com.lm2a.tacoonline.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

@Data
public class Order {

    @NotBlank(message="El nombre es obligatorio")
    private String deliveryName;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZip;

    @CreditCardNumber(message="No es una tarjeta valida!")
    private String ccNumber;
    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message="Debe tener un formato MM/AA")
    private String ccExpiration;
    @Digits(integer=3, fraction=0, message="CVV invalido!")
    private String ccCVV;
}
