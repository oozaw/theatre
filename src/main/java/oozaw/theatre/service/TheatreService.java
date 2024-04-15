package oozaw.theatre.service;

import oozaw.theatre.model.TheatreResponse;

import java.util.List;

public interface TheatreService {

   List<TheatreResponse> getMany(String name);
}
