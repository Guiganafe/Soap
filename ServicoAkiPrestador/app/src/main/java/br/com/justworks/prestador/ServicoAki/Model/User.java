package br.com.justworks.prestador.ServicoAki.Model;

public class User {
    private String email, imageUrl, name, phoneNumber;
    private Boolean isAuthenticated, isProfessional;

    public User() {
    }

    public User(String email, String imageUrl, String name, String phoneNumber, Boolean isAuthenticated, Boolean isProfessional) {
        this.email = email;
        this.imageUrl = imageUrl;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isAuthenticated = isAuthenticated;
        this.isProfessional = isProfessional;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public Boolean isProfessional() {
        return isProfessional;
    }

    public void setProfessional(Boolean professional) {
        isProfessional = professional;
    }
}
