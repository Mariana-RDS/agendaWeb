package com.vespertinedev.agendaWeb.controllers;

import java.sql.SQLException;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import com.vespertinedev.agendaWeb.model.entity.UsuarioEntity;
import com.vespertinedev.agendaWeb.model.repositories.Fachada;
import com.vespertinedev.agendaWeb.model.repositories.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MainController {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public MainController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @RequestMapping({"/", "", "/*"})
    public String menuPrincipal(HttpSession session) {
        if(session.getAttribute("usuario") != null){
            return "redirect:home";
        }else{
            return "redirect:/login";
        }

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @PostMapping("/login")
    public String authUsuario(HttpSession session, @RequestParam String username, @RequestParam String password) {
        System.out.println("Tentativa de login:");
        System.out.println("Username recebido: " + username);
        System.out.println("Password recebido: " + password);
        if(isValidUser(username, password)){
            session.setAttribute("user", username);
            System.out.println("Login bem-sucedido. Redirecionando para /home");
            return "redirect:/home";
        }else{
            System.out.println("Falha no login. Redirecionando para /login");
            return "redirect:login?error=true";
        }
    }

    @GetMapping("/cadastro")
    public String pagCadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastroUser(UsuarioEntity usuario) {

        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(Model m) {
        try {
            m.addAttribute("contatos", Fachada.getCurrentInstance().readAll());
        } catch (SQLException e) {
            m.addAttribute("msg", "Erro ao carregar a agenda");
        }
        return "index";
    }

    private boolean isValidUser(String username, String password) {
        try {

            UsuarioEntity user = usuarioRepository.findByUsername(username);
            return user != null && user.getPassword().equals(password);

        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }
}
