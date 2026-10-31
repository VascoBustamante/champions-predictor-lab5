package com.lab5.champions.controller;
import com.lab5.champions.model.Partido;
import com.lab5.champions.model.Prediccion;
import com.lab5.champions.service.PartidoService;
import com.lab5.champions.service.PrediccionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api")
public class ApiController {
 private final PartidoService partidos;private final PrediccionService predicciones;
 public ApiController(PartidoService partidos,PrediccionService predicciones){this.partidos=partidos;this.predicciones=predicciones;}
 @GetMapping("/partidos") public List<Partido> partidos(){return partidos.listar();}
 @GetMapping("/predicciones") public List<Prediccion> predicciones(){return predicciones.listar();}
}
