package com.meuecommerce.model;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private List<ItemCarrinho> itens = new ArrayList<>();

    public void adicionarProduto(Produto produto) {
        for (ItemCarrinho item : itens) {
            if (item.getProduto().getId().equals(produto.getId())) {
                item.setQuantidade(item.getQuantidade() + 1);
                return;
            }
        }
        itens.add(new ItemCarrinho(produto, 1));
    }

    public void removerProduto(Long produtoId) {
        itens.removeIf(item -> item.getProduto().getId().equals(produtoId));
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    public double getTotal() {
        return itens.stream().mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade()).sum();
    }
}