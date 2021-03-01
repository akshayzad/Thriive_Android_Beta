package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeDisplayPOJO {

    @SerializedName("IsOK")
    @Expose
    private Boolean isOK;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("home_display_type")
    @Expose
    private String homeDisplayType;
    @SerializedName("meeting_template")
    @Expose
    private MeetingTemplate meetingTemplate;
    @SerializedName("meeting_data")
    @Expose
    private MeetingData meetingData;
    @SerializedName("entity_list")
    @Expose
    private List<EntityList> entityList = null;
    @SerializedName("push_token")
    @Expose
    private String pushToken;
    @SerializedName("featured_requests")
    @Expose
    private List<FeaturedRequest> featuredRequests = null;
    @SerializedName("top_title")
    @Expose
    private String top_title;
    @SerializedName("top_text")
    @Expose
    private String top_text;

    public Boolean getIsOK() {
        return isOK;
    }

    public void setIsOK(Boolean isOK) {
        this.isOK = isOK;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHomeDisplayType() {
        return homeDisplayType;
    }

    public void setHomeDisplayType(String homeDisplayType) {
        this.homeDisplayType = homeDisplayType;
    }

    public MeetingTemplate getMeetingTemplate() {
        return meetingTemplate;
    }

    public void setMeetingTemplate(MeetingTemplate meetingTemplate) {
        this.meetingTemplate = meetingTemplate;
    }

    public MeetingData getMeetingData() {
        return meetingData;
    }

    public void setMeetingData(MeetingData meetingData) {
        this.meetingData = meetingData;
    }

    public List<EntityList> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<EntityList> entityList) {
        this.entityList = entityList;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public List<FeaturedRequest> getFeaturedRequests() {
        return featuredRequests;
    }

    public void setFeaturedRequests(List<FeaturedRequest> featuredRequests) {
        this.featuredRequests = featuredRequests;
    }

    public String getTop_title() {
        return top_title;
    }

    public void setTop_title(String top_title) {
        this.top_title = top_title;
    }

    public String getTop_text() {
        return top_text;
    }

    public void setTop_text(String top_text) {
        this.top_text = top_text;
    }




    public class MeetingTemplate {

        @SerializedName("reason_id")
        @Expose
        private Integer reasonId;
        @SerializedName("reason_name")
        @Expose
        private String reasonName;
        @SerializedName("reason_rowcode")
        @Expose
        private String reasonRowcode;
        @SerializedName("l1_attrib_id")
        @Expose
        private Integer l1AttribId;
        @SerializedName("l1_attrib_name")
        @Expose
        private String l1AttribName;
        @SerializedName("giver_persona_id")
        @Expose
        private Integer giverPersonaId;
        @SerializedName("giver_persona_name")
        @Expose
        private String giverPersonaName;
        @SerializedName("l2_attrib_id")
        @Expose
        private Integer l2AttribId;
        @SerializedName("l2_attrib_name")
        @Expose
        private String l2AttribName;
        @SerializedName("domain_id")
        @Expose
        private Integer domainId;
        @SerializedName("domain_name")
        @Expose
        private String domainName;
        @SerializedName("sub_domain_id")
        @Expose
        private Integer subDomainId;
        @SerializedName("sub_domain_name")
        @Expose
        private String subDomainName;
        @SerializedName("expertise_id")
        @Expose
        private Integer expertiseId;
        @SerializedName("expertise_name")
        @Expose
        private String expertiseName;
        @SerializedName("flag_reason")
        @Expose
        private Boolean flagReason;
        @SerializedName("flag_l1")
        @Expose
        private Boolean flagL1;
        @SerializedName("flag_persona")
        @Expose
        private Boolean flagPersona;
        @SerializedName("flag_l2")
        @Expose
        private Boolean flagL2;
        @SerializedName("flag_domain")
        @Expose
        private Boolean flagDomain;
        @SerializedName("flag_expertise")
        @Expose
        private Boolean flagExpertise;
        @SerializedName("all_steps")
        @Expose
        private List<String> allSteps = null;
        @SerializedName("card_title")
        @Expose
        private String cardTitle;
        @SerializedName("card_text")
        @Expose
        private String cardText;

        public Integer getReasonId() {
            return reasonId;
        }

        public void setReasonId(Integer reasonId) {
            this.reasonId = reasonId;
        }

        public String getReasonName() {
            return reasonName;
        }

        public void setReasonName(String reasonName) {
            this.reasonName = reasonName;
        }

        public String getReasonRowcode() {
            return reasonRowcode;
        }

        public void setReasonRowcode(String reasonRowcode) {
            this.reasonRowcode = reasonRowcode;
        }

        public Integer getL1AttribId() {
            return l1AttribId;
        }

        public void setL1AttribId(Integer l1AttribId) {
            this.l1AttribId = l1AttribId;
        }

        public String getL1AttribName() {
            return l1AttribName;
        }

        public void setL1AttribName(String l1AttribName) {
            this.l1AttribName = l1AttribName;
        }

        public Integer getGiverPersonaId() {
            return giverPersonaId;
        }

        public void setGiverPersonaId(Integer giverPersonaId) {
            this.giverPersonaId = giverPersonaId;
        }

        public String getGiverPersonaName() {
            return giverPersonaName;
        }

        public void setGiverPersonaName(String giverPersonaName) {
            this.giverPersonaName = giverPersonaName;
        }

        public Integer getL2AttribId() {
            return l2AttribId;
        }

        public void setL2AttribId(Integer l2AttribId) {
            this.l2AttribId = l2AttribId;
        }

        public String getL2AttribName() {
            return l2AttribName;
        }

        public void setL2AttribName(String l2AttribName) {
            this.l2AttribName = l2AttribName;
        }

        public Integer getDomainId() {
            return domainId;
        }

        public void setDomainId(Integer domainId) {
            this.domainId = domainId;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }

        public Integer getSubDomainId() {
            return subDomainId;
        }

        public void setSubDomainId(Integer subDomainId) {
            this.subDomainId = subDomainId;
        }

        public String getSubDomainName() {
            return subDomainName;
        }

        public void setSubDomainName(String subDomainName) {
            this.subDomainName = subDomainName;
        }

        public Integer getExpertiseId() {
            return expertiseId;
        }

        public void setExpertiseId(Integer expertiseId) {
            this.expertiseId = expertiseId;
        }

        public String getExpertiseName() {
            return expertiseName;
        }

        public void setExpertiseName(String expertiseName) {
            this.expertiseName = expertiseName;
        }

        public Boolean getFlagReason() {
            return flagReason;
        }

        public void setFlagReason(Boolean flagReason) {
            this.flagReason = flagReason;
        }

        public Boolean getFlagL1() {
            return flagL1;
        }

        public void setFlagL1(Boolean flagL1) {
            this.flagL1 = flagL1;
        }

        public Boolean getFlagPersona() {
            return flagPersona;
        }

        public void setFlagPersona(Boolean flagPersona) {
            this.flagPersona = flagPersona;
        }

        public Boolean getFlagL2() {
            return flagL2;
        }

        public void setFlagL2(Boolean flagL2) {
            this.flagL2 = flagL2;
        }

        public Boolean getFlagDomain() {
            return flagDomain;
        }

        public void setFlagDomain(Boolean flagDomain) {
            this.flagDomain = flagDomain;
        }

        public Boolean getFlagExpertise() {
            return flagExpertise;
        }

        public void setFlagExpertise(Boolean flagExpertise) {
            this.flagExpertise = flagExpertise;
        }

        public List<String> getAllSteps() {
            return allSteps;
        }

        public void setAllSteps(List<String> allSteps) {
            this.allSteps = allSteps;
        }

        public String getCardTitle() {
            return cardTitle;
        }

        public void setCardTitle(String cardTitle) {
            this.cardTitle = cardTitle;
        }

        public String getCardText() {
            return cardText;
        }

        public void setCardText(String cardText) {
            this.cardText = cardText;
        }

    }



    public class MeetingData {

        @SerializedName("meeting_tag")
        @Expose
        private List<String> meetingTag = null;
        @SerializedName("meeting_id")
        @Expose
        private Integer meetingId;
        @SerializedName("meeting_token")
        @Expose
        private String meetingToken;
        @SerializedName("meeting_request_id")
        @Expose
        private Integer meetingRequestId;
        @SerializedName("meeting_code")
        @Expose
        private String meetingCode;
        @SerializedName("meeting_channel")
        @Expose
        private String meetingChannel;
        @SerializedName("meeting_reason")
        @Expose
        private String meetingReason;
        @SerializedName("meeting_domain")
        @Expose
        private String meetingDomain;
        @SerializedName("meeting_l1_attrib_name")
        @Expose
        private String meetingL1AttribName;
        @SerializedName("meeting_l2_attrib_name")
        @Expose
        private String meetingL2AttribName;
        @SerializedName("meeting_req_text")
        @Expose
        private String meetingReqText;
        @SerializedName("meeting_sub_domain")
        @Expose
        private String meetingSubDomain;
        @SerializedName("meeting_expertise")
        @Expose
        private String meetingExpertise;
        @SerializedName("plan_start_time")
        @Expose
        private String planStartTime;
        @SerializedName("plan_end_time")
        @Expose
        private String planEndTime;
        @SerializedName("giver_id")
        @Expose
        private Integer giverId;
        @SerializedName("giver_name")
        @Expose
        private String giverName;
        @SerializedName("giver_pic_url")
        @Expose
        private String giverPicUrl;
        @SerializedName("giver_email_id")
        @Expose
        private String giverEmailId;
        @SerializedName("giver_linkedin_url")
        @Expose
        private String giverLinkedinUrl;
        @SerializedName("giver_designation")
        @Expose
        private Object giverDesignation;
        @SerializedName("giver_company")
        @Expose
        private Object giverCompany;
        @SerializedName("giver_sub_title")
        @Expose
        private String giverSubTitle;
        @SerializedName("date_match")
        @Expose
        private String dateMatch;
        @SerializedName("meeting_label")
        @Expose
        private String meetingLabel;
        @SerializedName("giver_persona_tags")
        @Expose
        private List<String> giverPersonaTags = null;
        @SerializedName("giver_objective_tags")
        @Expose
        private List<Object> giverObjectiveTags = null;
        @SerializedName("giver_experience_tags")
        @Expose
        private List<String> giverExperienceTags = null;
        @SerializedName("giver_domain_tags")
        @Expose
        private List<String> giverDomainTags = null;
        @SerializedName("giver_sub_domain_tags")
        @Expose
        private List<String> giverSubDomainTags = null;
        @SerializedName("giver_designation_tags")
        @Expose
        private List<String> giverDesignationTags = null;
        @SerializedName("giver_expertise_tags")
        @Expose
        private List<String> giverExpertiseTags = null;
        @SerializedName("giver_response_int")
        @Expose
        private Integer giverResponseInt;
        @SerializedName("requestor_id")
        @Expose
        private Integer requestorId;
        @SerializedName("requestor_name")
        @Expose
        private String requestorName;
        @SerializedName("requestor_pic_url")
        @Expose
        private String requestorPicUrl;
        @SerializedName("requestor_email_id")
        @Expose
        private String requestorEmailId;
        @SerializedName("requestor_linkedin_url")
        @Expose
        private String requestorLinkedinUrl;
        @SerializedName("requestor_persona_tags")
        @Expose
        private List<String> requestorPersonaTags = null;
        @SerializedName("requestor_objective_tags")
        @Expose
        private List<Object> requestorObjectiveTags = null;
        @SerializedName("requestor_experience_tags")
        @Expose
        private List<String> requestorExperienceTags = null;
        @SerializedName("requestor_domain_tags")
        @Expose
        private List<String> requestorDomainTags = null;
        @SerializedName("requestor_sub_domain_tags")
        @Expose
        private List<String> requestorSubDomainTags = null;
        @SerializedName("requestor_designation_tags")
        @Expose
        private List<String> requestorDesignationTags = null;
        @SerializedName("requestor_expertise_tags")
        @Expose
        private List<String> requestorExpertiseTags = null;
        @SerializedName("requestor_response_int")
        @Expose
        private Integer requestorResponseInt;
        @SerializedName("requestor_designation")
        @Expose
        private Object requestorDesignation;
        @SerializedName("requestor_company")
        @Expose
        private Object requestorCompany;
        @SerializedName("requestor_sub_title")
        @Expose
        private String requestorSubTitle;
        @SerializedName("giver_country_name")
        @Expose
        private String giverCountryName;
        @SerializedName("requestor_country_name")
        @Expose
        private String requestorCountryName;
        @SerializedName("sel_meeting")
        @Expose
        private SelMeeting selMeeting;
        @SerializedName("slot_list")
        @Expose
        private List<SlotList> slotList = null;

        public List<String> getMeetingTag() {
            return meetingTag;
        }

        public void setMeetingTag(List<String> meetingTag) {
            this.meetingTag = meetingTag;
        }

        public Integer getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(Integer meetingId) {
            this.meetingId = meetingId;
        }

        public String getMeetingToken() {
            return meetingToken;
        }

        public void setMeetingToken(String meetingToken) {
            this.meetingToken = meetingToken;
        }

        public Integer getMeetingRequestId() {
            return meetingRequestId;
        }

        public void setMeetingRequestId(Integer meetingRequestId) {
            this.meetingRequestId = meetingRequestId;
        }

        public String getMeetingCode() {
            return meetingCode;
        }

        public void setMeetingCode(String meetingCode) {
            this.meetingCode = meetingCode;
        }

        public String getMeetingChannel() {
            return meetingChannel;
        }

        public void setMeetingChannel(String meetingChannel) {
            this.meetingChannel = meetingChannel;
        }

        public String getMeetingReason() {
            return meetingReason;
        }

        public void setMeetingReason(String meetingReason) {
            this.meetingReason = meetingReason;
        }

        public String getMeetingDomain() {
            return meetingDomain;
        }

        public void setMeetingDomain(String meetingDomain) {
            this.meetingDomain = meetingDomain;
        }

        public String getMeetingL1AttribName() {
            return meetingL1AttribName;
        }

        public void setMeetingL1AttribName(String meetingL1AttribName) {
            this.meetingL1AttribName = meetingL1AttribName;
        }

        public String getMeetingL2AttribName() {
            return meetingL2AttribName;
        }

        public void setMeetingL2AttribName(String meetingL2AttribName) {
            this.meetingL2AttribName = meetingL2AttribName;
        }

        public String getMeetingReqText() {
            return meetingReqText;
        }

        public void setMeetingReqText(String meetingReqText) {
            this.meetingReqText = meetingReqText;
        }

        public String getMeetingSubDomain() {
            return meetingSubDomain;
        }

        public void setMeetingSubDomain(String meetingSubDomain) {
            this.meetingSubDomain = meetingSubDomain;
        }

        public String getMeetingExpertise() {
            return meetingExpertise;
        }

        public void setMeetingExpertise(String meetingExpertise) {
            this.meetingExpertise = meetingExpertise;
        }

        public String getPlanStartTime() {
            return planStartTime;
        }

        public void setPlanStartTime(String planStartTime) {
            this.planStartTime = planStartTime;
        }

        public String getPlanEndTime() {
            return planEndTime;
        }

        public void setPlanEndTime(String planEndTime) {
            this.planEndTime = planEndTime;
        }

        public Integer getGiverId() {
            return giverId;
        }

        public void setGiverId(Integer giverId) {
            this.giverId = giverId;
        }

        public String getGiverName() {
            return giverName;
        }

        public void setGiverName(String giverName) {
            this.giverName = giverName;
        }

        public String getGiverPicUrl() {
            return giverPicUrl;
        }

        public void setGiverPicUrl(String giverPicUrl) {
            this.giverPicUrl = giverPicUrl;
        }

        public String getGiverEmailId() {
            return giverEmailId;
        }

        public void setGiverEmailId(String giverEmailId) {
            this.giverEmailId = giverEmailId;
        }

        public String getGiverLinkedinUrl() {
            return giverLinkedinUrl;
        }

        public void setGiverLinkedinUrl(String giverLinkedinUrl) {
            this.giverLinkedinUrl = giverLinkedinUrl;
        }

        public Object getGiverDesignation() {
            return giverDesignation;
        }

        public void setGiverDesignation(Object giverDesignation) {
            this.giverDesignation = giverDesignation;
        }

        public Object getGiverCompany() {
            return giverCompany;
        }

        public void setGiverCompany(Object giverCompany) {
            this.giverCompany = giverCompany;
        }

        public String getGiverSubTitle() {
            return giverSubTitle;
        }

        public void setGiverSubTitle(String giverSubTitle) {
            this.giverSubTitle = giverSubTitle;
        }

        public String getDateMatch() {
            return dateMatch;
        }

        public void setDateMatch(String dateMatch) {
            this.dateMatch = dateMatch;
        }

        public String getMeetingLabel() {
            return meetingLabel;
        }

        public void setMeetingLabel(String meetingLabel) {
            this.meetingLabel = meetingLabel;
        }

        public List<String> getGiverPersonaTags() {
            return giverPersonaTags;
        }

        public void setGiverPersonaTags(List<String> giverPersonaTags) {
            this.giverPersonaTags = giverPersonaTags;
        }

        public List<Object> getGiverObjectiveTags() {
            return giverObjectiveTags;
        }

        public void setGiverObjectiveTags(List<Object> giverObjectiveTags) {
            this.giverObjectiveTags = giverObjectiveTags;
        }

        public List<String> getGiverExperienceTags() {
            return giverExperienceTags;
        }

        public void setGiverExperienceTags(List<String> giverExperienceTags) {
            this.giverExperienceTags = giverExperienceTags;
        }

        public List<String> getGiverDomainTags() {
            return giverDomainTags;
        }

        public void setGiverDomainTags(List<String> giverDomainTags) {
            this.giverDomainTags = giverDomainTags;
        }

        public List<String> getGiverSubDomainTags() {
            return giverSubDomainTags;
        }

        public void setGiverSubDomainTags(List<String> giverSubDomainTags) {
            this.giverSubDomainTags = giverSubDomainTags;
        }

        public List<String> getGiverDesignationTags() {
            return giverDesignationTags;
        }

        public void setGiverDesignationTags(List<String> giverDesignationTags) {
            this.giverDesignationTags = giverDesignationTags;
        }

        public List<String> getGiverExpertiseTags() {
            return giverExpertiseTags;
        }

        public void setGiverExpertiseTags(List<String> giverExpertiseTags) {
            this.giverExpertiseTags = giverExpertiseTags;
        }

        public Integer getGiverResponseInt() {
            return giverResponseInt;
        }

        public void setGiverResponseInt(Integer giverResponseInt) {
            this.giverResponseInt = giverResponseInt;
        }

        public Integer getRequestorId() {
            return requestorId;
        }

        public void setRequestorId(Integer requestorId) {
            this.requestorId = requestorId;
        }

        public String getRequestorName() {
            return requestorName;
        }

        public void setRequestorName(String requestorName) {
            this.requestorName = requestorName;
        }

        public String getRequestorPicUrl() {
            return requestorPicUrl;
        }

        public void setRequestorPicUrl(String requestorPicUrl) {
            this.requestorPicUrl = requestorPicUrl;
        }

        public String getRequestorEmailId() {
            return requestorEmailId;
        }

        public void setRequestorEmailId(String requestorEmailId) {
            this.requestorEmailId = requestorEmailId;
        }

        public String getRequestorLinkedinUrl() {
            return requestorLinkedinUrl;
        }

        public void setRequestorLinkedinUrl(String requestorLinkedinUrl) {
            this.requestorLinkedinUrl = requestorLinkedinUrl;
        }

        public List<String> getRequestorPersonaTags() {
            return requestorPersonaTags;
        }

        public void setRequestorPersonaTags(List<String> requestorPersonaTags) {
            this.requestorPersonaTags = requestorPersonaTags;
        }

        public List<Object> getRequestorObjectiveTags() {
            return requestorObjectiveTags;
        }

        public void setRequestorObjectiveTags(List<Object> requestorObjectiveTags) {
            this.requestorObjectiveTags = requestorObjectiveTags;
        }

        public List<String> getRequestorExperienceTags() {
            return requestorExperienceTags;
        }

        public void setRequestorExperienceTags(List<String> requestorExperienceTags) {
            this.requestorExperienceTags = requestorExperienceTags;
        }

        public List<String> getRequestorDomainTags() {
            return requestorDomainTags;
        }

        public void setRequestorDomainTags(List<String> requestorDomainTags) {
            this.requestorDomainTags = requestorDomainTags;
        }

        public List<String> getRequestorSubDomainTags() {
            return requestorSubDomainTags;
        }

        public void setRequestorSubDomainTags(List<String> requestorSubDomainTags) {
            this.requestorSubDomainTags = requestorSubDomainTags;
        }

        public List<String> getRequestorDesignationTags() {
            return requestorDesignationTags;
        }

        public void setRequestorDesignationTags(List<String> requestorDesignationTags) {
            this.requestorDesignationTags = requestorDesignationTags;
        }

        public List<String> getRequestorExpertiseTags() {
            return requestorExpertiseTags;
        }

        public void setRequestorExpertiseTags(List<String> requestorExpertiseTags) {
            this.requestorExpertiseTags = requestorExpertiseTags;
        }

        public Integer getRequestorResponseInt() {
            return requestorResponseInt;
        }

        public void setRequestorResponseInt(Integer requestorResponseInt) {
            this.requestorResponseInt = requestorResponseInt;
        }

        public Object getRequestorDesignation() {
            return requestorDesignation;
        }

        public void setRequestorDesignation(Object requestorDesignation) {
            this.requestorDesignation = requestorDesignation;
        }

        public Object getRequestorCompany() {
            return requestorCompany;
        }

        public void setRequestorCompany(Object requestorCompany) {
            this.requestorCompany = requestorCompany;
        }

        public String getRequestorSubTitle() {
            return requestorSubTitle;
        }

        public void setRequestorSubTitle(String requestorSubTitle) {
            this.requestorSubTitle = requestorSubTitle;
        }

        public String getGiverCountryName() {
            return giverCountryName;
        }

        public void setGiverCountryName(String giverCountryName) {
            this.giverCountryName = giverCountryName;
        }

        public String getRequestorCountryName() {
            return requestorCountryName;
        }

        public void setRequestorCountryName(String requestorCountryName) {
            this.requestorCountryName = requestorCountryName;
        }

        public SelMeeting getSelMeeting() {
            return selMeeting;
        }

        public void setSelMeeting(SelMeeting selMeeting) {
            this.selMeeting = selMeeting;
        }

        public List<SlotList> getSlotList() {
            return slotList;
        }

        public void setSlotList(List<SlotList> slotList) {
            this.slotList = slotList;
        }

    }


    public class SelMeeting {

        @SerializedName("meeting_id")
        @Expose
        private Integer meetingId;
        @SerializedName("meeting_code")
        @Expose
        private String meetingCode;
        @SerializedName("requestor_id")
        @Expose
        private Integer requestorId;
        @SerializedName("giver_id")
        @Expose
        private Integer giverId;
        @SerializedName("requestor_persona_id")
        @Expose
        private Integer requestorPersonaId;
        @SerializedName("giver_persona_id")
        @Expose
        private Integer giverPersonaId;
        @SerializedName("reason_id")
        @Expose
        private Integer reasonId;
        @SerializedName("l1_attrib_id")
        @Expose
        private Integer l1AttribId;
        @SerializedName("l2_attrib_id")
        @Expose
        private Integer l2AttribId;
        @SerializedName("domain_id")
        @Expose
        private Integer domainId;
        @SerializedName("sub_domain_id")
        @Expose
        private Integer subDomainId;
        @SerializedName("expertise_id")
        @Expose
        private Integer expertiseId;
        @SerializedName("country_id")
        @Expose
        private Integer countryId;
        @SerializedName("req_text")
        @Expose
        private String reqText;
        @SerializedName("requestor_name")
        @Expose
        private String requestorName;
        @SerializedName("giver_name")
        @Expose
        private String giverName;
        @SerializedName("requestor_persona_name")
        @Expose
        private String requestorPersonaName;
        @SerializedName("giver_persona_name")
        @Expose
        private String giverPersonaName;
        @SerializedName("reason_name")
        @Expose
        private String reasonName;
        @SerializedName("l1_attrib_name")
        @Expose
        private String l1AttribName;
        @SerializedName("l2_attrib_name")
        @Expose
        private String l2AttribName;
        @SerializedName("domain_name")
        @Expose
        private String domainName;
        @SerializedName("sub_domain_name")
        @Expose
        private String subDomainName;
        @SerializedName("expertise_name")
        @Expose
        private String expertiseName;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("request_date")
        @Expose
        private String requestDate;
        @SerializedName("flag_matched")
        @Expose
        private Boolean flagMatched;
        @SerializedName("flag_rejected")
        @Expose
        private Boolean flagRejected;
        @SerializedName("flag_accepted")
        @Expose
        private Boolean flagAccepted;
        @SerializedName("flag_expired")
        @Expose
        private Boolean flagExpired;
        @SerializedName("flag_giver_prop_time")
        @Expose
        private Boolean flagGiverPropTime;
        @SerializedName("flag_req_accept_prop_time")
        @Expose
        private Boolean flagReqAcceptPropTime;
        @SerializedName("flag_req_prop_expired")
        @Expose
        private Boolean flagReqPropExpired;
        @SerializedName("flag_rescheduled")
        @Expose
        private Boolean flagRescheduled;
        @SerializedName("flag_cancelled")
        @Expose
        private Boolean flagCancelled;
        @SerializedName("flag_done")
        @Expose
        private Boolean flagDone;
        @SerializedName("cancel_reason")
        @Expose
        private String cancelReason;
        @SerializedName("cancelled_by")
        @Expose
        private Integer cancelledBy;
        @SerializedName("reschedule_reason")
        @Expose
        private String rescheduleReason;
        @SerializedName("rescheduled_by")
        @Expose
        private Integer rescheduledBy;
        @SerializedName("date_matched")
        @Expose
        private String dateMatched;
        @SerializedName("date_rejected")
        @Expose
        private String dateRejected;
        @SerializedName("date_accepted")
        @Expose
        private String dateAccepted;
        @SerializedName("date_expired")
        @Expose
        private String dateExpired;
        @SerializedName("date_giver_prop_time")
        @Expose
        private String dateGiverPropTime;
        @SerializedName("date_req_accept_prop_time")
        @Expose
        private String dateReqAcceptPropTime;
        @SerializedName("date_req_prop_expired")
        @Expose
        private String dateReqPropExpired;
        @SerializedName("date_rescheduled")
        @Expose
        private String dateRescheduled;
        @SerializedName("date_cancelled")
        @Expose
        private String dateCancelled;
        @SerializedName("date_done")
        @Expose
        private String dateDone;
        @SerializedName("plan_start_time")
        @Expose
        private String planStartTime;
        @SerializedName("plan_end_time")
        @Expose
        private String planEndTime;
        @SerializedName("meeting_channel")
        @Expose
        private String meetingChannel;
        @SerializedName("meeting_token")
        @Expose
        private String meetingToken;
        @SerializedName("flag_reminder_sent")
        @Expose
        private Boolean flagReminderSent;
        @SerializedName("flag_support_requested")
        @Expose
        private Boolean flagSupportRequested;
        @SerializedName("date_support_requested")
        @Expose
        private String dateSupportRequested;
        @SerializedName("deleted")
        @Expose
        private Boolean deleted;
        @SerializedName("rowcode")
        @Expose
        private String rowcode;
        @SerializedName("flag_scheduled")
        @Expose
        private Boolean flagScheduled;

        public Integer getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(Integer meetingId) {
            this.meetingId = meetingId;
        }

        public String getMeetingCode() {
            return meetingCode;
        }

        public void setMeetingCode(String meetingCode) {
            this.meetingCode = meetingCode;
        }

        public Integer getRequestorId() {
            return requestorId;
        }

        public void setRequestorId(Integer requestorId) {
            this.requestorId = requestorId;
        }

        public Integer getGiverId() {
            return giverId;
        }

        public void setGiverId(Integer giverId) {
            this.giverId = giverId;
        }

        public Integer getRequestorPersonaId() {
            return requestorPersonaId;
        }

        public void setRequestorPersonaId(Integer requestorPersonaId) {
            this.requestorPersonaId = requestorPersonaId;
        }

        public Integer getGiverPersonaId() {
            return giverPersonaId;
        }

        public void setGiverPersonaId(Integer giverPersonaId) {
            this.giverPersonaId = giverPersonaId;
        }

        public Integer getReasonId() {
            return reasonId;
        }

        public void setReasonId(Integer reasonId) {
            this.reasonId = reasonId;
        }

        public Integer getL1AttribId() {
            return l1AttribId;
        }

        public void setL1AttribId(Integer l1AttribId) {
            this.l1AttribId = l1AttribId;
        }

        public Integer getL2AttribId() {
            return l2AttribId;
        }

        public void setL2AttribId(Integer l2AttribId) {
            this.l2AttribId = l2AttribId;
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

        public Integer getExpertiseId() {
            return expertiseId;
        }

        public void setExpertiseId(Integer expertiseId) {
            this.expertiseId = expertiseId;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public String getReqText() {
            return reqText;
        }

        public void setReqText(String reqText) {
            this.reqText = reqText;
        }

        public String getRequestorName() {
            return requestorName;
        }

        public void setRequestorName(String requestorName) {
            this.requestorName = requestorName;
        }

        public String getGiverName() {
            return giverName;
        }

        public void setGiverName(String giverName) {
            this.giverName = giverName;
        }

        public String getRequestorPersonaName() {
            return requestorPersonaName;
        }

        public void setRequestorPersonaName(String requestorPersonaName) {
            this.requestorPersonaName = requestorPersonaName;
        }

        public String getGiverPersonaName() {
            return giverPersonaName;
        }

        public void setGiverPersonaName(String giverPersonaName) {
            this.giverPersonaName = giverPersonaName;
        }

        public String getReasonName() {
            return reasonName;
        }

        public void setReasonName(String reasonName) {
            this.reasonName = reasonName;
        }

        public String getL1AttribName() {
            return l1AttribName;
        }

        public void setL1AttribName(String l1AttribName) {
            this.l1AttribName = l1AttribName;
        }

        public String getL2AttribName() {
            return l2AttribName;
        }

        public void setL2AttribName(String l2AttribName) {
            this.l2AttribName = l2AttribName;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }

        public String getSubDomainName() {
            return subDomainName;
        }

        public void setSubDomainName(String subDomainName) {
            this.subDomainName = subDomainName;
        }

        public String getExpertiseName() {
            return expertiseName;
        }

        public void setExpertiseName(String expertiseName) {
            this.expertiseName = expertiseName;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public Boolean getFlagMatched() {
            return flagMatched;
        }

        public void setFlagMatched(Boolean flagMatched) {
            this.flagMatched = flagMatched;
        }

        public Boolean getFlagRejected() {
            return flagRejected;
        }

        public void setFlagRejected(Boolean flagRejected) {
            this.flagRejected = flagRejected;
        }

        public Boolean getFlagAccepted() {
            return flagAccepted;
        }

        public void setFlagAccepted(Boolean flagAccepted) {
            this.flagAccepted = flagAccepted;
        }

        public Boolean getFlagExpired() {
            return flagExpired;
        }

        public void setFlagExpired(Boolean flagExpired) {
            this.flagExpired = flagExpired;
        }

        public Boolean getFlagGiverPropTime() {
            return flagGiverPropTime;
        }

        public void setFlagGiverPropTime(Boolean flagGiverPropTime) {
            this.flagGiverPropTime = flagGiverPropTime;
        }

        public Boolean getFlagReqAcceptPropTime() {
            return flagReqAcceptPropTime;
        }

        public void setFlagReqAcceptPropTime(Boolean flagReqAcceptPropTime) {
            this.flagReqAcceptPropTime = flagReqAcceptPropTime;
        }

        public Boolean getFlagReqPropExpired() {
            return flagReqPropExpired;
        }

        public void setFlagReqPropExpired(Boolean flagReqPropExpired) {
            this.flagReqPropExpired = flagReqPropExpired;
        }

        public Boolean getFlagRescheduled() {
            return flagRescheduled;
        }

        public void setFlagRescheduled(Boolean flagRescheduled) {
            this.flagRescheduled = flagRescheduled;
        }

        public Boolean getFlagCancelled() {
            return flagCancelled;
        }

        public void setFlagCancelled(Boolean flagCancelled) {
            this.flagCancelled = flagCancelled;
        }

        public Boolean getFlagDone() {
            return flagDone;
        }

        public void setFlagDone(Boolean flagDone) {
            this.flagDone = flagDone;
        }

        public String getCancelReason() {
            return cancelReason;
        }

        public void setCancelReason(String cancelReason) {
            this.cancelReason = cancelReason;
        }

        public Integer getCancelledBy() {
            return cancelledBy;
        }

        public void setCancelledBy(Integer cancelledBy) {
            this.cancelledBy = cancelledBy;
        }

        public String getRescheduleReason() {
            return rescheduleReason;
        }

        public void setRescheduleReason(String rescheduleReason) {
            this.rescheduleReason = rescheduleReason;
        }

        public Integer getRescheduledBy() {
            return rescheduledBy;
        }

        public void setRescheduledBy(Integer rescheduledBy) {
            this.rescheduledBy = rescheduledBy;
        }

        public String getDateMatched() {
            return dateMatched;
        }

        public void setDateMatched(String dateMatched) {
            this.dateMatched = dateMatched;
        }

        public String getDateRejected() {
            return dateRejected;
        }

        public void setDateRejected(String dateRejected) {
            this.dateRejected = dateRejected;
        }

        public String getDateAccepted() {
            return dateAccepted;
        }

        public void setDateAccepted(String dateAccepted) {
            this.dateAccepted = dateAccepted;
        }

        public String getDateExpired() {
            return dateExpired;
        }

        public void setDateExpired(String dateExpired) {
            this.dateExpired = dateExpired;
        }

        public String getDateGiverPropTime() {
            return dateGiverPropTime;
        }

        public void setDateGiverPropTime(String dateGiverPropTime) {
            this.dateGiverPropTime = dateGiverPropTime;
        }

        public String getDateReqAcceptPropTime() {
            return dateReqAcceptPropTime;
        }

        public void setDateReqAcceptPropTime(String dateReqAcceptPropTime) {
            this.dateReqAcceptPropTime = dateReqAcceptPropTime;
        }

        public String getDateReqPropExpired() {
            return dateReqPropExpired;
        }

        public void setDateReqPropExpired(String dateReqPropExpired) {
            this.dateReqPropExpired = dateReqPropExpired;
        }

        public String getDateRescheduled() {
            return dateRescheduled;
        }

        public void setDateRescheduled(String dateRescheduled) {
            this.dateRescheduled = dateRescheduled;
        }

        public String getDateCancelled() {
            return dateCancelled;
        }

        public void setDateCancelled(String dateCancelled) {
            this.dateCancelled = dateCancelled;
        }

        public String getDateDone() {
            return dateDone;
        }

        public void setDateDone(String dateDone) {
            this.dateDone = dateDone;
        }

        public String getPlanStartTime() {
            return planStartTime;
        }

        public void setPlanStartTime(String planStartTime) {
            this.planStartTime = planStartTime;
        }

        public String getPlanEndTime() {
            return planEndTime;
        }

        public void setPlanEndTime(String planEndTime) {
            this.planEndTime = planEndTime;
        }

        public String getMeetingChannel() {
            return meetingChannel;
        }

        public void setMeetingChannel(String meetingChannel) {
            this.meetingChannel = meetingChannel;
        }

        public String getMeetingToken() {
            return meetingToken;
        }

        public void setMeetingToken(String meetingToken) {
            this.meetingToken = meetingToken;
        }

        public Boolean getFlagReminderSent() {
            return flagReminderSent;
        }

        public void setFlagReminderSent(Boolean flagReminderSent) {
            this.flagReminderSent = flagReminderSent;
        }

        public Boolean getFlagSupportRequested() {
            return flagSupportRequested;
        }

        public void setFlagSupportRequested(Boolean flagSupportRequested) {
            this.flagSupportRequested = flagSupportRequested;
        }

        public String getDateSupportRequested() {
            return dateSupportRequested;
        }

        public void setDateSupportRequested(String dateSupportRequested) {
            this.dateSupportRequested = dateSupportRequested;
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

        public Boolean getFlagScheduled() {
            return flagScheduled;
        }

        public void setFlagScheduled(Boolean flagScheduled) {
            this.flagScheduled = flagScheduled;
        }

    }



    public class SlotList {

        @SerializedName("meeting_slot_id")
        @Expose
        private Integer meetingSlotId;
        @SerializedName("meeting_id")
        @Expose
        private Integer meetingId;
        @SerializedName("proposer_id")
        @Expose
        private Integer proposerId;
        @SerializedName("proposer_name")
        @Expose
        private String proposerName;
        @SerializedName("requestor_id")
        @Expose
        private Integer requestorId;
        @SerializedName("requestor_name")
        @Expose
        private String requestorName;
        @SerializedName("giver_id")
        @Expose
        private Integer giverId;
        @SerializedName("giver_name")
        @Expose
        private String giverName;
        @SerializedName("flag_requestor")
        @Expose
        private Boolean flagRequestor;
        @SerializedName("flag_entry_by_support")
        @Expose
        private Boolean flagEntryBySupport;
        @SerializedName("slot_from_date")
        @Expose
        private String slotFromDate;
        @SerializedName("slot_to_date")
        @Expose
        private String slotToDate;
        @SerializedName("flag_slot_accepted")
        @Expose
        private Boolean flagSlotAccepted;
        @SerializedName("flag_slot_rejected")
        @Expose
        private Boolean flagSlotRejected;
        @SerializedName("slot_day")
        @Expose
        private Integer slotDay;
        @SerializedName("flag_cancelled")
        @Expose
        private Boolean flagCancelled;
        @SerializedName("flag_rescheduled")
        @Expose
        private Boolean flagRescheduled;
        @SerializedName("deleted")
        @Expose
        private Boolean deleted;
        @SerializedName("rowcode")
        @Expose
        private String rowcode;

        public Integer getMeetingSlotId() {
            return meetingSlotId;
        }

        public void setMeetingSlotId(Integer meetingSlotId) {
            this.meetingSlotId = meetingSlotId;
        }

        public Integer getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(Integer meetingId) {
            this.meetingId = meetingId;
        }

        public Integer getProposerId() {
            return proposerId;
        }

        public void setProposerId(Integer proposerId) {
            this.proposerId = proposerId;
        }

        public String getProposerName() {
            return proposerName;
        }

        public void setProposerName(String proposerName) {
            this.proposerName = proposerName;
        }

        public Integer getRequestorId() {
            return requestorId;
        }

        public void setRequestorId(Integer requestorId) {
            this.requestorId = requestorId;
        }

        public String getRequestorName() {
            return requestorName;
        }

        public void setRequestorName(String requestorName) {
            this.requestorName = requestorName;
        }

        public Integer getGiverId() {
            return giverId;
        }

        public void setGiverId(Integer giverId) {
            this.giverId = giverId;
        }

        public String getGiverName() {
            return giverName;
        }

        public void setGiverName(String giverName) {
            this.giverName = giverName;
        }

        public Boolean getFlagRequestor() {
            return flagRequestor;
        }

        public void setFlagRequestor(Boolean flagRequestor) {
            this.flagRequestor = flagRequestor;
        }

        public Boolean getFlagEntryBySupport() {
            return flagEntryBySupport;
        }

        public void setFlagEntryBySupport(Boolean flagEntryBySupport) {
            this.flagEntryBySupport = flagEntryBySupport;
        }

        public String getSlotFromDate() {
            return slotFromDate;
        }

        public void setSlotFromDate(String slotFromDate) {
            this.slotFromDate = slotFromDate;
        }

        public String getSlotToDate() {
            return slotToDate;
        }

        public void setSlotToDate(String slotToDate) {
            this.slotToDate = slotToDate;
        }

        public Boolean getFlagSlotAccepted() {
            return flagSlotAccepted;
        }

        public void setFlagSlotAccepted(Boolean flagSlotAccepted) {
            this.flagSlotAccepted = flagSlotAccepted;
        }

        public Boolean getFlagSlotRejected() {
            return flagSlotRejected;
        }

        public void setFlagSlotRejected(Boolean flagSlotRejected) {
            this.flagSlotRejected = flagSlotRejected;
        }

        public Integer getSlotDay() {
            return slotDay;
        }

        public void setSlotDay(Integer slotDay) {
            this.slotDay = slotDay;
        }

        public Boolean getFlagCancelled() {
            return flagCancelled;
        }

        public void setFlagCancelled(Boolean flagCancelled) {
            this.flagCancelled = flagCancelled;
        }

        public Boolean getFlagRescheduled() {
            return flagRescheduled;
        }

        public void setFlagRescheduled(Boolean flagRescheduled) {
            this.flagRescheduled = flagRescheduled;
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



    public class EntityList {

        @SerializedName("entity_name")
        @Expose
        private String entityName;
        @SerializedName("designation_name")
        @Expose
        private String designationName;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("pic_url")
        @Expose
        private String picUrl;
        @SerializedName("expertise_list")
        @Expose
        private List<String> expertiseList = null;
        @SerializedName("domain_list")
        @Expose
        private List<String> domainList = null;

        public String getEntityName() {
            return entityName;
        }

        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }

        public String getDesignationName() {
            return designationName;
        }

        public void setDesignationName(String designationName) {
            this.designationName = designationName;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public List<String> getExpertiseList() {
            return expertiseList;
        }

        public void setExpertiseList(List<String> expertiseList) {
            this.expertiseList = expertiseList;
        }

        public List<String> getDomainList() {
            return domainList;
        }

        public void setDomainList(List<String> domainList) {
            this.domainList = domainList;
        }

    }



    public class FeaturedRequest {

        @SerializedName("reason_id")
        @Expose
        private Integer reasonId;
        @SerializedName("reason_name")
        @Expose
        private String reasonName;
        @SerializedName("reason_rowcode")
        @Expose
        private String reasonRowcode;
        @SerializedName("l1_attrib_id")
        @Expose
        private Integer l1AttribId;
        @SerializedName("l1_attrib_name")
        @Expose
        private String l1AttribName;
        @SerializedName("giver_persona_id")
        @Expose
        private Integer giverPersonaId;
        @SerializedName("giver_persona_name")
        @Expose
        private String giverPersonaName;
        @SerializedName("l2_attrib_id")
        @Expose
        private Integer l2AttribId;
        @SerializedName("l2_attrib_name")
        @Expose
        private String l2AttribName;
        @SerializedName("domain_id")
        @Expose
        private Integer domainId;
        @SerializedName("domain_name")
        @Expose
        private String domainName;
        @SerializedName("sub_domain_id")
        @Expose
        private Integer subDomainId;
        @SerializedName("sub_domain_name")
        @Expose
        private String subDomainName;
        @SerializedName("expertise_id")
        @Expose
        private Integer expertiseId;
        @SerializedName("expertise_name")
        @Expose
        private String expertiseName;
        @SerializedName("flag_reason")
        @Expose
        private Boolean flagReason;
        @SerializedName("flag_l1")
        @Expose
        private Boolean flagL1;
        @SerializedName("flag_persona")
        @Expose
        private Boolean flagPersona;
        @SerializedName("flag_l2")
        @Expose
        private Boolean flagL2;
        @SerializedName("flag_domain")
        @Expose
        private Boolean flagDomain;
        @SerializedName("flag_expertise")
        @Expose
        private Boolean flagExpertise;
        @SerializedName("all_steps")
        @Expose
        private List<String> allSteps = null;
        @SerializedName("card_title")
        @Expose
        private String cardTitle;
        @SerializedName("card_text")
        @Expose
        private String cardText;

        public Integer getReasonId() {
            return reasonId;
        }

        public void setReasonId(Integer reasonId) {
            this.reasonId = reasonId;
        }

        public String getReasonName() {
            return reasonName;
        }

        public void setReasonName(String reasonName) {
            this.reasonName = reasonName;
        }

        public String getReasonRowcode() {
            return reasonRowcode;
        }

        public void setReasonRowcode(String reasonRowcode) {
            this.reasonRowcode = reasonRowcode;
        }

        public Integer getL1AttribId() {
            return l1AttribId;
        }

        public void setL1AttribId(Integer l1AttribId) {
            this.l1AttribId = l1AttribId;
        }

        public String getL1AttribName() {
            return l1AttribName;
        }

        public void setL1AttribName(String l1AttribName) {
            this.l1AttribName = l1AttribName;
        }

        public Integer getGiverPersonaId() {
            return giverPersonaId;
        }

        public void setGiverPersonaId(Integer giverPersonaId) {
            this.giverPersonaId = giverPersonaId;
        }

        public String getGiverPersonaName() {
            return giverPersonaName;
        }

        public void setGiverPersonaName(String giverPersonaName) {
            this.giverPersonaName = giverPersonaName;
        }

        public Integer getL2AttribId() {
            return l2AttribId;
        }

        public void setL2AttribId(Integer l2AttribId) {
            this.l2AttribId = l2AttribId;
        }

        public String getL2AttribName() {
            return l2AttribName;
        }

        public void setL2AttribName(String l2AttribName) {
            this.l2AttribName = l2AttribName;
        }

        public Integer getDomainId() {
            return domainId;
        }

        public void setDomainId(Integer domainId) {
            this.domainId = domainId;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }

        public Integer getSubDomainId() {
            return subDomainId;
        }

        public void setSubDomainId(Integer subDomainId) {
            this.subDomainId = subDomainId;
        }

        public String getSubDomainName() {
            return subDomainName;
        }

        public void setSubDomainName(String subDomainName) {
            this.subDomainName = subDomainName;
        }

        public Integer getExpertiseId() {
            return expertiseId;
        }

        public void setExpertiseId(Integer expertiseId) {
            this.expertiseId = expertiseId;
        }

        public String getExpertiseName() {
            return expertiseName;
        }

        public void setExpertiseName(String expertiseName) {
            this.expertiseName = expertiseName;
        }

        public Boolean getFlagReason() {
            return flagReason;
        }

        public void setFlagReason(Boolean flagReason) {
            this.flagReason = flagReason;
        }

        public Boolean getFlagL1() {
            return flagL1;
        }

        public void setFlagL1(Boolean flagL1) {
            this.flagL1 = flagL1;
        }

        public Boolean getFlagPersona() {
            return flagPersona;
        }

        public void setFlagPersona(Boolean flagPersona) {
            this.flagPersona = flagPersona;
        }

        public Boolean getFlagL2() {
            return flagL2;
        }

        public void setFlagL2(Boolean flagL2) {
            this.flagL2 = flagL2;
        }

        public Boolean getFlagDomain() {
            return flagDomain;
        }

        public void setFlagDomain(Boolean flagDomain) {
            this.flagDomain = flagDomain;
        }

        public Boolean getFlagExpertise() {
            return flagExpertise;
        }

        public void setFlagExpertise(Boolean flagExpertise) {
            this.flagExpertise = flagExpertise;
        }

        public List<String> getAllSteps() {
            return allSteps;
        }

        public void setAllSteps(List<String> allSteps) {
            this.allSteps = allSteps;
        }

        public String getCardTitle() {
            return cardTitle;
        }

        public void setCardTitle(String cardTitle) {
            this.cardTitle = cardTitle;
        }

        public String getCardText() {
            return cardText;
        }

        public void setCardText(String cardText) {
            this.cardText = cardText;
        }

    }


}
