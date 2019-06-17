
package com.virtual.customervendor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationClass implements Serializable {

    @SerializedName("subtitle")
    @Expose
    private String subtitle;

    @SerializedName("smallIcon")
    @Expose
    private String smallIcon;

    @SerializedName("subcategory_id")
    @Expose
    private String subcategoryId;


    @SerializedName("object_id")
    @Expose
    private String object_id;

    @SerializedName("from_id")
    @Expose
    private String fromId;

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("sound")
    @Expose
    private String sound;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("vibrate")
    @Expose
    private String vibrate;
    @SerializedName("largeIcon")
    @Expose
    private String largeIcon;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("busines_id")
    @Expose
    private String businesId;
    @SerializedName("tickerText")
    @Expose
    private String tickerText;
    @SerializedName("user_type")
    @Expose
    private String user_type;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("startdate")
    @Expose
    private String startdate;
    @SerializedName("endate")
    @Expose
    private String endate;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVibrate() {
        return vibrate;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBusinesId() {
        return businesId;
    }

    public void setBusinesId(String businesId) {
        this.businesId = businesId;
    }

    public String getTickerText() {
        return tickerText;
    }

    public void setTickerText(String tickerText) {
        this.tickerText = tickerText;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEndate() {
        return endate;
    }

    public void setEndate(String endate) {
        this.endate = endate;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    @Override
    public String toString() {
        return "NotificationClass{" +
                "subtitle='" + subtitle + '\'' +
                ", smallIcon='" + smallIcon + '\'' +
                ", subcategoryId='" + subcategoryId + '\'' +
                ", object_id='" + object_id + '\'' +
                ", fromId='" + fromId + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", sound='" + sound + '\'' +
                ", title='" + title + '\'' +
                ", vibrate='" + vibrate + '\'' +
                ", largeIcon='" + largeIcon + '\'' +
                ", message='" + message + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", businesId='" + businesId + '\'' +
                ", tickerText='" + tickerText + '\'' +
                ", user_type='" + user_type + '\'' +
                ", image='" + image + '\'' +
                ", discount='" + discount + '\'' +
                ", startdate='" + startdate + '\'' +
                ", endate='" + endate + '\'' +
                '}';
    }
}
