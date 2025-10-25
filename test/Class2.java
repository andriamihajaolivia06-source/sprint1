package com.sprint1;

@Controller("/admin")
public class Class2 {

    @PathAnnotation("/users")
    public void listUsers() {}

    @PathAnnotation("/delete")
    public void deleteUser() {}
}
