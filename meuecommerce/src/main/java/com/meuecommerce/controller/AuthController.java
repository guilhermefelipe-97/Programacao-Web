package com.meuecommerce.controller;

import com.meuecommerce.model.Cliente;
import com.meuecommerce.model.Lojista;
import com.meuecommerce.repository.ClienteRepository;
import com.meuecommerce.repository.LojistaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LojistaRepository lojistaRepository;

    @GetMapping("/login")
    public String exibirFormularioLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
        Cliente cliente = clienteRepository.findById(email).orElse(null);
        if (cliente != null && cliente.getSenha().equals(senha)) {
            session.setAttribute("usuario", cliente);
            session.setAttribute("tipoUsuario", "cliente");
            return "redirect:/produtos";
        }
        Lojista lojista = lojistaRepository.findById(email).orElse(null);
        if (lojista != null && lojista.getSenha().equals(senha)) {
            session.setAttribute("usuario", lojista);
            session.setAttribute("tipoUsuario", "lojista");
            return "redirect:/lojista/produtos";
        }
        model.addAttribute("erro", "Email ou senha inválidos");
        return "login";
    }

    @GetMapping("/logout")
    public String fazerLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String realizarCadastro(@RequestParam String nome, @RequestParam String email, @RequestParam String senha, Model model) {
        if (clienteRepository.existsById(email) || lojistaRepository.existsById(email)) {
            model.addAttribute("erro", "Email já cadastrado");
            return "cadastro";
        }
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setSenha(senha);
        clienteRepository.save(cliente);
        return "redirect:/login";
    }
}
