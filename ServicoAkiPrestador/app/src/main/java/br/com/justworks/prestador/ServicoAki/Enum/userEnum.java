package br.com.justworks.prestador.ServicoAki.Enum;

public enum userEnum {

    USER_ADDRESS("addres"),
    USER_BIRTHDATE("birthDate"),
    USER_BIRTHDATE_TIMESTAMP("birthDateTimestamp"),
    USER_CIVIL_STATE("civilState"),
    USER_DISPATICHING_AGENCY("dispatchingAgency"),
    USER_DOCUMENT_SELFIE("documentSelfie"),
    USER_EMAIL("email"),
    USER_EMISSION_DATE("emissionDate"),
    USER_GOVERNMENT_ID("governmentId"),
    USER_DOC_FRONT_IMAGE("idFrontImage"),
    USER_DOC_BACK_IMAGE("idBackImage"),
    USER_IDENTIFY_DOCUMENT("identifyDocument"),
    USER_IMAGE_URL("imageUrl"),
    USER_IS_AUTHENTICATED("isAuthenticated"),
    USER_IS_NEW("isNew"),
    USER_IS_PROFESSIONAL("isProfessional"),
    USER_MOTHER_NAME("motherName"),
    USER_NAME("name"),
    USER_PHONE("phoneNumber"),
    USER_PROF_ADDRESS_IMAGE("proofOfAddressImage"),
    USER_SERVICES("services"),
    USER_SEXO("sex");

    private String displayName;

    userEnum(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
