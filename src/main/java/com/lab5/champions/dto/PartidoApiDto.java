package com.lab5.champions.dto;
import java.time.LocalDateTime;
public record PartidoApiDto(String id, String local, String visitante, LocalDateTime fecha, String etapa) {}
