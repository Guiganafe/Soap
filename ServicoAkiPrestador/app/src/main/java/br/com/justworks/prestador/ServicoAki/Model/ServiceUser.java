package br.com.justworks.prestador.ServicoAki.Model;

import java.util.ArrayList;

public class ServiceUser {
    private int avgExecutionTime;
    private ArrayList<CategoriesServices> category;
    private Description description;
    private String id;
    private boolean moveToClient;
    private String movementCost;
    private Name name;
    private boolean offersMeterial;
    private String price;

    public ServiceUser() {
    }

    public ServiceUser(int avgExecutionTime, ArrayList<CategoriesServices> category, Description description, String id, boolean moveToClient, String movementCost, Name name, boolean offersMeterial, String price) {
        this.avgExecutionTime = avgExecutionTime;
        this.category = category;
        this.description = description;
        this.id = id;
        this.moveToClient = moveToClient;
        this.movementCost = movementCost;
        this.name = name;
        this.offersMeterial = offersMeterial;
        this.price = price;
    }

    public int getAvgExecutionTime() {
        return avgExecutionTime;
    }

    public void setAvgExecutionTime(int avgExecutionTime) {
        this.avgExecutionTime = avgExecutionTime;
    }

    public ArrayList<CategoriesServices> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<CategoriesServices> category) {
        this.category = category;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMoveToClient() {
        return moveToClient;
    }

    public void setMoveToClient(boolean moveToClient) {
        this.moveToClient = moveToClient;
    }

    public String getMovementCost() {
        return movementCost;
    }

    public void setMovementCost(String movementCost) {
        this.movementCost = movementCost;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public boolean isOffersMeterial() {
        return offersMeterial;
    }

    public void setOffersMeterial(boolean offersMeterial) {
        this.offersMeterial = offersMeterial;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}