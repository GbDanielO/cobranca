package com.algaworks.cobranca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.algaworks.cobranca.model.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.Titulos;

@Service
public class SrvTitulo {

    @Autowired
    private Titulos titulos;

    public Titulo salvar( final Titulo titulo ) {
        try {
            return titulos.save( titulo );
        } catch ( DataIntegrityViolationException e ) {
            throw new IllegalArgumentException( "Formato da data inválido" );
        }
    }

    public List<Titulo> buscarTodos() {

        final List<Titulo> todosTitulos = titulos.findAll( new Sort( "codigo" ) );

        return todosTitulos;
    }

    public void excluir( Long codigo ) {
        titulos.delete( codigo );
    }

    public StatusTitulo receber( Long codigo ) {
        Titulo titulo = titulos.findOne( codigo );
        titulo.setStatus( StatusTitulo.RECEBIDO );
        titulos.save( titulo );
        return titulo.getStatus();
    }
}
