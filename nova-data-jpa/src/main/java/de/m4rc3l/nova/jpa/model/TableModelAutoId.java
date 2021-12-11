package de.m4rc3l.nova.jpa.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class TableModelAutoId extends TableModel {

  @Id
  @GeneratedValue
  @Column(name = "id", updatable = false, nullable = false, unique = true)
  private UUID id;
}
