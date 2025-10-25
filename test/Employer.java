package com.sprint1;

public class Employer {

    @PathAnnotation("/employer")
    public String getDetails() {
        return "Employer details";
    }
}
