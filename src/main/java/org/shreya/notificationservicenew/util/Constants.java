package org.shreya.notificationservicenew.util;

public class Constants {
    //Failure Codes
    public static final String ERROR = "Error";
    public static final String ERROR_INVALID_REQUEST = "INVALID REQUEST";
    public static final String ERROR_INTERNAL_SERVER = "INTERNAL_SERVER_ERROR";
    public static final String ERROR_BLACKLISTED = "ERROR_BLACKLISTED";
    public static final String ERROR_API = "API_ERROR";

    // HTTP Response Messages
    public static final String ERROR_REQUEST_ID_NOT_FOUND_MESSAGE = "request_id not found";
    public static final String ERROR_UNEXPECTED_MESSAGE = "An unexpected error occurred.";
    public static final String ERROR_INVALID_PHONE_MESSAGE = "Invalid Indian phone number. Must start with 6, 7, 8, or 9 and be 10 digits long.";
    public static final String ERROR_EMPTY_MESSAGE = "Message cannot be empty!";
    public static final String ERROR_LIMIT_MESSAGE = "Message cannot exceed 500 characters.";
    public static final String ERROR_EMPTY_PHONE_MESSAGE = "Phone number cannot be empty!";
    public static final String ERROR_BLACKLISTED_MESSAGE = "Phone number is blacklisted!";
    public static final String ERROR_API_MESSAGE = "Failed to send SMS via 3rd party API";
    public static final String ERROR_INVALID_DATE_FORMAT = "Failed to send SMS via 3rd party API";

    // Validation Regex
    public static final String REGEX_INDIAN_PHONE = "^(?:\\+91|91)?[6789]\\d{9}$";

    //Kafka
    public static final String KAFKA_NOTIFICATION_TOPIC = "notifications";
    public static final String KAFKA_GROUP_ID = "myGroup";

    //Redis
    public static final String BLACKLIST_KEY = "notification:blacklist";
}
