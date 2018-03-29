package com.algaworks.cobranca.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.cobranca.model.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.Titulos;
import com.algaworks.cobranca.service.SrvTitulo;

/*
 * *
 * Se eu quiser mapear o recurso aqui (@RequestMapping("/titulos")) eu posso, já
 * que todas as requisições desse controller tratam do mesmo recurso que é um titulo.
 * Se for feito assim, cada vez que eu quiser adicionar uma nova parte na URL como
 * "/novo" para a página de cadastro de novos objetos, eu acrescento só esse restante
 * em uma annotation sobre o método: (@RequestMapping("/novo")). O Spring vai entender
 * como (@RequestMapping("/titulos/novo")).
 */
@Controller
@RequestMapping("/titulos/titulos")
public class TituloController {

    @Autowired
    private Titulos titulos;

    @Autowired
    private SrvTitulo srvTitulo;

    @RequestMapping("/novo")
    public ModelAndView novo() {
        final ModelAndView mv = new ModelAndView( "titulos/CadastroTitulo" );
        mv.addObject( new Titulo() );
        return mv;
    }

    /*
     * o ModelAndView substitui o retorno do tipo String
     * que leva apenas o nome da página por um objeto em que pode-se
     * adicionar coisas
     */
    @RequestMapping(method = RequestMethod.POST)
    // public ModelAndView salvar( @Validated final Titulo titulo, final Errors erros,
    // RedirectAttributes attributes ) {
    public String salvar( @Validated final Titulo titulo, final Errors erros, final RedirectAttributes attributes ) {
        // final ModelAndView mv = new ModelAndView( "titulos/CadastroTitulo" );
        if ( erros.hasErrors() ) {
            // return mv;
            return "titulos/CadastroTitulo";
        }
        try {
            srvTitulo.salvar( titulo );
            // mv.addObject( "mensagem", "Título adicionado com sucesso!" );
            if ( titulo.getCodigo() == null ) {
                attributes.addFlashAttribute( "mensagem", "Título adicionado com sucesso!" );
            } else {
                attributes.addFlashAttribute( "mensagem", "Título alterado com sucesso!" );
            }
            // return mv;
            return "redirect:titulos";
        } catch ( Exception e ) {
            erros.rejectValue( "dataVencimento", null, e.getMessage() );
            return "titulos/CadastroTitulo";
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView filtrar( @RequestParam(defaultValue = "%") String descricao ) {
        List<Titulo> todosTitulos = srvTitulo.filtrar( descricao );

        final ModelAndView mv = new ModelAndView( "titulos/PesquisaTitulos" );

        mv.addObject( "titulos", todosTitulos );

        return mv;
    }

    /*
     * o model attribute serve para adicionar atributos na página
     * sem duplicar códigos. O value deve existir na página xhtml.
     */
    @ModelAttribute(value = "statusTitulos")
    public List<StatusTitulo> todosStatusTitulo() {
        return Arrays.asList( StatusTitulo.values() );
    }

    @RequestMapping("/{codigo}")
    public ModelAndView editar( @PathVariable final Long codigo ) {

        final ModelAndView mv = new ModelAndView( "titulos/CadastroTitulo" );

        mv.addObject( "titulo", titulos.findOne( codigo ) );

        return mv;
    }

    /*
     * Esse método excluir tem um DELETE mapeado, porém o action do form é um POST.
     * Isso funciona no Spring passando uma propriedade via input hidden:
     * 
     * <input type="hidden" name="_method" value="DELETE"/>
     */
    @RequestMapping(value = "/{codigo}", method = RequestMethod.DELETE)
    public ModelAndView exluir( @PathVariable Long codigo, RedirectAttributes attributes ) {
        srvTitulo.excluir( codigo );
        return filtrar( "%" ).addObject( "mensagem", "Título excluído com sucesso!" );
    }

    @RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
    public ResponseEntity<String> receber( @PathVariable Long codigo ) {
        StatusTitulo status = srvTitulo.receber( codigo );
        return ResponseEntity.ok().body( status.getDescricao() );
    }
}
