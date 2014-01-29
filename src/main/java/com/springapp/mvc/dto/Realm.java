package com.springapp.mvc.dto;

/**
 * Created with IntelliJ IDEA.
 * User: paullawler
 * Date: 1/29/14
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Realm {

    private String identifier;
    private String name;

    public Realm() {}

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

}
