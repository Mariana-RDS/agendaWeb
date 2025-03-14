package com.vespertinedev.agendaWeb.controllers;

import java.sql.SQLException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.vespertinedev.agendaWeb.model.entity.UsuarioEntity;
import com.vespertinedev.agendaWeb.model.repositories.Fachada;
import com.vespertinedev.agendaWeb.model.repositories.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.mindrot.jbcrypt.BCrypt;


@Controller
public class MainController {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public MainController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @RequestMapping({"/", "", "/*"})
    public String menuPrincipal(HttpSession session) {
        if(session.getAttribute("usuarioLogado") != null){
            return "redirect:/home";
        }else{
            return "redirect:/login";
        }

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String authUsuario(HttpSession session, @RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            UsuarioEntity usuario = usuarioRepository.findByUsername(username);
            if (usuario != null && BCrypt.checkpw(password, usuario.getPassword())) {
                
                session.setAttribute("usuarioLogado", usuario);

                return "redirect:/home";
            } else {

                return "redirect:/login?error=true";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("msg", "Erro ao autenticar o usuário");
            return "redirect:/login?error=true";
        }
    }


    @GetMapping("/cadastro")
    public String pagCadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastroUser(@ModelAttribute UsuarioEntity usuario, RedirectAttributes redirectAttributes) {
        System.out.println("Telefone recebido: " + usuario.getTelefoneNum());
        try {
            if (usuarioRepository.findByUsername(usuario.getUsername()) != null) {
                redirectAttributes.addFlashAttribute("msg", "Usuário já existe");
                return "redirect:/cadastro";
            }
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("msg", "Usuário cadastrado com sucesso");
            return "redirect:/login";
        } catch (SQLException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("msg", "Erro ao cadastrar usuário");
            return "redirect:/cadastro";
        }
    }

    @GetMapping("/home")
    public String home(Model m, HttpSession session, RedirectAttributes redirectAttributes) {
        UsuarioEntity usuarioLogado = (UsuarioEntity) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            redirectAttributes.addFlashAttribute("msg", "Usuário não está logado");

            return "redirect:/login";
        }
        m.addAttribute("nomeUsuario", usuarioLogado.getNome());
        m.addAttribute("usuarioLogado", usuarioLogado);

        try {
            m.addAttribute("contatos", Fachada.getCurrentInstance().readAll(usuarioLogado.getId()));
        } catch (SQLException e) {
            m.addAttribute("msg", "Erro ao carregar a agenda");
        }
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
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
