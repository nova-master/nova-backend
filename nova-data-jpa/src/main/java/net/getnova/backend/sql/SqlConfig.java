package net.getnova.backend.sql;

import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Config
class SqlConfig {

  @Value("${SQL_SERVER_LOCATION:postgresql://localhost:5432}")
  private String location;

  @Value("${SQL_SERVER_DATABASE:nova}")
  private String database;

  @Value("${SQL_SERVER_USERNAME:nova}")
  private String username;

  @Value("${SQL_SERVER_PASSWORD:nova}")
  private String password;

  @Value("${SQL_SHOW_STATEMENTS:false}")
  private boolean showStatements;
}
