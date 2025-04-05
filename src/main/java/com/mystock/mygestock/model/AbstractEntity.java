package com.mystock.mygestock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Setter
@Getter
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @CreatedDate
    @JsonIgnore
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate ;
    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    @JsonIgnore
    private Instant lastModifiedDate ;



}
