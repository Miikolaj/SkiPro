package com.example.skipro.util;

import com.example.skipro.model.Instructor;
import com.example.skipro.model.Lesson;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonMapper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");

    public static Map<String, Object> instructorToMap(Instructor instructor) {
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", instructor.getFirstName());
        map.put("lastName", instructor.getLastName());
        map.put("id", instructor.getId());
        map.put("qualificationLevel", instructor.getQualificationLevel());
        List<Integer> ratings = instructor.getRatings();
        double avgRating = 0.0;
        if (ratings != null && !ratings.isEmpty()) {
            avgRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            avgRating = Math.round(avgRating * 10.0) / 10.0;
        }
        map.put("rating", avgRating);
        return map;
    }

    /**
     * Lightweight representation of a lesson for list views.
     * Does NOT include the full clients list.
     */
    public static Map<String, Object> lessonToTileMap(Lesson lesson) {
        Map<String, Object> lessonMap = new HashMap<>();
        lessonMap.put("id", lesson.getId());
        lessonMap.put("date", lesson.getDateTime().format(DATE_TIME_FORMATTER));
        lessonMap.put("duration", formatDuration(lesson.getDuration()));
        lessonMap.put("status", lesson.getStatus());
        lessonMap.put("instructor", instructorToMap(lesson.getInstructor()));
        int clientsCount = lesson.getClients() == null ? 0 : lesson.getClients().size();
        lessonMap.put("clientsCount", clientsCount);
        return lessonMap;
    }

    public static Map<String, Object> lessonToMap(Lesson lesson) {
        Map<String, Object> lessonMap = lessonToTileMap(lesson);

        // Keep clients list only for details views / explicit fetches
        lessonMap.put("clients", (lesson.getClients() == null ? List.<Object>of() : lesson.getClients().stream()
                .map(c -> {
                    Map<String, Object> clientMap = new HashMap<>();
                    clientMap.put("firstName", c.getFirstName());
                    clientMap.put("lastName", c.getLastName());
                    clientMap.put("id", c.getId());
                    return clientMap;
                })
                .toList()));

        return lessonMap;
    }

    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m");
        return sb.toString().trim();
    }
}