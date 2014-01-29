package com.springapp.mvc.student;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created with IntelliJ IDEA.
 * User: paullawler
 * Date: 11/6/13
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class Address {

    private String nameOfCounty;
    private String apartmentRoomSuiteNumber;
    private String streetNumberName;
    private String postalCode;
    private String stateAbbreviation;
    private String addressType;
    private String city;

    public String getStreetNumberName() {
        return streetNumberName;
    }

    public String getCity() {
        return city;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public String getApartmentRoomSuiteNumber() {
        return apartmentRoomSuiteNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

}
