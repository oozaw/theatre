package oozaw.theatre.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oozaw.theatre.entity.User;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    private Long expiredAt;

    private String id;

    private String name;

    private String email;

    private String phone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public AuthResponse(User user) {
        this.token = user.getToken();
        this.expiredAt = user.getTokenExpiredAt();
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
