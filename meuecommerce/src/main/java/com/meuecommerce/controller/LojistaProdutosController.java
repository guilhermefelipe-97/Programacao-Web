package com.meuecommerce.controller;

import com.meuecommerce.dao.ProdutoDAO;
import com.meuecommerce.model.Produto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/lojista/produtos")
public class LojistaProdutosController {
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @RequestMapping(method = RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userType = (String) request.getSession().getAttribute("userType");
        if (!"storeOwner".equals(userType)) {
            response.sendRedirect("/login");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String error = request.getParameter("error");
        String success = request.getParameter("success");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Produtos - Lojista</title>");
        out.println("<script src='https://cdn.tailwindcss.com'></script>");
        out.println("</head>");
        out.println("<body class='bg-gray-100 min-h-screen p-6'>");
        out.println("<div class='max-w-5xl mx-auto bg-white p-8 rounded-lg shadow-md'>");
        out.println("<div class='flex justify-between items-center mb-6'>");
        out.println("<h2 class='text-2xl font-bold'>Produtos Cadastrados</h2>");
        out.println("<div class='space-x-4'>");
        out.println("<a href='/lojista/cadastro' class='bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700'>Cadastrar Novo Produto</a>");
        out.println("<a href='/logout' class='text-red-600 hover:underline'>Sair</a>");
        out.println("</div>");
        out.println("</div>");
        if (error != null) {
            out.println("<div class='bg-red-100 text-red-700 p-4 rounded mb-4'>" + error + "</div>");
        }
        if (success != null) {
            out.println("<div class='bg-green-100 text-green-700 p-4 rounded mb-4'>" + success + "</div>");
        }

        try {
            List<Produto> produtos = produtoDAO.findAll();
            if (produtos.isEmpty()) {
                out.println("<p class='text-center text-gray-600'>Nenhum produto cadastrado.</p>");
            } else {
                out.println("<table class='w-full border-collapse'>");
                out.println("<thead><tr class='bg-gray-200'>");
                out.println("<th class='p-3 text-left'>Nome</th>");
                out.println("<th class='p-3 text-left'>Descrição</th>");
                out.println("<th class='p-3 text-left'>Preço</th>");
                out.println("<th class='p-3 text-left'>Estoque</th>");
                out.println("</tr></thead>");
                out.println("<tbody>");
                for (Produto produto : produtos) {
                    out.println("<tr class='border-b'>");
                    out.println("<td class='p-3'>" + produto.getNome() + "</td>");
                    out.println("<td class='p-3'>" + produto.getDescricao() + "</td>");
                    out.println("<td class='p-3'>R$ " + String.format("%.2f", produto.getPreco()) + "</td>");
                    out.println("<td class='p-3'>" + produto.getEstoque() + "</td>");
                    out.println("</tr>");
                }
                out.println("</tbody>");
                out.println("</table>");
            }
        } catch (SQLException e) {
            out.println("<div class='bg-red-100 text-red-700 p-4 rounded mb-4'>Erro ao listar produtos</div>");
        }

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}