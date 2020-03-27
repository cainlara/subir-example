package io.github.cainlara.subir.example;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {

  public static void main(String[] args) {
    List<String> fieldsToGroupBy = Arrays.stream(new String[] { "eventName", "styleColorId" })
        .collect(Collectors.toList());

    List<EpEvent> events = buildEvents(2, 2);

    Map<List<String>, List<EpEvent>> grouped = doTheMagicTrick(events, fieldsToGroupBy);

    System.out.println(grouped);
  }

  private static Map<List<String>, List<EpEvent>> doTheMagicTrick(List<EpEvent> eventsList,
      List<String> fieldsToGroupBy) {
    final MethodHandles.Lookup lookup = MethodHandles.lookup();

    List<MethodHandle> methodHandlers = fieldsToGroupBy.stream().map(field -> {
      try {
        return lookup.findVirtual(EpEvent.class, getGetterName(field), MethodType.methodType(String.class));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).collect(toList());

    return eventsList.stream().collect(groupingBy(d -> methodHandlers.stream().map(methodHandler -> {
      try {
        return (String) methodHandler.invokeExact(d);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }).collect(toList())));
  }

  private static List<EpEvent> buildEvents(int amountOfEvents, int amountOfStyleColors) {
    List<EpEvent> events = new ArrayList<>();

    String eventName = "Event-";
    String styleColorId = "StyleColor-";
    String sapId = "Sap-";
    int sapIndex = 0;

    for (int eventIndex = 0; eventIndex < amountOfEvents; eventIndex++) {
      for (int styleColorIndex = 0; styleColorIndex < amountOfStyleColors; styleColorIndex++) {
        EpEvent event = new EpEvent(eventName + eventIndex, styleColorId + styleColorIndex, sapId + sapIndex++);
        events.add(event);
      }
    }

    return events;
  }

  private static String getGetterName(final String fieldName) {
    return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
  }
}
