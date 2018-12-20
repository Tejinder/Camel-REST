package com.cleverbuilder.cameldemos.restdsl.beans;

import com.cleverbuilder.cameldemos.restdsl.types.FilePostRequestType;
import org.springframework.stereotype.Component;

@Component
public class FileBean {

    public String response(FilePostRequestType input) {
        return "Thanks for your post, " + input.getFile().getName() + "!";

        // Do some further processing here
    }
}
