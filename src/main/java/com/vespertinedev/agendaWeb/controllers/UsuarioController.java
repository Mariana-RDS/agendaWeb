package com.vespertinedev.agendaWeb.controllers;


import com.vespertinedev.agendaWeb.model.entity.UsuarioEntity;
import com.vespertinedev.agendaWeb.model.repositories.Fachada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private String msg = null;
    private Fachada fachada;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpSession session;

    public UsuarioController() {
        this.fachada = Fachada.getCurrentInstance();
    }

    @GetMapping({"", "/", "listar"})
    public String listarUsuario(Model m, RedirectAttributes redirectAttributes) {
        try {
            UsuarioEntity usuarioLogado = (UsuarioEntity) session.getAttribute("usuarioLogado");
            if (usuarioLogado == null) {
                redirectAttributes.addFlashAttribute("msg", "Nenhum usuário está logado");
                return "redirect:/login";
            }
            m.addAttribute("usuario", usuarioLogado);
            if (this.msg != null) {
                m.addAttribute("msg", this.msg);
                this.msg = null;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "Erro ao buscar o usuário logado");
            return "redirect:/";
        }
        return "usuario/listar";
    }

    @PostMapping("/criar")
    public String criarUsuario(@ModelAttribute UsuarioEntity usuario, RedirectAttributes redirectAttributes) {
        try {
            fachada.create(usuario);
            session.setAttribute("usuarioLogado", usuario);

            redirectAttributes.addFlashAttribute("msg", "Usuário cadastrado com sucesso!");
            return "redirect:/home";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("msg", "Erro ao cadastrar usuário");
            return "redirect:/login";
        }

    }

    @GetMapping("/editarUsuario/{id}")
    public String telaEditarUsuario(Model m, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            UsuarioEntity usuario = fachada.readUsuario(id);
            if (usuario == null) {
                redirectAttributes.addFlashAttribute("msg", "Usuário não encontrado");
                return "redirect:/usuario/listar";
            }
            m.addAttribute("usuario", usuario);
            return "usuario/editar";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("msg", "Erro ao carregar dados do usuário");
            return "redirect:/usuario/listar";
        }
    }

    @PostMapping("/editarUsuario")
    public String editarUsuario(@ModelAttribute UsuarioEntity usuario, RedirectAttributes redirectAttributes) {
        try {
            fachada.update(usuario);
            redirectAttributes.addFlashAttribute("msg", "Usuário atualizado com sucesso");
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("msg", "Erro ao atualizar usuário");
        }
        return "redirect:/usuario/listar";
    }

    @GetMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            fachada.deleteUsuario(id);
            redirectAttributes.addFlashAttribute("msg", "Usuário deletado com sucesso");
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("msg", "Erro ao deletar usuário");
        }
        return "redirect:/login";
    }
}
