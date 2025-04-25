package com.meuecommerce.controller;

import com.meuecommerce.dao.ProdutoDAO;
import com.meuecommerce.model.Produto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class LojistaController {

    @Autowired
    private ProdutoDAO produtoDAO;

    @GetMapping("/lojista/produtos")
    public String listarProdutosLojista(HttpSession session, Model model,
                                        @RequestParam(required = false) String error,
                                        @RequestParam(required = false) String success) {
        if (!"storeOwner".equals(session.getAttribute("userType"))) {
            return "redirect:/login";
        }
        try {
            model.addAttribute("produtos", produtoDAO.findAll());
            if (error != null) {
                model.addAttribute("error", error);
            }
            if (success != null) {
                model.addAttribute("success", success);
            }
            return "lojista-produtos";
        } catch (SQLException e) {
            model.addAttribute("error", "Erro ao listar produtos");
            return "lojista-produtos";
        }
    }

    @GetMapping("/lojista/cadastro")
    public String cadastroProdutoForm(HttpSession session) {
        if (!"storeOwner".equals(session.getAttribute("userType"))) {
            return "redirect:/login";
        }
        return "cadastro-produto";
    }

    @PostMapping("/lojista/cadastro")
    public String cadastrarProduto(@RequestParam(required = false) String nome,
                                   @RequestParam(required = false) String descricao,
                                   @RequestParam(required = false) Double preco,
                                   @RequestParam(required = false) Integer estoque,
                                   HttpSession session, Model model) {
        if (!"storeOwner".equals(session.getAttribute("userType"))) {
            return "redirect:/login";
        }
        if (nome == null || nome.trim().isEmpty() ||
                descricao == null || descricao.trim().isEmpty() ||
                preco == null || preco <= 0 ||
                estoque == null || estoque < 0) {
            model.addAttribute("error", "Todos os campos são obrigatórios e devem ser válidos");
            return "cadastro-produto";
        }
        try {
            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setEstoque(estoque);
            produtoDAO.save(produto);
            return "redirect:/lojista/produtos?success=Produto%20cadastrado%20com%20sucesso";
        } catch (SQLException e) {
            model.addAttribute("error", "Erro ao salvar produto");
            return "cadastro-produto";
        }
    }
}