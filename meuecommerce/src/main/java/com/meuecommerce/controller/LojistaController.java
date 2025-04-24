package com.meuecommerce.controller;

import com.meuecommerce.model.Produto;
import com.meuecommerce.repository.ProdutoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LojistaController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/lojista/produtos")
    public String listarProdutosLojista(HttpSession session, Model model) {
        if (!"lojista".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }
        model.addAttribute("produtos", produtoRepository.findAll());
        return "lojista-produtos";
    }

    @GetMapping("/lojista/cadastro")
    public String exibirFormularioCadastroProduto(HttpSession session) {
        if (!"lojista".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }
        return "cadastro-produto";
    }

    @PostMapping("/lojista/cadastro")
    public String cadastrarProduto(@RequestParam String nome, @RequestParam String descricao,
                                   @RequestParam double preco, @RequestParam int estoque, HttpSession session) {
        if (!"lojista".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/login";
        }
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setEstoque(estoque);
        produtoRepository.save(produto);
        return "redirect:/lojista/produtos";
    }
}