package com.lab5.champions.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name="partidos", uniqueConstraints=@UniqueConstraint(columnNames="external_id"))
public class Partido {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(name="external_id",nullable=false,unique=true) private String externalId;
  @Column(nullable=false) private String local;
  @Column(nullable=false) private String visitante;
  @Column(nullable=false) private LocalDateTime fecha;
  @Column(nullable=false) private String etapa;
  public Long getId(){return id;}
  public String getExternalId(){return externalId;}
  public void setExternalId(String v){externalId=v;}
  public String getLocal(){return local;}
  public void setLocal(String v){local=v;}
  public String getVisitante(){return visitante;}
  public void setVisitante(String v){visitante=v;}
  public LocalDateTime getFecha(){return fecha;}
  public void setFecha(LocalDateTime v){fecha=v;}
  public String getEtapa(){return etapa;}
  public void setEtapa(String v){etapa=v;}
}
