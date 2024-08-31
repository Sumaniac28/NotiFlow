package notiflow.server.Requests;

import notiflow.server.Enums.RecipientType;

public class RecipientRequest {

    private String email;
    private RecipientType type;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RecipientType getType() {
        return type;
    }

    public void setType(RecipientType type) {
        this.type = type;
    }
}
