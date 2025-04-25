package com.meuecommerce.controller;

import com.meuecommerce.dao.ClienteDAO;
import com.meuecommerce.dao.LojistaDAO;
import com.meuecommerce.model.Cliente;
import com.meuecommerce.model.Lojista;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@Controller
@RequestMapping("/cadastro")
public class CadastroController {
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final LojistaDAO lojistaDAO = new LojistaDAO();

    @RequestMapping(method = RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String error = request.getParameter("error");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Cadastro</title>");
        out.println("<script src='https://cdn.tailwindcss.com'></script>");
        out.println("</head>");
        out.println("<body class='bg-gray-100 flex items-center justify-center min-h-screen'>");
        out.println("<div class='bg-white p-8 rounded-lg shadow-md w-full max-w-md'>");
        out.println("<h2 class='text-2xl font-bold mb-6 text-center'>Cadastro</h2>");
        out.println("<form action='/cadastro' method='post' class='space-y-4'>");
        out.println("<div>");
        out.println("<label for='nome' class='block text-sm font-medium text-gray-700'>Nome:</label>");
        out.println("<input type='text' id='nome' name='nome' required class='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'>");
        out.println("</div>");
        out.println("<div>");
        out.println("<label for='email' class='block text-sm font-medium text-gray-700'>Email:</label>");
        out.println("<input type='email' id='email' name='email' required class='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'>");
        out.println("</div>");
        out.println("<div>");
        out.println("<label for='senha' class='block text-sm font-medium text-gray-700'>Senha:</label>");
        out.println("<input type='password' id='senha' name='senha' required class='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'>");
        out.println("</div>");
        out.println("<div>");
        out.println("<label for='tipo' class='block text-sm font-medium text-gray-700'>Tipo:</label>");
        out.println("<select id='tipo' name='tipo' required class='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'>");
        out.println("<option value='cliente'>Cliente</option>");
        out.println("<option value='lojista'>Lojista</option>");
        out.println("</select>");
        out.println("</div>");
        out.println("<div>");
        out.println("<button type='submit' class='w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700'>Cadastrar</button>");
        out.println("</div>");
        if (error != null) {
            out.println("<div class='text-red-500 text-sm text-center'>" + error + "</div>");
        }
        out.println("</form>");
        out.println("<p class='mt-4 text-center text-sm'>Já tem uma conta? <a href='/login' class='text-indigo-600 hover:underline'>Fazer login</a></p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @RequestMapping(method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String tipo = request.getParameter("tipo");

        if (nome == null || nome.trim().isEmpty() || email == null || email.trim().isEmpty() ||
                senha == null || senha.trim().isEmpty() || tipo == null || tipo.trim().isEmpty()) {
            response.sendRedirect("/cadastro?error=Todos%20os%20campos%20são%20obrigatórios");
            return;
        }

        try {
            if (clienteDAO.existsByEmail(email) || lojistaDAO.existsByEmail(email)) {
                response.sendRedirect("/cadastro?error=Email%20já%20cadastrado");
                return;
            }

            if (tipo.equals("cliente")) {
                Cliente cliente = new Cliente();
                cliente.setNome(nome);
                cliente.setEmail(email);
                cliente.setSenha(senha);
                clienteDAO.save(cliente);
            } else if (tipo.equals("lojista")) {
                Lojista lojista = new Lojista();
                lojista.setNome(nome);
                lojista.setEmail(email);
                lojista.setSenha(senha);
                lojistaDAO.save(lojista);
            } else {
                response.sendRedirect("/cadastro?error=Tipo%20de%20usuário%20inválido");
                return;
            }

            response.sendRedirect("/login");
        } catch (SQLException e) {
            response.sendRedirect("/cadastro?error=Erro%20ao%20salvar%20usuário");
        }
    }
}