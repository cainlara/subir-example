package io.github.cainlara.subir.example;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EpEvent {
  private String eventName;
  private String styleColorId;
  private String sapId;

  public EpEvent(final String eventName, final String styleColorId, final String sapId) {
    this.eventName = eventName;
    this.styleColorId = styleColorId;
    this.sapId = sapId;
  }
}
