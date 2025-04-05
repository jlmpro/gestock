package com.mystock.mygestock.dto;


import com.mystock.mygestock.model.Roles;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RolesDto {
    private Long id ;
    private String roleName;
    private UtilisateurDto utilisateur;



    public static RolesDto fromEntity(Roles roles) {
        if (roles == null) {
            return null;
        }
        return RolesDto.builder()
                .id(roles.getId())
                .roleName(roles.getRoleName())
                .build();
    }
    public static Roles toEntity(RolesDto rolesDto){
        Roles roles = new Roles();
        roles.setId(rolesDto.getId());
        roles.setRoleName(rolesDto.getRoleName());
        return roles;
    }
}
