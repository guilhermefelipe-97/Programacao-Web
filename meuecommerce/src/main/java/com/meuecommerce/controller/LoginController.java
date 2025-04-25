package com.meuecommerce.controller;

import com.meuecommerce.dao.ClienteDAO;
import com.meuecommerce.dao.LojistaDAO;
import com.meuecommerce.model.Cliente;
import com.meuecommerce.model.Lojista;
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
@RequestMapping("/login")
public class LoginController {
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
        out.println("<title>Login</title>");
        out.println("<script src='https://cdn.tailwindcss.com'></script>");
        out.println("</head>");
        out.println("<body class='bg-gray-100 flex items-center justify-center min-h-screen'>");
        out.println("<div class='bg-white p-8 rounded-lg shadow-md w-full max-w-md'>");
        out.println("<h2 class='text-2xl font-bold mb-6 text-center'>Login</h2>");
        out.println("<form action='/login' method='post' class='space-y-4'>");
        out.println("<div>");
        out.println("<label for='email' class='block text-sm font-medium text-gray-700'>Email:</label>");
        out.println("<input type='email' id='email' name='email' required class='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'>");
        out.println("</div>");
        out.println("<div>");
        out.println("<label for='senha' class='block text-sm font-medium text-gray-700'>Senha:</label>");
        out.println("<input type='password' id='senha' name='senha' required class='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'>");
        out.println("</div>");
        out.println("<div>");
        out.println("<button type='submit' class='w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700'>Entrar</button>");
        out.println("</div>");
        if (error != null) {
            out.println("<div class='text-red-500 text-sm text-center'>" + error + "</div>");
        }
        out.println("</form>");
        out.println("<p class='mt-4 text-center text-sm'>Não tem uma conta? <a href='/cadastro' class='text-indigo-600 hover:underline'>Cadastrar-se</a></p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @RequestMapping(method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            response.sendRedirect("/login?error=Email%20e%20senha%20são%20obrigatórios");
            return;
        }

        try {
            Cliente cliente = clienteDAO.findByEmail(email);
            if (cliente != null && cliente.getSenha().equals(senha)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", cliente);
                session.setAttribute("userType", "cliente");
                response.sendRedirect("/produtos");
                return;
            }

            Lojista lojista = lojistaDAO.findByEmail(email);
            if (lojista != null && lojista.getSenha().equals(senha)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", lojista);
                session.setAttribute("userType", "storeOwner");
                response.sendRedirect("/lojista/produtos");
                return;
            }

            response.sendRedirect("/login?error=Email%20ou%20senha%20inválidos");
        } catch (SQLException e) {
            response.sendRedirect("/login?error=Erro%20ao%20acessar%20o%20banco%20de%20dados");
        }
    }
}