package br.com.justworks.prestador.ServicoAki.Enum;

public enum userEnum {

    USER_NAME("name"),
    USER_EMAIL("email"),
    USER_IMAGEURL("imageUrl"),
    USER_PHONE("phoneNumber"),
    USER_IS_AUTHENTICATED("isAuthenticated"),
    USER_IS_PROFESSIONAL("isProfessional");

    private String displayName;

    userEnum(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
