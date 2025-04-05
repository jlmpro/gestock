package com.mystock.mygestock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class Category extends AbstractEntity {
    @Column(name = "code")
    private String code;
    @Column(name = "designation")
    private String designation ;
    @OneToMany(mappedBy = "category")
    private List<Article> articles ;

}
