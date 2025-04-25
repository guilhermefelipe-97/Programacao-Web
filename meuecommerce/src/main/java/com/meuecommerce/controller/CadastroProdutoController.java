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

@Controller
@RequestMapping("/lojista/cadastro")
public class CadastroProdutoController {
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

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Cadastrar Produto</title>");
        out.println("<script src='https://cdn.tailwindcss.com'></script>");
        out.println("</head>");
        out.println("<body class='bg-gray-100 flex items-center justify-center min-h-screen'>");
        out.println("<div class='bg-white p-8 rounded-lg shadow-md w-full max-w-md'>");
        out.println("<h2 class='text-2xl font-bold mb-6 text-center'>Cadastrar Produto</h2>");
        out.println("<form action='/lojista/cadastro' method='post' class='space-y-4'>");
        out.println("<div>");
        out.println("<label for='nome' class='block text-sm font-medium text-gray-700'>Nome:</label>");
        out.println("<input type='text' id='nome' name='nome' required class='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'>");
        out.println("</div>");
        out.println("<div>");
        out.println("<label for='descricao' class='block text-sm font-medium text-gray-700'>Descrição:</label>");
        out.println("<textarea id='descricao' name='descricao' required class='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'></textarea>");
        out.println("</div>");
        out.println("<div>");
        out.println("<label for='preco' class='block text-sm font-medium text-gray-700'>Preço:</label>");
        out.println("<input type='number' id='preco' name='preco' step='0.01' min='0.01' required class='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'>");
        out.println("</div>");
        out.println("<div>");
        out.println("<label for='estoque' class='block text-sm font-medium text-gray-700'>Estoque:</label>");
        out.println("<input type='number' id='estoque' name='estoque' min='0' required class='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'>");
        out.println("</div>");
        out.println("<div>");
        out.println("<button type='submit' class='w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700'>Cadastrar</button>");
        out.println("</div>");
        if (error != null) {
            out.println("<div class='text-red-500 text-sm text-center'>" + error + "</div>");
        }
        out.println("</form>");
        out.println("<div class='mt-4 text-center space-x-4'>");
        out.println("<a href='/lojista/produtos' class='text-indigo-600 hover:underline'>Voltar</a>");
        out.println("<a href='/logout' class='text-red-600 hover:underline'>Sair</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @RequestMapping(method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userType = (String) request.getSession().getAttribute("userType");
        if (!"storeOwner".equals(userType)) {
            response.sendRedirect("/login");
            return;
        }

        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String precoParam = request.getParameter("preco");
        String estoqueParam = request.getParameter("estoque");

        if (nome == null || nome.trim().isEmpty() || descricao == null || descricao.trim().isEmpty() ||
                precoParam == null || estoqueParam == null) {
            response.sendRedirect("/lojista/cadastro?error=Todos%20os%20campos%20são%20obrigatórios");
            return;
        }

        try {
            double preco = Double.parseDouble(precoParam);
            int estoque = Integer.parseInt(estoqueParam);
            if (preco <= 0 || estoque < 0) {
                response.sendRedirect("/lojista/cadastro?error=Preço%20deve%20ser%20positivo%20e%20estoque%20não%20negativo");
                return;
            }

            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setEstoque(estoque);
            produtoDAO.save(produto);
            response.sendRedirect("/lojista/produtos?success=Produto%20cadastrado%20com%20sucesso");
        } catch (NumberFormatException e) {
            response.sendRedirect("/lojista/cadastro?error=Preço%20ou%20estoque%20inválidos");
        } catch (SQLException e) {
            response.sendRedirect("/lojista/cadastro?error=Erro%20ao%20cadastrar%20produto");
        }
    }
}