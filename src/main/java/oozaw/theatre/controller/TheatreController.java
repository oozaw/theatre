package oozaw.theatre.controller;

import oozaw.theatre.model.TheatreResponse;
import oozaw.theatre.model.WebResponse;
import oozaw.theatre.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/theatres")
public class TheatreController {

   @Autowired
   private TheatreService theatreService;

   @GetMapping(
      path = "",
      produces = MediaType.APPLICATION_JSON_VALUE
   )
   ResponseEntity<WebResponse<List<TheatreResponse>>> getMany(
      @RequestParam(required = false) String name
   ) {

      List<TheatreResponse> theatreResponses = theatreService.getMany(name);

      return ResponseEntity.status(HttpStatus.OK).body(
         WebResponse.<List<TheatreResponse>>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .data(theatreResponses)
            .build()
      );
   }
}
