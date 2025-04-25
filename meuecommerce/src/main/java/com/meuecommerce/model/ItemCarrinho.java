package com.meuecommerce.model;

public class ItemCarrinho {
    private Produto produto;
    private int quantity;

    public ItemCarrinho(Produto produto, int quantity) {
        this.produto = produto;
        this.quantity = quantity;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}