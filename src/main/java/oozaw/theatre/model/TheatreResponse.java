package oozaw.theatre.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oozaw.theatre.entity.Theatre;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheatreResponse {

   private String id;

   private String name;

   private String city;

   private String province;

   private String address;

   private String latitude;

   private String longitude;

   private LocalDateTime createdAt;

   private LocalDateTime updatedAt;

   public static TheatreResponse fromEntity(Theatre theatre) {
      return TheatreResponse.builder()
         .id(theatre.getId())
         .name(theatre.getName())
         .city(theatre.getCity())
         .province(theatre.getProvince())
         .address(theatre.getAddress())
         .latitude(theatre.getLatitude())
         .longitude(theatre.getLongitude())
         .createdAt(theatre.getCreatedAt())
         .updatedAt(theatre.getUpdatedAt())
         .build();
   }
}
