package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaListPOJO {
    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("tag_list")
    private List<TagList> tagList = null;

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

    public List<TagList> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagList> tagList) {
        this.tagList = tagList;
    }

    public class TagList {

        @SerializedName("tag_id")
        private Integer tagId;
        @SerializedName("tag_name")
        private String tagName;
        @SerializedName("tag_type")
        private String tagType;
        @SerializedName("parent_tag_id")
        private Integer parentTagId;
        @SerializedName("parent_tag_name")
        private String parentTagName;
        @SerializedName("domain_id")
        private Integer domainId;
        @SerializedName("sub_domain_id")
        private Integer subDomainId;
        @SerializedName("children")
        private List<Child> children = null;

        public Integer getTagId() {
            return tagId;
        }

        public void setTagId(Integer tagId) {
            this.tagId = tagId;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getTagType() {
            return tagType;
        }

        public void setTagType(String tagType) {
            this.tagType = tagType;
        }

        public Integer getParentTagId() {
            return parentTagId;
        }

        public void setParentTagId(Integer parentTagId) {
            this.parentTagId = parentTagId;
        }

        public String getParentTagName() {
            return parentTagName;
        }

        public void setParentTagName(String parentTagName) {
            this.parentTagName = parentTagName;
        }

        public Integer getDomainId() {
            return domainId;
        }

        public void setDomainId(Integer domainId) {
            this.domainId = domainId;
        }

        public Integer getSubDomainId() {
            return subDomainId;
        }

        public void setSubDomainId(Integer subDomainId) {
            this.subDomainId = subDomainId;
        }

        public List<Child> getChildren() {
            return children;
        }

        public void setChildren(List<Child> children) {
            this.children = children;
        }
    }

    public class Child {

        @SerializedName("tag_id")
        private Integer tagId;
        @SerializedName("tag_name")
        private String tagName;
        @SerializedName("tag_type")
        private String tagType;
        @SerializedName("parent_tag_id")
        private Integer parentTagId;
        @SerializedName("parent_tag_name")
        private String parentTagName;
        @SerializedName("domain_id")
        private Integer domainId;
        @SerializedName("sub_domain_id")
        private Integer subDomainId;
        @SerializedName("children")
        private List<Object> children = null;

        public Integer getTagId() {
            return tagId;
        }

        public void setTagId(Integer tagId) {
            this.tagId = tagId;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getTagType() {
            return tagType;
        }

        public void setTagType(String tagType) {
            this.tagType = tagType;
        }

        public Integer getParentTagId() {
            return parentTagId;
        }

        public void setParentTagId(Integer parentTagId) {
            this.parentTagId = parentTagId;
        }

        public String getParentTagName() {
            return parentTagName;
        }

        public void setParentTagName(String parentTagName) {
            this.parentTagName = parentTagName;
        }

        public Integer getDomainId() {
            return domainId;
        }

        public void setDomainId(Integer domainId) {
            this.domainId = domainId;
        }

        public Integer getSubDomainId() {
            return subDomainId;
        }

        public void setSubDomainId(Integer subDomainId) {
            this.subDomainId = subDomainId;
        }

        public List<Object> getChildren() {
            return children;
        }

        public void setChildren(List<Object> children) {
            this.children = children;
        }
    }
}
