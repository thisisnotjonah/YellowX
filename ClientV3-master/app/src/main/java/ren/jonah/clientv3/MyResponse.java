package ren.jonah.clientv3;

public class MyResponse {

    private String id, contractorId, contractorFirstName, contractorLastName, contractorEmail, description;

    public MyResponse(String id, String contractorId, String contractorFirstName, String contractorLastName, String contractorEmail, String description) {
        this.id = id;
        this.contractorId = contractorId;
        this.contractorFirstName = contractorFirstName;
        this.contractorLastName = contractorLastName;
        this.contractorEmail = contractorEmail;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getContractorId() {
        return contractorId;
    }

    public String getContractorFirstName() {
        return contractorFirstName;
    }

    public String getContractorLastName() {
        return contractorLastName;
    }

    public String getContractorName() {
        return contractorFirstName + " " + contractorLastName;
    }

    public String getContractorEmail() {
        return contractorEmail;
    }

    public String getDescription() {
        return  description;
    }
}
