package com.nhpm.Models.response.rsbyMembers;

import java.io.Serializable;

/**
 * Created by PSQ on 3/13/2017.
 */

public class RsbyCardCategoryItem implements Serializable {
   private String sno ;
   private String catCode;
   private String  categoryCode;
   private String  categoryName ;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCatName() {
        return categoryName;
    }

    public void setCatName(String catName) {
        this.categoryName = catName;
    }
}
