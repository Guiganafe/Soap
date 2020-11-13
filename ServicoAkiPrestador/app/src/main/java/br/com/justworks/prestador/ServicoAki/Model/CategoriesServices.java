package br.com.justworks.prestador.ServicoAki.Model;

public class CategoriesServices {
    private boolean active;
    private Description description;
    private ImageIconUrl imageIconUrl;
    private String imageUrl;
    private Name name;
    private int qtdServices;
    private int qtdUsers;

    public CategoriesServices() {
    }

    public CategoriesServices(boolean active, Description description, ImageIconUrl imageIconUrl, String imageUrl, Name name, int qtdServices, int qtdUsers) {
        this.active = active;
        this.description = description;
        this.imageIconUrl = imageIconUrl;
        this.imageUrl = imageUrl;
        this.name = name;
        this.qtdServices = qtdServices;
        this.qtdUsers = qtdUsers;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public ImageIconUrl getImageIconUrl() {
        return imageIconUrl;
    }

    public void setImageIconUrl(ImageIconUrl imageIconUrl) {
        this.imageIconUrl = imageIconUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public int getQtdServices() {
        return qtdServices;
    }

    public void setQtdServices(int qtdServices) {
        this.qtdServices = qtdServices;
    }

    public int getQtdUsers() {
        return qtdUsers;
    }

    public void setQtdUsers(int qtdUsers) {
        this.qtdUsers = qtdUsers;
    }
}
