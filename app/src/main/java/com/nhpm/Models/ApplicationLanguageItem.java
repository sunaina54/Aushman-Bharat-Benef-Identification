package com.nhpm.Models;

import java.io.Serializable;

/**
 * Created by Saurabh on 05-04-2017.
 */

public class ApplicationLanguageItem implements Serializable {

    public String languageCode;
    public String languageName;

    public ApplicationLanguageItem(String Code, String name) {
        this.languageCode = Code;
        this.languageName = name;
    }
}
