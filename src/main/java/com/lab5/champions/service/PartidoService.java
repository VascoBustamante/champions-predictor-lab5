package com.lab5.champions.service;
import com.lab5.champions.dto.PartidoApiDto;
import com.lab5.champions.model.Partido;
import com.lab5.champions.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import java.util.List;
@Service
public class PartidoService implements ApplicationRunner {
 private final PartidoRepository repository;
 private final RestClient client=RestClient.create();
 @Value("${matches.api.url}") private String apiUrl;
 public PartidoService(PartidoRepository repository){this.repository=repository;}
 @Override @Transactional
 public void run(ApplicationArguments args){
  List<PartidoApiDto> datos=client.get().uri(apiUrl).retrieve().body(new ParameterizedTypeReference<List<PartidoApiDto>>(){});
  if(datos==null||datos.isEmpty()) throw new IllegalStateException("La API no devolvió partidos");
  for(PartidoApiDto dto:datos){
   if(dto.id()==null||dto.local()==null||dto.visitante()==null||dto.fecha()==null||dto.etapa()==null) throw new IllegalStateException("Partido incompleto en API");
   if(!repository.existsByExternalId(dto.id())){
    Partido partido=new Partido();
    partido.setExternalId(dto.id());partido.setLocal(dto.local());partido.setVisitante(dto.visitante());
    partido.setFecha(dto.fecha());partido.setEtapa(dto.etapa());repository.save(partido);
   }
  }
 }
 public List<Partido> listar(){return repository.findAll(org.springframework.data.domain.Sort.by("fecha"));}
 public Partido buscar(Long id){return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Partido no encontrado"));}
}
