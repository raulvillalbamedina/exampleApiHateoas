package com.rvillalba.exampleApiHateoas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Example {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String name;
}
