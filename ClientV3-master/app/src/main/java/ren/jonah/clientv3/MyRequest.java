package ren.jonah.clientv3;

public class MyRequest {

    String title, id, clientId, clientFirstName, clientLastName, clientEmail;

    public MyRequest(String title, String id, String clientId, String clientFirstName, String clientLastName, String clientEmail) {
        this.title = title;
        this.id = id;
        this.clientId = clientId;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.clientEmail = clientEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getClientName() {
        return clientFirstName + " " + clientLastName;
    }
}

