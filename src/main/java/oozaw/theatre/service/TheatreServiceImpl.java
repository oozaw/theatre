package oozaw.theatre.service;

import jakarta.transaction.Transactional;
import oozaw.theatre.entity.Theatre;
import oozaw.theatre.model.TheatreResponse;
import oozaw.theatre.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheatreServiceImpl implements TheatreService {

   @Autowired
   private TheatreRepository theatreRepository;

   @Transactional
   @Override
   public List<TheatreResponse> getMany(String name) {
      List<Theatre> theatres;

      if (name != null && !name.isEmpty() && !name.isBlank()) {
         theatres = theatreRepository.findByNameContainsIgnoreCase(name);
      } else {
         theatres = theatreRepository.findAll();
      }

      if (theatres.isEmpty()) {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No theatres found");
      }

      return theatres.stream().map(TheatreResponse::fromEntity).collect(Collectors.toList());
   }

   @Transactional
   @Override
   public TheatreResponse get(String id) {
      return theatreRepository.findById(id)
         .map(TheatreResponse::fromEntity)
         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Theatre not found"));
   }
}
