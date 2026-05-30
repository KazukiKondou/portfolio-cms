package com.kondo.portfolio.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** /contact のフォーム */
public class ContactForm {

    @NotBlank(message = "お名前は必須です")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレスの形式が不正です")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "メッセージは必須です")
    @Size(max = 5000)
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
