package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonaListPOJO {
    @SerializedName("persona_id")
    private Integer personaId;
    @SerializedName("persona_name")
    private String personaName;
    @SerializedName("deleted")
    private Boolean deleted;
    @SerializedName("rowcode")
    private String rowcode;

    public Integer getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Integer personaId) {
        this.personaId = personaId;
    }

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getRowcode() {
        return rowcode;
    }

    public void setRowcode(String rowcode) {
        this.rowcode = rowcode;
    }
}
