package com.hht.hsatellitemobile.ui.mqtt;

public class MqttAlarmData {
    private final String alarmId;
    private final String title;
    private final String timeText;
    private final String message;
    private final String dedupeKey;
    private final String payload;

    public MqttAlarmData(String alarmId, String title, String timeText, String message, String dedupeKey) {
        this(alarmId, title, timeText, message, dedupeKey, "");
    }

    public MqttAlarmData(String alarmId, String title, String timeText, String message, String dedupeKey, String payload) {
        this.alarmId = alarmId;
        this.title = title;
        this.timeText = timeText;
        this.message = message;
        this.dedupeKey = dedupeKey;
        this.payload = payload;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public String getTitle() {
        return title;
    }

    public String getTimeText() {
        return timeText;
    }

    public String getMessage() {
        return message;
    }

    public String getDedupeKey() {
        return dedupeKey;
    }

    public String getPayload() {
        return payload;
    }
}
