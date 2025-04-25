package com.meuecommerce.controller;

import com.meuecommerce.dao.ClienteDAO;
import com.meuecommerce.dao.LojistaDAO;
import com.meuecommerce.model.Cliente;
import com.meuecommerce.model.Lojista;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class AuthController {

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private LojistaDAO lojistaDAO;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(required = false) String email, @RequestParam(required = false) String senha, HttpSession session, Model model) {
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            model.addAttribute("error", "Email e senha são obrigatórios");
            return "login";
        }
        try {
            Cliente cliente = clienteDAO.findByEmail(email);
            if (cliente != null && cliente.getSenha().equals(senha)) {
                session.setAttribute("user", cliente);
                session.setAttribute("userType", "cliente");
                return "redirect:/produtos";
            }
            Lojista lojista = lojistaDAO.findByEmail(email);
            if (lojista != null && lojista.getSenha().equals(senha)) {
                session.setAttribute("user", lojista);
                session.setAttribute("userType", "storeOwner");
                return "redirect:/lojista/produtos";
            }
            model.addAttribute("error", "Email ou senha inválidos");
            return "login";
        } catch (SQLException e) {
            model.addAttribute("error", "Erro ao acessar o banco de dados");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/cadastro")
    public String cadastroForm() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastro(@RequestParam(required = false) String nome, @RequestParam(required = false) String email, @RequestParam(required = false) String senha, @RequestParam(required = false) String tipo, Model model) {
        if (nome == null || nome.trim().isEmpty() || email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty() || tipo == null || tipo.trim().isEmpty()) {
            model.addAttribute("error", "Todos os campos são obrigatórios");
            return "cadastro";
        }
        try {
            if (clienteDAO.existsByEmail(email) || lojistaDAO.existsByEmail(email)) {
                model.addAttribute("error", "Email já cadastrado");
                return "cadastro";
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
                model.addAttribute("error", "Tipo de usuário inválido");
                return "cadastro";
            }
            return "redirect:/login";
        } catch (SQLException e) {
            model.addAttribute("error", "Erro ao salvar usuário");
            return "cadastro";
        }
    }
}