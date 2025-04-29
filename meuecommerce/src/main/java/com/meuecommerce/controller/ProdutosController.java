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
@RequestMapping("/produtos")
public class ProdutosController {
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @RequestMapping(method = RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userType = (String) request.getSession().getAttribute("userType");
        if (userType == null || (!userType.equals("cliente") && !userType.equals("storeOwner"))) {
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
        out.println("<title>Lista de Produtos</title>");
        out.println("<script src='https://cdn.tailwindcss.com'></script>");
        out.println("<style>");
        out.println(".tooltip { position: absolute; z-index: 10; }");
        out.println("</style>");
        out.println("<script>");
        out.println("function showLink(element, link) {");
        out.println("  var tooltip = document.getElementById('tooltip');");
        out.println("  tooltip.innerText = link;"); // Alterado para innerText, exibindo apenas texto
        out.println("  tooltip.className = 'tooltip bg-white border border-gray-300 p-2 rounded shadow-md text-sm text-gray-700';");
        out.println("  tooltip.style.display = 'block';");
        out.println("  var rect = element.getBoundingClientRect();");
        out.println("  tooltip.style.left = (rect.left + window.scrollX + 10) + 'px';");
        out.println("  tooltip.style.top = (rect.bottom + window.scrollY + 5) + 'px';");
        out.println("}");
        out.println("function hideLink() {");
        out.println("  var tooltip = document.getElementById('tooltip');");
        out.println("  tooltip.style.display = 'none';");
        out.println("}");
        out.println("</script>");
        out.println("</head>");
        out.println("<body class='bg-gray-100 min-h-screen p-6'>");
        out.println("<div class='max-w-5xl mx-auto bg-white p-8 rounded-lg shadow-md'>");
        out.println("<div class='flex justify-between items-center mb-6'>");
        out.println("<h2 class='text-2xl font-bold'>Lista de Produtos</h2>");
        out.println("<div class='space-x-4'>");
        out.println("<a href='/carrinho' class='text-indigo-600 hover:underline'>Ver Carrinho</a>");
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

        try {
            List<Produto> produtos = produtoDAO.findAll();
            if (produtos.isEmpty()) {
                out.println("<p class='text-center text-gray-600'>Nenhum produto disponível.</p>");
            } else {
                out.println("<div class='grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6'>");
                for (Produto produto : produtos) {
                    out.println("<div class='bg-gray-50 p-4 rounded-lg shadow'>");
                    out.println("<h3 class='text-lg font-semibold'>" + produto.getNome() + "</h3>");
                    out.println("<p class='text-gray-600'>" + produto.getDescricao() + "</p>");
                    out.println("<p class='text-indigo-600 font-medium'>R$ " + String.format("%.2f", produto.getPreco()) + "</p>");
                    out.println("<p class='text-gray-500'>Estoque: " + produto.getEstoque() + "</p>");
                    String fullLink = "http://localhost:8080/carrinho/add?id=" + produto.getId(); // Link com domínio completo
                    String linkClass = produto.getEstoque() > 0 ? "mt-2 inline-block bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700" : "mt-2 inline-block bg-gray-400 text-white py-2 px-4 rounded-md cursor-not-allowed";
                    String disabled = produto.getEstoque() > 0 ? "" : "disabled";
                    out.println("<a href='/carrinho/add?id=" + produto.getId() + "' class='" + linkClass + "' " + disabled + " onmouseover=\"showLink(this, '" + fullLink + "')\" onmouseout=\"hideLink()\">Adicionar ao Carrinho</a>");
                    out.println("</div>");
                }
                out.println("</div>");
            }
        } catch (SQLException e) {
            out.println("<div class='bg-red-100 text-red-700 p-4 rounded mb-4'>Erro ao listar produtos</div>");
        }

        out.println("<div id='tooltip' class='tooltip hidden'></div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}