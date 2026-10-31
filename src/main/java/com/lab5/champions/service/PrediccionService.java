package com.lab5.champions.service;
import com.lab5.champions.model.*;
import com.lab5.champions.repository.PrediccionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class PrediccionService {
 private final PrediccionRepository repo;
 private final PartidoService partidos;
 public PrediccionService(PrediccionRepository repo,PartidoService partidos){this.repo=repo;this.partidos=partidos;}
 @Transactional
 public void crear(Long partidoId,Resultado resultado,Integer gl,Integer gv){
  if(gl==null||gv==null||gl<0||gv<0||gl>99||gv>99) throw new IllegalArgumentException("Los goles deben estar entre 0 y 99");
  Resultado calculado=gl>gv?Resultado.LOCAL:gl<gv?Resultado.VISITANTE:Resultado.EMPATE;
  if(resultado==null||resultado!=calculado) throw new IllegalArgumentException("El resultado escogido debe coincidir con el marcador");
  Prediccion p=new Prediccion();p.setPartido(partidos.buscar(partidoId));p.setResultado(resultado);p.setGolesLocal(gl);p.setGolesVisitante(gv);repo.save(p);
 }
 public List<Prediccion> listar(){return repo.findAll(Sort.by(Sort.Direction.DESC,"creadoEn"));}
}
