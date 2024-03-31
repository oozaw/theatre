package oozaw.theatre.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    
    private String id;

    private String name;

    private String email;

    private String phone;

    private Time createdAt;

    private Time updatedAt;
}
