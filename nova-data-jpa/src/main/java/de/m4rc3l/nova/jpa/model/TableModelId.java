package de.m4rc3l.nova.jpa.model;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class TableModelId extends TableModel {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
}
