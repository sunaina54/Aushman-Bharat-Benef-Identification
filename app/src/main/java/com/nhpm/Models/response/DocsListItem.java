package com.nhpm.Models.response;

import com.google.gson.Gson;
import com.nhpm.Models.request.FamilyDetailsItemModel;
import com.nhpm.Models.request.PersonalDetailItem;

import java.io.Serializable;

/**
 * Created by SUNAINA on 31-05-2018.
 */
public class DocsListItem implements Serializable {
    private PersonalDetailItem personalDetail;
    private FamilyDetailsItemModel familyDetailsItemModel;
    private String LastModifiedDate;
    private String block_name_english;
    private String wardid;
    private String relation;
    private String ahlblockno;
    private String vt_name;
    private String state_name;
    private String d_2;
    private String d_1;
    private String d_4;
    private String d_3;
    private String mothername;
    private String d_5;
    private String id;
    private String hhd_no;
    private String districtcode;
    private String d_7;
    private String ahlslnohhd;
    private String pincode;
    private String state_name_english;
    private String suggestions_words;
    private String ahl_tin;
    private String relation_sl;
    private String name_sl;
    private String towncode_mdds;
    private String dob;
    private String district_code;
    private String tehsilcode;
    private String name;
    private String addressline3;
    private String addressline2;
    private String addressline3sl;
    private String addressline1;
    private String addressline5sl;
    private String state_code;
    private String district_name_english;
    private String addressline5;
    private String addressline4;
    private String addressline1sl;
    private String slnomember;
    private String hoh;
    private String mstatusid;
    private String genderid;
    private String statecode;
    private String fathername_combined;
    private String ahlsubblockno;
    private String district_name;
    private String fathername_sl;
    private String towncode;
    private String mothername_sl;
    private String rural_urban;
    private String i_1;
    private String caste_group;
    private String i_3;
    private String i_2;
    private String tehsil_name;
    private String i_5;
    private String i_4;
    private String village_code;
    private String mothername_combined;
    private String name_combined;
    private String tin_npr;
    private String fathername;
    private String _version_;
    private String addressline4sl;
    private String block_code;
    private String village_name_english;
    private String grampanchayatname;
    private String incomesource_urban;
    private String addressline2sl;

    public PersonalDetailItem getPersonalDetail() {
        return personalDetail;
    }

    public FamilyDetailsItemModel getFamilyDetailsItemModel() {
        return familyDetailsItemModel;
    }

    public void setFamilyDetailsItemModel(FamilyDetailsItemModel familyDetailsItemModel) {
        this.familyDetailsItemModel = familyDetailsItemModel;
    }

    public void setPersonalDetail(PersonalDetailItem personalDetail) {
        this.personalDetail = personalDetail;
    }

    public String getLastModifiedDate() {
        return LastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        LastModifiedDate = lastModifiedDate;
    }

    public String getBlock_name_english() {
        return block_name_english;
    }

    public void setBlock_name_english(String block_name_english) {
        this.block_name_english = block_name_english;
    }

    public String getWardid() {
        return wardid;
    }

