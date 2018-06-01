package com.nhpm.Models.response;

/**
 * Created by Saurabh on 27-07-2017.
 */

public class ApplicationConfigurationModel extends GenericResponse {


    private String stateCode;
    private String stateName;
    private String configId;
    private String configName;
    private String acceptedvalue;
    private String acceptedvalueName;


    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getAcceptedvalue() {
        return acceptedvalue;
    }

    public void setAcceptedvalue(String acceptedvalue) {
        this.acceptedvalue = acceptedvalue;
    }

    public String getAcceptedvalueName() {
        return acceptedvalueName;
    }

    public void setAcceptedvalueName(String acceptedvalueName) {
        this.acceptedvalueName = acceptedvalueName;
    }
}
