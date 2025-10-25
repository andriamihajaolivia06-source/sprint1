package com.sprint1;

@Controller("/api")
public class Class1 {

    @PathAnnotation("/hello")
    public void sayHello() {}

    @PathAnnotation("/bye")
    public void sayBye() {}
}
