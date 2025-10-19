package com.sprint1;

import jakarta.ws.rs.Path;

@Path("/employer")
public class Employer {
    
    public String getDetails() {
        return "Employer details";
    }
}