    public void setWardid(String wardid) {
        this.wardid = wardid;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getAhlblockno() {
        return ahlblockno;
    }

    public void setAhlblockno(String ahlblockno) {
        this.ahlblockno = ahlblockno;
    }

    public String getVt_name() {
        return vt_name;
    }

    public void setVt_name(String vt_name) {
        this.vt_name = vt_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getD_2() {
        return d_2;
    }

    public void setD_2(String d_2) {
        this.d_2 = d_2;
    }

    public String getD_1() {
        return d_1;
    }

    public void setD_1(String d_1) {
        this.d_1 = d_1;
    }

    public String getD_4() {
        return d_4;
    }

    public void setD_4(String d_4) {
        this.d_4 = d_4;
    }

    public String getD_3() {
        return d_3;
    }

    public void setD_3(String d_3) {
        this.d_3 = d_3;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getD_5() {
        return d_5;
    }

    public void setD_5(String d_5) {
        this.d_5 = d_5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHhd_no() {
        return hhd_no;
    }

    public void setHhd_no(String hhd_no) {
        this.hhd_no = hhd_no;
    }

    public String getDistrictcode() {
        return districtcode;
    }

    public void setDistrictcode(String districtcode) {
        this.districtcode = districtcode;
    }

    public String getD_7() {
        return d_7;
    }

    public void setD_7(String d_7) {
        this.d_7 = d_7;
    }

    public String getAhlslnohhd() {
        return ahlslnohhd;
    }

    public void setAhlslnohhd(String ahlslnohhd) {
        this.ahlslnohhd = ahlslnohhd;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState_name_english() {
        return state_name_english;
    }

    public void setState_name_english(String state_name_english) {
        this.state_name_english = state_name_english;
    }

    public String getSuggestions_words() {
        return suggestions_words;
    }

    public void setSuggestions_words(String suggestions_words) {
        this.suggestions_words = suggestions_words;
    }

    public String getAhl_tin() {
        return ahl_tin;
    }

    public void setAhl_tin(String ahl_tin) {
        this.ahl_tin = ahl_tin;
    }

    public String getRelation_sl() {
        return relation_sl;
    }

    public void setRelation_sl(String relation_sl) {
        this.relation_sl = relation_sl;
    }

    public String getName_sl() {
        return name_sl;
    }

    public void setName_sl(String name_sl) {
        this.name_sl = name_sl;
    }

    public String getTowncode_mdds() {
        return towncode_mdds;
    }

    public void setTowncode_mdds(String towncode_mdds) {
        this.towncode_mdds = towncode_mdds;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getTehsilcode() {
        return tehsilcode;
    }

    public void setTehsilcode(String tehsilcode) {
        this.tehsilcode = tehsilcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressline3() {
        return addressline3;
    }

    public void setAddressline3(String addressline3) {
        this.addressline3 = addressline3;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getAddressline3sl() {
        return addressline3sl;
    }

    public void setAddressline3sl(String addressline3sl) {
        this.addressline3sl = addressline3sl;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline5sl() {
        return addressline5sl;
    }

    public void setAddressline5sl(String addressline5sl) {
        this.addressline5sl = addressline5sl;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getDistrict_name_english() {
        return district_name_english;
    }

    public void setDistrict_name_english(String district_name_english) {
        this.district_name_english = district_name_english;
    }

    public String getAddressline5() {
        return addressline5;
    }

    public void setAddressline5(String addressline5) {
        this.addressline5 = addressline5;
    }

    public String getAddressline4() {
        return addressline4;
    }

    public void setAddressline4(String addressline4) {
        this.addressline4 = addressline4;
    }

    public String getAddressline1sl() {
        return addressline1sl;
    }

    public void setAddressline1sl(String addressline1sl) {
        this.addressline1sl = addressline1sl;
    }

    public String getSlnomember() {
        return slnomember;
    }

    public void setSlnomember(String slnomember) {
        this.slnomember = slnomember;
    }

    public String getHoh() {
        return hoh;
    }

    public void setHoh(String hoh) {
        this.hoh = hoh;
    }

    public String getMstatusid() {
        return mstatusid;
    }

    public void setMstatusid(String mstatusid) {
        this.mstatusid = mstatusid;
    }

    public String getGenderid() {
        return genderid;
    }

    public void setGenderid(String genderid) {
        this.genderid = genderid;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getFathername_combined() {
        return fathername_combined;
    }

    public void setFathername_combined(String fathername_combined) {
        this.fathername_combined = fathername_combined;
    }

    public String getAhlsubblockno() {
        return ahlsubblockno;
    }

    public void setAhlsubblockno(String ahlsubblockno) {
        this.ahlsubblockno = ahlsubblockno;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getFathername_sl() {
        return fathername_sl;
    }

    public void setFathername_sl(String fathername_sl) {
        this.fathername_sl = fathername_sl;
    }

    public String getTowncode() {
        return towncode;
    }

    public void setTowncode(String towncode) {
        this.towncode = towncode;
    }

    public String getMothername_sl() {
        return mothername_sl;
    }

    public void setMothername_sl(String mothername_sl) {
        this.mothername_sl = mothername_sl;
    }

    public String getRural_urban() {
        return rural_urban;
    }

    public void setRural_urban(String rural_urban) {
        this.rural_urban = rural_urban;
    }

    public String getI_1() {
        return i_1;
    }

    public void setI_1(String i_1) {
        this.i_1 = i_1;
    }

    public String getCaste_group() {
        return caste_group;
    }

    public void setCaste_group(String caste_group) {
        this.caste_group = caste_group;
    }

    public String getI_3() {
        return i_3;
    }

    public void setI_3(String i_3) {
        this.i_3 = i_3;
    }

    public String getI_2() {
        return i_2;
    }

    public void setI_2(String i_2) {
        this.i_2 = i_2;
    }

    public String getTehsil_name() {
        return tehsil_name;
    }

    public void setTehsil_name(String tehsil_name) {
        this.tehsil_name = tehsil_name;
    }

    public String getI_5() {
        return i_5;
    }

    public void setI_5(String i_5) {
        this.i_5 = i_5;
    }

    public String getI_4() {
        return i_4;
    }

    public void setI_4(String i_4) {
        this.i_4 = i_4;
    }

    public String getVillage_code() {
        return village_code;
    }

    public void setVillage_code(String village_code) {
        this.village_code = village_code;
    }

    public String getMothername_combined() {
        return mothername_combined;
    }

    public void setMothername_combined(String mothername_combined) {
        this.mothername_combined = mothername_combined;
    }

    public String getName_combined() {
        return name_combined;
    }

    public void setName_combined(String name_combined) {
        this.name_combined = name_combined;
    }

    public String getTin_npr() {
        return tin_npr;
    }

    public void setTin_npr(String tin_npr) {
        this.tin_npr = tin_npr;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String get_version_() {
        return _version_;
    }

    public void set_version_(String _version_) {
        this._version_ = _version_;
    }

    public String getAddressline4sl() {
        return addressline4sl;
    }

    public void setAddressline4sl(String addressline4sl) {
        this.addressline4sl = addressline4sl;
    }

    public String getBlock_code() {
        return block_code;
    }

    public void setBlock_code(String block_code) {
        this.block_code = block_code;
    }

    public String getVillage_name_english() {
        return village_name_english;
    }

    public void setVillage_name_english(String village_name_english) {
        this.village_name_english = village_name_english;
    }

    public String getGrampanchayatname() {
        return grampanchayatname;
    }

    public void setGrampanchayatname(String grampanchayatname) {
        this.grampanchayatname = grampanchayatname;
    }

    public String getIncomesource_urban() {
        return incomesource_urban;
    }

    public void setIncomesource_urban(String incomesource_urban) {
        this.incomesource_urban = incomesource_urban;
    }

    public String getAddressline2sl() {
        return addressline2sl;
    }

    public void setAddressline2sl(String addressline2sl) {
        this.addressline2sl = addressline2sl;
    }


    static public DocsListItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, DocsListItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
