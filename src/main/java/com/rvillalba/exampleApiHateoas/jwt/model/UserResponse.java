package com.rvillalba.exampleApiHateoas.jwt.model;

import java.util.List;

import org.apache.coyote.http2.Setting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String username;
    private List<Setting> settings;
}
