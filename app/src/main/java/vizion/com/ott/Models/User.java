package vizion.com.ott.Models;

public class User {

    public User() {
    }

    private String uid;
    private String name;
    private String email;
    private double coinCard;
    private String notiToken;
    private String avatar;
    private String histories;
    private double wins;
    private double loses;
    private String socketId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getCoinCard() {
        return coinCard;
    }

    public void setCoinCard(double coinCard) {
        this.coinCard = coinCard;
    }

    public String getNotiToken() {
        return notiToken;
    }

    public void setNotiToken(String notiToken) {
        this.notiToken = notiToken;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHistories() {
        return histories;
    }

    public void setHistories(String histories) {
        this.histories = histories;
    }

    public double getWins() {
        return wins;
    }

    public void setWins(double wins) {
        this.wins = wins;
    }

    public double getLoses() {
        return loses;
    }

    public void setLoses(double loses) {
        this.loses = loses;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }
}
