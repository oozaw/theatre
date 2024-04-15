package oozaw.theatre.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

   private String synopsys;

   private String duration;

   @Column(name = "created_at")
   private LocalDateTime createdAt;


   @Column(name = "updated_at")
   private LocalDateTime updatedAt;
}
