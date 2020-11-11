package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IntrestsBodyPOJO {
    @SerializedName("rowcode")
    private String rowcode;
    @SerializedName("result")
    private List<CommonInterestsPOJO.InterestsList> result = null;
}
