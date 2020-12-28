package com.moshna.banners.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public void handleArgumentNotValidException(Exception ex, Model model) {
        model.addAttribute("validationMessage", ex.getMessage());
    }
}
