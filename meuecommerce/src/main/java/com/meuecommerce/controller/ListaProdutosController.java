package com.meuecommerce.controller;

import com.meuecommerce.dao.ProdutoDAO;
import com.meuecommerce.model.Produto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Controller
public class ListaProdutosController {

    @Autowired
    private ProdutoDAO produtoDAO;

    @GetMapping("/produtos")
    public String listarProdutos(HttpSession session, Model model,
                                 @RequestParam(required = false) String error,
                                 @RequestParam(required = false) String success) {
        if (!"cliente".equals(session.getAttribute("userType")) &&
                !"storeOwner".equals(session.getAttribute("userType"))) {
            return "redirect:/login";
        }
        try {
            List<Produto> produtos = produtoDAO.findAll();
            model.addAttribute("produtos", produtos);
            if (error != null) {
                model.addAttribute("error", error);
            }
            if (success != null) {
                model.addAttribute("success", success);
            }
            return "lista-produtos";
        } catch (SQLException e) {
            model.addAttribute("error", "Erro ao listar produtos");
            return "lista-produtos";
        }
    }
}