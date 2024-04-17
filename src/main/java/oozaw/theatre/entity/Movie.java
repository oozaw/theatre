package oozaw.theatre.entity;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oozaw.theatre.model.Genre;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {

   @Id
   private String id;

   private String title;

   @Type(value = StringArrayType.class)
   @Enumerated(EnumType.STRING)
   private Genre[] genres;

   private String synopsys;

   private String duration;

   @Column(name = "created_at")
   private LocalDateTime createdAt;

   @Column(name = "updated_at")
   private LocalDateTime updatedAt;
}
