package com.mystock.mygestock.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChangerPasswordUtilisateur {
    private Long id;
   // private String email;
    private String password;
    private String newPassword;
}
