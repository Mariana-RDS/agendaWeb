package com.vespertinedev.agendaWeb.controllers;

import java.sql.SQLException;
import java.util.List;

import com.vespertinedev.agendaWeb.model.entity.ContatoEntity;
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

    @GetMapping({"", "/", "listar"})
    public String contatos(Model m, RedirectAttributes redirectAttributes){

        try{
            List<ContatoEntity> contatos = fachadaC.readAll();
            m.addAttribute("contatos", contatos);

            if(this.msg != null){
                m.addAttribute("msg",this.msg);
                this.msg = null;
            }
        }catch(SQLException e){
            redirectAttributes.addFlashAttribute("msg", "Erro ao buscar a lista de contatos");
            return "redirect:/";
        }
        return "index";
    }

    @PostMapping("/criar")
    public String criar(Model m, ContatoEntity c, HttpServletRequest request){

        try{
            Fachada.getCurrentInstance().create(c);
        }catch(SQLException e){
            this.msg ="Erro ao cadastrar";
            e.printStackTrace();
        }

        return "redirect:/index";
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
    public String editar(Model m, @ModelAttribute ContatoEntity c) {
        try {
            ContatoEntity contatoExistente = Fachada.getCurrentInstance().readContato(c.getId());

            if (contatoExistente == null) {
                this.msg = "Contato não encontrado.";
                return "redirect:/index";
            }

            c.setUsuarioEntity(contatoExistente.getUsuarioEntity());

            Fachada.getCurrentInstance().update(c);

            this.msg = "Contato editado com sucesso";
            return "redirect:/index";
        } catch (SQLException e) {
            this.msg = "Não foi possível editar o contato";
            return "redirect:/index";
        }
    }


    @SuppressWarnings("finally")
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
}
