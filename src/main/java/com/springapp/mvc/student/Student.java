package com.springapp.mvc.student;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: paullawler
 * Date: 11/5/13
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class Student {

    private String id;
    private String sex;
    private boolean hispanicLatinoEthnicity;
    private Name name;

    @JsonProperty(value = "address")
    private List<Address> addresses;

    public String getId() {
        return id;
    }

    public String getSex() {
        return sex;
    }

    public String getFullName() {
        return name.getFullName();
    }

    public List<Address> getAddresses() {
        return addresses;
    }

}
