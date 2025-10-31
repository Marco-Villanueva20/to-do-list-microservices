package com.cibertec.mapper;

import com.cibertec.dto.ToDoDTO;
import com.cibertec.entity.ToDo;

public class ToDoMapper {
    public static ToDoDTO toDto(ToDo e) {
        if (e == null)
            return null;
        ToDoDTO d = ToDoDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .completed(e.getCompleted())
                .createdAt(e.getCreatedAt().toString())
                .updatedAt(e.getUpdatedAt().toString())
                .build();

        if (e.getUser() != null) {
            d.setUserId(e.getUser().getId());
            d.setUsername(e.getUser().getUsername());
        }
        return d;
    }

    public static ToDo toEntity(ToDoDTO d) {
        if (d == null)
            return null;
        ToDo e = new ToDo();
        e.setId(d.getId());
        e.setTitle(d.getTitle());
        e.setDescription(d.getDescription());
        e.setCompleted(d.getCompleted() == null ? false : d.getCompleted());
        // createdAt/updatedAt se manejan por JPA @PrePersist/@PreUpdate
        return e;
    }
}
