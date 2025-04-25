package com.meuecommerce.controller;

import com.meuecommerce.dao.ProdutoDAO;
import com.meuecommerce.model.Carrinho;
import com.meuecommerce.model.ItemCarrinho;
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
public class CarrinhoController {

    @Autowired
    private ProdutoDAO produtoDAO;

    @GetMapping("/carrinho/add")
    public String adicionarAoCarrinho(@RequestParam(required = false) Long id, HttpSession session, Model model) {
        if (id == null || id <= 0) {
            return "redirect:/produtos?error=ID%20do%20produto%20inválido";
        }
        try {
            Produto produto = produtoDAO.findById(id);
            if (produto == null || produto.getEstoque() <= 0) {
                return "redirect:/produtos?error=Produto%20não%20encontrado%20ou%20sem%20estoque";
            }
            Carrinho carrinho = (Carrinho) session.getAttribute("cart");
            if (carrinho == null) {
                carrinho = new Carrinho();
                session.setAttribute("cart", carrinho);
            }
            // Verificar se a quantidade no carrinho não excede o estoque
            int quantidadeNoCarrinho = carrinho.getItems().stream()
                    .filter(item -> item.getProduto().getId().equals(id))
                    .mapToInt(ItemCarrinho::getQuantity)
                    .sum();
            if (quantidadeNoCarrinho >= produto.getEstoque()) {
                return "redirect:/produtos?error=Estoque%20insuficiente%20para%20adicionar%20mais";
            }
            carrinho.addProduct(produto);
            return "redirect:/produtos";
        } catch (SQLException e) {
            return "redirect:/produtos?error=Erro%20ao%20adicionar%20produto%20ao%20carrinho";
        }
    }

    @GetMapping("/carrinho/remove")
    public String removerDoCarrinho(@RequestParam(required = false) Long id, HttpSession session, Model model) {
        if (id == null || id <= 0) {
            return "redirect:/carrinho?error=ID%20do%20produto%20inválido";
        }
        Carrinho carrinho = (Carrinho) session.getAttribute("cart");
        if (carrinho == null) {
            return "redirect:/carrinho?error=Carrinho%20vazio";
        }
        carrinho.removeProduct(id);
        return "redirect:/carrinho";
    }

    @PostMapping("/finalizar")
    public String finalizarCompra(HttpSession session, Model model) {
        try {
            Carrinho carrinho = (Carrinho) session.getAttribute("cart");
            if (carrinho == null || carrinho.getItems().isEmpty()) {
                return "redirect:/carrinho?error=Carrinho%20vazio";
            }
            for (ItemCarrinho item : carrinho.getItems()) {
                Produto produto = produtoDAO.findById(item.getProduto().getId());
                if (produto == null || produto.getEstoque() < item.getQuantity()) {
                    return "redirect:/carrinho?error=Estoque%20insuficiente%20para%20um%20ou%20mais%20produtos";
                }
                produtoDAO.updateEstoque(produto.getId(), produto.getEstoque() - item.getQuantity());
            }
            session.removeAttribute("cart");
            return "redirect:/produtos?success=Compra%20finalizada%20com%20sucesso";
        } catch (SQLException e) {
            return "redirect:/carrinho?error=Erro%20ao%20finalizar%20compra";
        }
    }
}