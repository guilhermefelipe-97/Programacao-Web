package com.meuecommerce.controller;

import com.meuecommerce.model.Carrinho;
import com.meuecommerce.model.ItemCarrinho;
import com.meuecommerce.model.Produto;
import com.meuecommerce.repository.ProdutoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarrinhoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/carrinho/adicionar")
    public String adicionarAoCarrinho(@RequestParam("id") Long id, HttpSession session) {
        Produto produto = produtoRepository.findById(id).orElse(null);
        if (produto != null && produto.getEstoque() > 0) {
            Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
            if (carrinho == null) {
                carrinho = new Carrinho();
                session.setAttribute("carrinho", carrinho);
            }
            carrinho.adicionarProduto(produto);
        }
        return "redirect:/produtos";
    }

    @GetMapping("/carrinho/remover")
    public String removerDoCarrinho(@RequestParam("id") Long id, HttpSession session) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho != null) {
            carrinho.removerProduto(id);
        }
        return "redirect:/carrinho";
    }

    @PostMapping("/carrinho/finalizar")
    public String finalizarCompra(HttpSession session) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho != null) {
            for (ItemCarrinho item : carrinho.getItens()) {
                Produto produto = produtoRepository.findById(item.getProduto().getId()).orElse(null);
                if (produto != null && produto.getEstoque() >= item.getQuantidade()) {
                    produto.setEstoque(produto.getEstoque() - item.getQuantidade());
                    produtoRepository.save(produto);
                }
            }
            session.removeAttribute("carrinho"); // Limpa o carrinho após a compra
        }
        return "redirect:/produtos";
    }
}
