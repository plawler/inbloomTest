package com.springapp.mvc.dto;

/**
 * Created with IntelliJ IDEA.
 * User: paullawler
 * Date: 1/30/14
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Service {

    private String identifier;
    private String url;
    private String uniqueRealmIdentifier;

    public Service() {}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUniqueRealmIdentifier() {
        return uniqueRealmIdentifier;
    }

    public void setUniqueRealmIdentifier(String uniqueRealmIdentifier) {
        this.uniqueRealmIdentifier = uniqueRealmIdentifier;
    }

}
