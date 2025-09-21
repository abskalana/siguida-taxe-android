package com.gouandiaka.market.entity;
import com.google.gson.annotations.SerializedName;

public class Paiement {

    @SerializedName("value")
    private int value;

    @SerializedName("ticket_num")
    private int ticketNum;

    @SerializedName("ticket_type")
    private String ticketType;

    @SerializedName("date")
    private String date;   // ou Date si tu veux parser avec Gson + DateFormat

    @SerializedName("status")
    private String status;

    @SerializedName("entity_model")
    private String entityModel;   // ForeignKey → en général, Django renvoie un ID

    @SerializedName("user")
    private int user;          // idem, ID utilisateur

    @SerializedName("coord")
    private String coord;

    @SerializedName("commentaire")
    private String commentaire;

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public int getTicketNum() { return ticketNum; }
    public void setTicketNum(int ticketNum) { this.ticketNum = ticketNum; }

    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }



    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEntityModel() { return entityModel; }
    public void setEntityModel(String entityModel) { this.entityModel = entityModel; }

    public int getUser() { return user; }
    public void setUser(int user) { this.user = user; }

    public String getCoord() { return coord; }
    public void setCoord(String coord) { this.coord = coord; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }


}
