package com.nhpm.Models.response;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResult implements Serializable{
    private ArrayList<DocsListItem> docs;
    private String numFound;
    private String start;

    public ArrayList<DocsListItem> getDocs() {
        return docs;
    }

    public void setDocs(ArrayList<DocsListItem> docs) {
        this.docs = docs;
    }

    public String getNumFound() {
        return numFound;
    }

    public void setNumFound(String numFound) {
        this.numFound = numFound;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
