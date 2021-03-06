package br.com.justworks.prestador.ServicoAki.Model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private Boolean active;
    private Boolean isAuthenticated;
    private Boolean isNew;
    private Boolean isProfessional;
    private String createdEnv;
    private String name;
    private String birthDate;
    private Timestamp birthDateTimestamp;
    private CivilState civilState;
    private String dispatchingAgency;
    private String email;
    private String emissionDate;
    private String governmentId;
    private String proofOfAddressImage;
    private String documentSelfie;
    private String idBackImage;
    private String idFrontImage;
    private String identifyDocument;
    private String imageUrl;
    private String motherName;
    private String phoneNumber;
    private String sex;
    private ArrayList<ServiceUser> services;

    public User() {
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(Boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean aNew) {
        isNew = aNew;
    }

    public Boolean getIsProfessional() {
        return isProfessional;
    }

    public void setIsProfessional(Boolean professional) {
        isProfessional = professional;
    }

    public String getCreatedEnv() {
        return createdEnv;
    }

    public void setCreatedEnv(String createdEnv) {
        this.createdEnv = createdEnv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Timestamp getBirthDateTimestamp() {
        return birthDateTimestamp;
    }

    public void setBirthDateTimestamp(Timestamp birthDateTimestamp) {
        this.birthDateTimestamp = birthDateTimestamp;
    }

    public CivilState getCivilState() {
        return civilState;
    }

    public void setCivilState(CivilState civilState) {
        this.civilState = civilState;
    }

    public String getDispatchingAgency() {
        return dispatchingAgency;
    }

    public void setDispatchingAgency(String dispatchingAgency) {
        this.dispatchingAgency = dispatchingAgency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(String emissionDate) {
        this.emissionDate = emissionDate;
    }

    public String getGovernmentId() {
        return governmentId;
    }

    public void setGovernmentId(String governamentId) {
        this.governmentId = governamentId;
    }

    public String getProofOfAddressImage() {
        return proofOfAddressImage;
    }

    public void setProofOfAddressImage(String proofOfAddressImage) {
        this.proofOfAddressImage = proofOfAddressImage;
    }

    public String getDocumentSelfie() {
        return documentSelfie;
    }

    public void setDocumentSelfie(String documentSelfie) {
        this.documentSelfie = documentSelfie;
    }

    public String getIdBackImage() {
        return idBackImage;
    }

    public void setIdBackImage(String idBackImage) {
        this.idBackImage = idBackImage;
    }

    public String getIdFrontImage() {
        return idFrontImage;
    }

    public void setIdFrontImage(String idFrontImage) {
        this.idFrontImage = idFrontImage;
    }

    public String getIdentifyDocument() {
        return identifyDocument;
    }

    public void setIdentifyDocument(String identifyDocument) {
        this.identifyDocument = identifyDocument;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public ArrayList<ServiceUser> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServiceUser> services) {
        this.services = services;
    }

    public Map<String, Object> toMap(){

        Map<String, Object> data = new HashMap<>();
        data.put("createdEnv", createdEnv);
        data.put("active", active);
        data.put("isNew", isNew);
        data.put("isProfessional", isProfessional);
        data.put("isAuthenticated", isAuthenticated);
        data.put("name", name);
        data.put("email", email);
        data.put("birthDate", birthDate);
        data.put("birthDateTimestamp", birthDateTimestamp);
        data.put("civilState", civilState);
        data.put("imageUrl", imageUrl);
        data.put("motherName", motherName);
        data.put("phoneNumber", phoneNumber);
        data.put("sex", sex);
        data.put("dispatchingAgency", dispatchingAgency);
        data.put("emissionDate", emissionDate);
        data.put("governmentId", governmentId);
        data.put("identifyDocument", identifyDocument);
        data.put("documentSelfie", documentSelfie);
        data.put("idBackImage", idBackImage);
        data.put("idFrontImage", idFrontImage);
        data.put("proofOfAddressImage", proofOfAddressImage);
        data.put("services", services);

        return data;
    }
}
