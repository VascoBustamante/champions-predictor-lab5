package com.lab5.champions.controller;
import com.lab5.champions.model.Resultado;
import com.lab5.champions.service.PartidoService;
import com.lab5.champions.service.PrediccionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class WebController {
 private final PartidoService partidos;private final PrediccionService predicciones;
 public WebController(PartidoService partidos,PrediccionService predicciones){this.partidos=partidos;this.predicciones=predicciones;}
 @GetMapping("/") public String inicio(Model model){model.addAttribute("partidos",partidos.listar());model.addAttribute("resultados",Resultado.values());return "index";}
 @PostMapping("/predicciones") public String crear(@RequestParam Long partidoId,@RequestParam Resultado resultado,@RequestParam Integer golesLocal,@RequestParam Integer golesVisitante,RedirectAttributes redirect){
  try{predicciones.crear(partidoId,resultado,golesLocal,golesVisitante);redirect.addFlashAttribute("mensaje","Predicción registrada correctamente");}
  catch(IllegalArgumentException ex){redirect.addFlashAttribute("error",ex.getMessage());}
  return "redirect:/";
 }
 @GetMapping("/predicciones") public String listar(Model model){model.addAttribute("predicciones",predicciones.listar());return "predicciones";}
}
