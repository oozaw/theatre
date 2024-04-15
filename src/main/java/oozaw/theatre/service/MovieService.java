package oozaw.theatre.service;

import oozaw.theatre.model.MovieResponse;

import java.util.List;

public interface MovieService {
   List<MovieResponse> getMany(String title);

   MovieResponse get(String id);
}
