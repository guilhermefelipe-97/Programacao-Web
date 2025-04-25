package com.meuecommerce.model;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private List<ItemCarrinho> items = new ArrayList<>();

    public void addProduct(Produto produto) {
        for (ItemCarrinho item : items) {
            if (item.getProduto().getId().equals(produto.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new ItemCarrinho(produto, 1));
    }

    public void removeProduct(Long productId) {
        items.removeIf(item -> item.getProduto().getId().equals(productId));
    }

    public List<ItemCarrinho> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrinho> items) {
        this.items = items;
    }

    public double getTotal() {
        return items.stream().mapToDouble(item -> item.getProduto().getPreco() * item.getQuantity()).sum();
    }
}