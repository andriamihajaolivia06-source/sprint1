package com.sprint1;

import jakarta.ws.rs.Path;


public class Employer {
    
    @Path("/employer")
    public String getDetails() {
        return "Employer details";
    }
}