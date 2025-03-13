package com.vespertinedev.agendaWeb.controllers;

import java.sql.SQLException;
import java.util.List;

import com.vespertinedev.agendaWeb.model.entity.ContatoEntity;
import com.vespertinedev.agendaWeb.model.entity.TelefoneEntity;
import com.vespertinedev.agendaWeb.model.entity.UsuarioEntity;
import com.vespertinedev.agendaWeb.model.repositories.ContatoRepository;
import com.vespertinedev.agendaWeb.model.repositories.Fachada;
import com.vespertinedev.agendaWeb.model.repositories.FachadaComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contato")
public class ContatoController {

    private String msg = null;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpSession session;
    @Autowired
    private FachadaComponent fachadaC;

    @Autowired
    private ContatoRepository contatoRepository;

    @GetMapping({"", "/", "listar"})
    public String contatos(Model m, HttpSession session, RedirectAttributes redirectAttributes) {
        UsuarioEntity usuarioLogado = (UsuarioEntity) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            redirectAttributes.addFlashAttribute("msg", "Usuário não está logado");
            return "redirect:/login";
        }

        try {
            List<ContatoEntity> contatos = contatoRepository.readAll(usuarioLogado.getId());
            m.addAttribute("contatos", contatos);
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("msg", "Erro ao buscar a lista de contatos");
            return "redirect:/";
        }
        return "index";
    }

    @PostMapping("/criar")
    public String cadastroContato(ContatoEntity contato, HttpSession session) {
        UsuarioEntity usuarioLogado = (UsuarioEntity) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            contatoRepository.create(contato, usuarioLogado.getId());
            return "redirect:/home";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/erro";
        }
    }

    @GetMapping("/editar/{id}")
    public String telaEditar(Model m,@PathVariable("id") Integer id) {

        try {
            ContatoEntity c = Fachada.getCurrentInstance().readContato(id);

            if(c == null) {

                this.msg = "Este contato não existe";

                return "redirect:/index";
            }

            m.addAttribute("contato", c);

            return "contato/editar";
        } catch (SQLException e) {

            this.msg = "Não é possível editar o contato";

            return "redirect:/index";
        }

    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute ContatoEntity contato, HttpSession session, RedirectAttributes redirectAttributes) {
        UsuarioEntity usuarioLogado = (UsuarioEntity) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            for (TelefoneEntity telefone : contato.getTelefones()) {
                if (telefone.getId() == null) {
                    throw new SQLException("ID do telefone não pode ser nulo");
                }
            }

            contato.setUsuarioEntity(usuarioLogado);
            contatoRepository.update(contato, usuarioLogado.getId());
            redirectAttributes.addFlashAttribute("msg", "Contato editado com sucesso");
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("msg", "Erro ao editar o contato: " + e.getMessage());
        }
        return "redirect:/contato/listar";
    }



    @GetMapping("/deletar/{id}")
    public String deletar(Model m,@PathVariable Integer id){
        try{
            Fachada.getCurrentInstance().deleteContato(id);

            this.msg = "Contato deletado de sua agenda";
        }catch(SQLException e){
            this.msg = "Erro ao deletar contato";
        }finally{
            return "redirect:/index";
        }
    }

    @GetMapping("/contato/pesquisar")
    public String pesquisarContatos(@RequestParam(name = "nome", required = false) String nome, Model model, HttpSession session) {
        UsuarioEntity usuarioLogado = (UsuarioEntity) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            List<ContatoEntity> contatos;
            if (nome != null && !nome.isEmpty()) {
                contatos = contatoRepository.findByNomeUsuario(nome, usuarioLogado.getId());
            } else {
                contatos = contatoRepository.findByUsuarioId(usuarioLogado.getId());
            }
            model.addAttribute("contatos", contatos);
        } catch (SQLException e) {
            model.addAttribute("msg", "Erro ao pesquisar contatos");
        }
        return "index";
    }
}
