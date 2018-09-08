package br.com.ope.controller

import br.com.ope.dto.MensagemVO
import br.com.ope.model.Curso
import br.com.ope.repository.CursoRepository
import br.com.ope.repository.DisciplinaRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/painel/admin/cursos")
class PainelAdminCursosController {

    private val cursoRepository : CursoRepository
    private val disciplinaRepository : DisciplinaRepository

    constructor(cursoRepository: CursoRepository, disciplinaRepository: DisciplinaRepository) {
        this.cursoRepository = cursoRepository
        this.disciplinaRepository = disciplinaRepository
    }


    @GetMapping
    fun index(model : Model) : String {
        model.addAttribute("cursos", cursoRepository.findAll())
        return "painel/admin/cursos/index"
    }

    @GetMapping("/novo")
    fun novo(model : Model) : String {
        popularForm(model)
        return "painel/admin/cursos/novo"
    }

    @PostMapping("/novo")
    fun novoSalvar(model : Model, curso: Curso, redirectAttributes: RedirectAttributes,  bindingResult : BindingResult) : String {
        if (bindingResult.hasErrors()) return "painel/admin/cursos/novo"
        cursoRepository.save(curso)
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Curso salvo!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/admin/cursos"
    }

    @GetMapping("/{id}")
    fun editar(model : Model, redirectAttributes: RedirectAttributes, @PathVariable id : UUID) : String {
        var curso = cursoRepository.findById(id)

        if (!curso.isPresent) return redirectCursoNaoEncontrado(model, redirectAttributes)

        popularForm(model, curso.get())
        return "painel/admin/cursos/editar"
    }

    @PostMapping("/{id}")
    fun editarSalvar(model : Model, cursoEditado: Curso, redirectAttributes: RedirectAttributes, @PathVariable id : UUID,  bindingResult : BindingResult) : String {
        var curso = cursoRepository.findById(id)

        if (!curso.isPresent) return redirectCursoNaoEncontrado(model, redirectAttributes)

        if (bindingResult.hasErrors()) return "painel/admin/cursos/editar"
        cursoRepository.save(curso.get().atualizar(cursoEditado))

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Curso salvo!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/admin/cursos"
    }

    @GetMapping("/{id}/excluir")
    fun excluir(model : Model, redirectAttributes: RedirectAttributes, @PathVariable id : UUID) : String {
        var curso = cursoRepository.findById(id)

        if (!curso.isPresent) return redirectCursoNaoEncontrado(model, redirectAttributes)

        curso.get().dataExclusao = Date()
        cursoRepository.save(curso.get())

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Curso excluido.","", MensagemVO.TipoMensagem.info ))
        return "redirect:/painel/admin/cursos"
    }

    private fun redirectCursoNaoEncontrado(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Curso não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/admin/cursos"
    }

    private fun popularForm(model : Model, curso : Curso = Curso()): Model {
        model.addAttribute("curso", curso)
        model.addAttribute("disciplinas", disciplinaRepository.findAll())
        return model
    }

}