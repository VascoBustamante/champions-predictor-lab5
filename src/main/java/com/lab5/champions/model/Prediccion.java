package com.lab5.champions.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name="predicciones")
public class Prediccion {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="partido_id",nullable=false) private Partido partido;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Resultado resultado;
 @Column(nullable=false) private Integer golesLocal;
 @Column(nullable=false) private Integer golesVisitante;
 @Column(nullable=false) private LocalDateTime creadoEn=LocalDateTime.now();
 public Long getId(){return id;}
 public Partido getPartido(){return partido;}
 public void setPartido(Partido v){partido=v;}
 public Resultado getResultado(){return resultado;}
 public void setResultado(Resultado v){resultado=v;}
 public Integer getGolesLocal(){return golesLocal;}
 public void setGolesLocal(Integer v){golesLocal=v;}
 public Integer getGolesVisitante(){return golesVisitante;}
 public void setGolesVisitante(Integer v){golesVisitante=v;}
 public LocalDateTime getCreadoEn(){return creadoEn;}
}
