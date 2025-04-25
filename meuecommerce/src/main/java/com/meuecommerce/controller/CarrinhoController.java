package com.meuecommerce.controller;

import com.meuecommerce.dao.ProdutoDAO;
import com.meuecommerce.model.Carrinho;
import com.meuecommerce.model.ItemCarrinho;
import com.meuecommerce.model.Produto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@Controller
@RequestMapping({"/carrinho", "/carrinho/add", "/carrinho/remove", "/finalizar"})
public class CarrinhoController {
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userType = (String) request.getSession().getAttribute("userType");
        if (userType == null || (!userType.equals("cliente") && !userType.equals("storeOwner"))) {
            response.sendRedirect("/login");
            return;
        }

        String path = request.getServletPath();
        if (path.equals("/carrinho/add") && request.getMethod().equals("GET")) {
            handleAddToCart(request, response);
        } else if (path.equals("/carrinho/remove") && request.getMethod().equals("GET")) {
            handleRemoveFromCart(request, response);
        } else if (path.equals("/finalizar") && request.getMethod().equals("POST")) {
            handleFinalizePurchase(request, response);
        } else {
            displayCart(request, response);
        }
    }

    private void displayCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String error = request.getParameter("error");
        String success = request.getParameter("success");
        HttpSession session = request.getSession();
        Carrinho carrinho = (Carrinho) session.getAttribute("cart");
        String userType = (String) session.getAttribute("userType");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Carrinho de Compras</title>");
        out.println("<script src='https://cdn.tailwindcss.com'></script>");
        out.println("</head>");
        out.println("<body class='bg-gray-100 min-h-screen p-6'>");
        out.println("<div class='max-w-4xl mx-auto bg-white p-8 rounded-lg shadow-md'>");
        out.println("<div class='flex justify-between items-center mb-6'>");
        out.println("<h2 class='text-2xl font-bold'>Carrinho de Compras</h2>");
        out.println("<div class='space-x-4'>");
        out.println("<a href='/produtos' class='text-indigo-600 hover:underline'>Produtos</a>");
        if ("storeOwner".equals(userType)) {
            out.println("<a href='/lojista/produtos' class='text-indigo-600 hover:underline'>Gerenciar Produtos</a>");
        }
        out.println("<a href='/logout' class='text-red-600 hover:underline'>Sair</a>");
        out.println("</div>");
        out.println("</div>");
        if (error != null) {
            out.println("<div class='bg-red-100 text-red-700 p-4 rounded mb-4'>" + error + "</div>");
        }
        if (success != null) {
            out.println("<div class='bg-green-100 text-green-700 p-4 rounded mb-4'>" + success + "</div>");
        }

        if (carrinho == null || carrinho.getItems().isEmpty()) {
            out.println("<p class='text-center text-gray-600'>Seu carrinho está vazio.</p>");
            out.println("<div class='mt-4 text-center'>");
            out.println("<a href='/produtos' class='text-indigo-600 hover:underline'>Continuar comprando</a>");
            out.println("</div>");
        } else {
            out.println("<table class='w-full border-collapse'>");
            out.println("<thead><tr class='bg-gray-200'>");
            out.println("<th class='p-3 text-left'>Produto</th>");
            out.println("<th class='p-3 text-left'>Preço</th>");
            out.println("<th class='p-3 text-left'>Quantidade</th>");
            out.println("<th class='p-3 text-left'>Subtotal</th>");
            out.println("<th class='p-3 text-left'>Ação</th>");
            out.println("</tr></thead>");
            out.println("<tbody>");
            for (ItemCarrinho item : carrinho.getItems()) {
                out.println("<tr class='border-b'>");
                out.println("<td class='p-3'>" + item.getProduto().getNome() + "</td>");
                out.println("<td class='p-3'>R$ " + String.format("%.2f", item.getProduto().getPreco()) + "</td>");
                out.println("<td class='p-3'>" + item.getQuantity() + "</td>");
                out.println("<td class='p-3'>R$ " + String.format("%.2f", item.getProduto().getPreco() * item.getQuantity()) + "</td>");
                out.println("<td class='p-3'><a href='/carrinho/remove?id=" + item.getProduto().getId() + "' class='text-red-600 hover:underline'>Remover</a></td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("<div class='mt-6 flex justify-between items-center'>");
            out.println("<p class='text-lg font-semibold'>Total: R$ " + String.format("%.2f", carrinho.getTotal()) + "</p>");
            out.println("<form action='/finalizar' method='post'>");
            out.println("<button type='submit' class='bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700'>Finalizar Compra</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("<div class='mt-4 text-center'>");
            out.println("<a href='/produtos' class='text-indigo-600 hover:underline'>Continuar comprando</a>");
            out.println("</div>");
        }

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("/produtos?error=ID%20do%20produto%20inválido");
            return;
        }

        try {
            Long id = Long.parseLong(idParam);
            Produto produto = produtoDAO.findById(id);
            if (produto == null || produto.getEstoque() <= 0) {
                response.sendRedirect("/produtos?error=Produto%20não%20encontrado%20ou%20sem%20estoque");
                return;
            }

            HttpSession session = request.getSession();
            Carrinho carrinho = (Carrinho) session.getAttribute("cart");
            if (carrinho == null) {
                carrinho = new Carrinho();
                session.setAttribute("cart", carrinho);
            }

            int quantidadeNoCarrinho = carrinho.getItems().stream()
                    .filter(item -> item.getProduto().getId().equals(id))
                    .mapToInt(ItemCarrinho::getQuantity)
                    .sum();
            if (quantidadeNoCarrinho >= produto.getEstoque()) {
                response.sendRedirect("/produtos?error=Estoque%20insuficiente%20para%20adicionar%20mais");
                return;
            }

            carrinho.addProduct(produto);
            response.sendRedirect("/produtos");
        } catch (NumberFormatException e) {
            response.sendRedirect("/produtos?error=ID%20do%20produto%20inválido");
        } catch (SQLException e) {
            response.sendRedirect("/produtos?error=Erro%20ao%20adicionar%20produto%20ao%20carrinho");
        }
    }

    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("/carrinho?error=ID%20do%20produto%20inválido");
            return;
        }

        try {
            Long id = Long.parseLong(idParam);
            HttpSession session = request.getSession();
            Carrinho carrinho = (Carrinho) session.getAttribute("cart");
            if (carrinho == null) {
                response.sendRedirect("/carrinho?error=Carrinho%20vazio");
                return;
            }

            carrinho.removeProduct(id);
            response.sendRedirect("/carrinho");
        } catch (NumberFormatException e) {
            response.sendRedirect("/carrinho?error=ID%20do%20produto%20inválido");
        }
    }

    private void handleFinalizePurchase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Carrinho carrinho = (Carrinho) session.getAttribute("cart");
        if (carrinho == null || carrinho.getItems().isEmpty()) {
            response.sendRedirect("/carrinho?error=Carrinho%20vazio");
            return;
        }

        try {
            for (ItemCarrinho item : carrinho.getItems()) {
                Produto produto = produtoDAO.findById(item.getProduto().getId());
                if (produto == null || produto.getEstoque() < item.getQuantity()) {
                    response.sendRedirect("/carrinho?error=Estoque%20insuficiente%20para%20um%20ou%20mais%20produtos");
                    return;
                }
                produtoDAO.updateEstoque(produto.getId(), produto.getEstoque() - item.getQuantity());
            }
            session.removeAttribute("cart");
            response.sendRedirect("/produtos?success=Compra%20finalizada%20com%20sucesso");
        } catch (SQLException e) {
            response.sendRedirect("/carrinho?error=Erro%20ao%20finalizar%20compra");
        }
    }
}