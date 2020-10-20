package br.com.justworks.prestador.ServicoAki;

public enum Constants {
    KEY_EMAIL("email"),
    KEY_PHOTO_URI("imageUrl"),
    KEY_AUTENTICATED("isAuthenticated"),
    KEY_PROFESSIONAL("isProfessional");

    private String displayName;

    Constants(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return this.displayName;
    }
}
