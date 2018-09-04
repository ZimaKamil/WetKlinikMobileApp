package com.example.asztar.wetklinikmobileapp;

import com.example.asztar.wetklinikmobileapp.ApiConn.ApiConnection;
import com.example.asztar.wetklinikmobileapp.ApiConn.RestResponse;

public class Controller {
    ApiConnection apiConnection;
    RestResponse exceptionRequestResponse = new RestResponse(-1, "");

    public Controller(ApiConnection apiConnection) {
        this.apiConnection = apiConnection;
    }

    public RestResponse postLogin(String mEmail, String mPassword) {
        try {
            apiConnection.setRequest("/api/token");
            apiConnection.setReqMethod("POST");
            apiConnection.addBodyParameter("grant_type", "password");
            apiConnection.addBodyParameter("username", mEmail);
            apiConnection.addBodyParameter("password", mPassword);
            RestResponse response = apiConnection.execute();
            return response;
            }
        catch (Exception e) {
            e.getMessage();
        }
            return exceptionRequestResponse;
        }

    public RestResponse getClinic(String clinicId) {
        try {
            apiConnection.setRequest("/api/clinics/{clinicId}");
            apiConnection.setReqMethod("GET");
            apiConnection.addUrlSegment("{clinicId}", clinicId);
            RestResponse response = apiConnection.execute();
            return response;
            }
        catch (Exception e) {
            e.getMessage();
        }
        return exceptionRequestResponse;
    }

    public RestResponse getPets() {
        try {
            apiConnection.setRequest("/api/Pets");
            apiConnection.setReqMethod("GET");
            apiConnection.setAuthorize(true);
            RestResponse response = apiConnection.execute();
            return response;
        }
        catch (Exception e) {
            e.getMessage();
        }
        return exceptionRequestResponse;
    }

    public RestResponse getClient() {
        try {
            apiConnection.setRequest("/api/Clients");
            apiConnection.setReqMethod("GET");
            apiConnection.setAuthorize(true);
            RestResponse response = apiConnection.execute();
            return response;
        }
        catch (Exception e) {
            e.getMessage();
        }
        return exceptionRequestResponse;
    }

    public boolean putPet(String PetId, String PetDesc) {
        try {
            apiConnection.setRequest("/api/Pets/{PetId}");
            apiConnection.setReqMethod("GET");
            apiConnection.setAuthorize(true);
            apiConnection.addHeaderParameter("{PetDesc}", PetDesc);
            RestResponse response = apiConnection.execute();
            if(response.ResponseCode == 204)
                return true;
        }
        catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public RestResponse getPetTreatment(String PetId) {
        try {
            apiConnection.setRequest("/api/PetTreatments/{PetId}");
            apiConnection.setReqMethod("GET");
            apiConnection.setAuthorize(true);
            apiConnection.addUrlSegment("{PetId}", PetId);
            RestResponse response = apiConnection.execute();
            return response;
        }
        catch (Exception e) {
            e.getMessage();
        }
        return exceptionRequestResponse;
    }

    public RestResponse getAppointments() {
        try {
            apiConnection.setRequest("/api/Appointments");
            apiConnection.setReqMethod("GET");
            apiConnection.setAuthorize(true);
            RestResponse response = apiConnection.execute();
            return response;
        }
        catch (Exception e) {
            e.getMessage();
        }
        return exceptionRequestResponse;
    }

    public RestResponse getEmployees(String ClinicId) {
        try {
            apiConnection.setRequest("/api/Employees/{ClinicId}");
            apiConnection.setReqMethod("GET");
            apiConnection.setAuthorize(true);
            apiConnection.addUrlSegment("{ClinicId}", ClinicId);
            RestResponse response = apiConnection.execute();
            return response;
        }
        catch (Exception e) {
            e.getMessage();
        }
        return exceptionRequestResponse;
    }

    public void postLogout() {
        try {
            apiConnection.setRequest("api/Account/Logout");
            apiConnection.setReqMethod("POST");
            apiConnection.setAuthorize(true);
            RestResponse response = apiConnection.execute();
            Token.reset();
        }
        catch (Exception e) {
            e.getMessage();
        }
    }

    public void postChangePassword(String OldPassword, String NewPassword, String ConfirmPassword) {
        try {
            apiConnection.setRequest("api/Account/ChangePassword");
            apiConnection.setReqMethod("POST");
            apiConnection.setAuthorize(true);
            apiConnection.addBodyParameter("OldPassword", OldPassword);
            apiConnection.addBodyParameter("NewPassword", NewPassword);
            apiConnection.addBodyParameter("ConfirmPassword", ConfirmPassword);
            RestResponse response = apiConnection.execute();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
