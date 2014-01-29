package com.springapp.mvc.student;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created with IntelliJ IDEA.
 * User: paullawler
 * Date: 11/5/13
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class Name {

    private String firstName;
    private String lastSurname;

    public String getFullName() {
        return firstName + " " + lastSurname;
    }

}
