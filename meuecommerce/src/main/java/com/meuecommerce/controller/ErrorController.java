package com.meuecommerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @RequestMapping(method = RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String message = (String) request.getAttribute("jakarta.servlet.error.message");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Erro</title>");
        out.println("<script src='https://cdn.tailwindcss.com'></script>");
        out.println("</head>");
        out.println("<body class='bg-gray-100 flex items-center justify-center min-h-screen'>");
        out.println("<div class='bg-white p-8 rounded-lg shadow-md w-full max-w-md'>");
        out.println("<h2 class='text-2xl font-bold mb-6 text-center'>Erro</h2>");
        if (statusCode != null && statusCode == 404) {
            out.println("<p class='text-center text-red-600'>Página não encontrada (404)</p>");
        } else if (statusCode != null && statusCode == 500) {
            out.println("<p class='text-center text-red-600'>Erro interno do servidor (500)</p>");
        } else {
            out.println("<p class='text-center text-red-600'>Ocorreu um erro: " + (message != null ? message : "Desconhecido") + "</p>");
        }
        out.println("<div class='mt-4 text-center'>");
        out.println("<a href='/login' class='text-indigo-600 hover:underline'>Voltar ao Login</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}