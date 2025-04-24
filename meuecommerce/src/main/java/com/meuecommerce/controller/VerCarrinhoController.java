package com.meuecommerce.controller;

import com.meuecommerce.model.Carrinho;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VerCarrinhoController {

    @GetMapping("/carrinho")
    public String verCarrinho(HttpSession session, Model model) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new Carrinho();
            session.setAttribute("carrinho", carrinho);
        }
        model.addAttribute("carrinho", carrinho);
        return "ver-carrinho";
    }
}