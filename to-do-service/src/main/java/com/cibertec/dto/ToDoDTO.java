package com.cibertec.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String description;
    private Boolean completed;
    private String createdAt;
    private String updatedAt;
    private Integer userId;      // solo id del usuario (evitamos serializar todo User)
    private String username;   
}
