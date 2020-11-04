package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

public class BaseUrlPOJo {
    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("env")
    private String env;
    @SerializedName("api_url")
    private String apiUrl;
    @SerializedName("register_url")
    private String registerUrl;

    @SerializedName("android_version_name")
    private String androidVersionName;
    @SerializedName("android_version_code")
    private int androidVersionCode;
    public Boolean getOK() {
        return isOK;
    }

    public void setOK(Boolean OK) {
        isOK = OK;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getAndroidVersionName() {
        return androidVersionName;
    }

    public void setAndroidVersionName(String androidVersionName) {
        this.androidVersionName = androidVersionName;
    }

    public int getAndroidVersionCode() {
        return androidVersionCode;
    }

    public void setAndroidVersionCode(int androidVersionCode) {
        this.androidVersionCode = androidVersionCode;
    }
}
