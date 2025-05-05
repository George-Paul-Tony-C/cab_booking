package com.example.cab_booking.payload.response;

public class MessageResponse {
    private String message;

    // No-args constructor for Jackson
    public MessageResponse() {
    }

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
