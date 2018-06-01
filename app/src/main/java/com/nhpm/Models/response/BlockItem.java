package com.nhpm.Models.response;

import java.io.Serializable;

/**
 * Created by Anand on 19-10-2016.
 */
public class BlockItem implements Serializable {
    private String blockCode,blockName,subBlockCode;

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getSubBlockCode() {
        return subBlockCode;
    }

    public void setSubBlockCode(String subBlockCode) {
        this.subBlockCode = subBlockCode;
    }
}
