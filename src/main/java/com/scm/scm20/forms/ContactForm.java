package com.scm.scm20.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactForm {

    private String contactId;

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    @NotBlank(message = "Phone number is required")
    private String phone;

    private String address;
    private String profilePic;
    private String description;
}
