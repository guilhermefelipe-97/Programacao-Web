package com.meuecommerce.controller;

import com.meuecommerce.model.Carrinho;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerCarrinhoController {

    @GetMapping("/carrinho")
    public String verCarrinho(HttpSession session, Model model,
                              @RequestParam(required = false) String error,
                              @RequestParam(required = false) String success) {
        if (!"cliente".equals(session.getAttribute("userType")) &&
                !"storeOwner".equals(session.getAttribute("userType"))) {
            return "redirect:/login";
        }
        Carrinho carrinho = (Carrinho) session.getAttribute("cart");
        if (carrinho == null) {
            model.addAttribute("error", "Carrinho vazio");
            return "redirect:/produtos";
        }
        model.addAttribute("cart", carrinho);
        model.addAttribute("user", session.getAttribute("user"));
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", success);
        }
        return "ver-carrinho";
    }
}