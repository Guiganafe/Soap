package br.com.justworks.prestador.ServicoAki.Model;

import java.util.ArrayList;

public class Services {
    private boolean active;
    private boolean allowEditDescription;
    private ArrayList<CategoriesServices> category;
    private ArrayList<String> caregoryIds;
    private boolean certificationRequired;
    private Description description;
    private Name name;
    private int qtdUsers;
    private ServiceLocation serviceLocation;
    private ServiceType serviceType;
    private TypePayment typePayment;

    public Services() {
    }

    public Services(boolean active, boolean allowEditDescription, ArrayList<CategoriesServices> category, ArrayList<String> caregoryIds, boolean certificationRequired, Description description, Name name, int qtdUsers, ServiceLocation serviceLocation, ServiceType serviceType, TypePayment typePayment) {
        this.active = active;
        this.allowEditDescription = allowEditDescription;
        this.category = category;
        this.caregoryIds = caregoryIds;
        this.certificationRequired = certificationRequired;
        this.description = description;
        this.name = name;
        this.qtdUsers = qtdUsers;
        this.serviceLocation = serviceLocation;
        this.serviceType = serviceType;
        this.typePayment = typePayment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAllowEditDescription() {
        return allowEditDescription;
    }

    public void setAllowEditDescription(boolean allowEditDescription) {
        this.allowEditDescription = allowEditDescription;
    }

    public ArrayList<CategoriesServices> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<CategoriesServices> category) {
        this.category = category;
    }

    public ArrayList<String> getCaregoryIds() {
        return caregoryIds;
    }

    public void setCaregoryIds(ArrayList<String> caregoryIds) {
        this.caregoryIds = caregoryIds;
    }

    public boolean isCertificationRequired() {
        return certificationRequired;
    }

    public void setCertificationRequired(boolean certificationRequired) {
        this.certificationRequired = certificationRequired;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public int getQtdUsers() {
        return qtdUsers;
    }

    public void setQtdUsers(int qtdUsers) {
        this.qtdUsers = qtdUsers;
    }

    public ServiceLocation getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(ServiceLocation serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public TypePayment getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(TypePayment typePayment) {
        this.typePayment = typePayment;
    }
}
