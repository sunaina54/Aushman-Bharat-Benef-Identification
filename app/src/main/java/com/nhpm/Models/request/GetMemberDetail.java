package com.nhpm.Models.request;

import com.google.gson.Gson;
import com.nhpm.Models.response.FamilyDetailResponse;
import com.nhpm.Models.response.PersonalDetailResponse;

import java.io.Serializable;

public class GetMemberDetail implements Serializable {
        private boolean status;
        private String operation;
        private  String errorMessage;
        private String ahl_tin;
        private String hhd_no;
        private Integer statecode;
        private PersonalDetailResponse personalDetail;
        private FamilyDetailResponse familyDetailsItem;

        public boolean isStatus() {
                return status;
        }

        public void setStatus(boolean status) {
                this.status = status;
        }

        public String getOperation() {
                return operation;
        }

        public void setOperation(String operation) {
                this.operation = operation;
        }

        public String getErrorMessage() {
                return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
        }

        public String getAhl_tin() {
                return ahl_tin;
        }

        public void setAhl_tin(String ahl_tin) {
                this.ahl_tin = ahl_tin;
        }

        public String getHhd_no() {
                return hhd_no;
        }

        public void setHhd_no(String hhd_no) {
                this.hhd_no = hhd_no;
        }

        public Integer getStatecode() {
                return statecode;
        }

        public void setStatecode(Integer statecode) {
                this.statecode = statecode;
        }

        public PersonalDetailResponse getPersonalDetail() {
                return personalDetail;
        }

        public void setPersonalDetail(PersonalDetailResponse personalDetail) {
                this.personalDetail = personalDetail;
        }

        public FamilyDetailResponse getFamilyDetailsItem() {
                return familyDetailsItem;
        }

        public void setFamilyDetailsItem(FamilyDetailResponse familyDetailsItem) {
                this.familyDetailsItem = familyDetailsItem;
        }

        static public GetMemberDetail create(String serializedData) {
                // Use GSON to instantiate this class using the JSON representation of the state
                Gson gson = new Gson();
                return gson.fromJson(serializedData, GetMemberDetail.class);
        }

        public String serialize() {
                // Serialize this class into a JSON string using GSON
                Gson gson = new Gson();
                return gson.toJson(this);
        }
}
