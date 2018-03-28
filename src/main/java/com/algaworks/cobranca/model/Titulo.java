package com.algaworks.cobranca.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Entity
public class Titulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotNull(message = "A descrição é obrigatória")
    @Size(min = 5, max = 60, message = "A descrição deve ter entre 5 e 60 caracteres")
    private String descricao;

    @NotNull(message = "Data de vencimento é obrigatória.")
    @Temporal(TemporalType.DATE)
    @Column(name = "data_vencimento")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dataVencimento;

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.10", message = "O valor mínimo é 0,10 centavos.")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal valor;

    @Enumerated(value = EnumType.STRING)
    private StatusTitulo status;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo( final Long codigo ) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao( final String descricao ) {
        this.descricao = descricao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento( final Date dataVencimento ) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor( final BigDecimal valor ) {
        this.valor = valor;
    }

    public StatusTitulo getStatus() {
        return status;
    }

    public void setStatus( final StatusTitulo status ) {
        this.status = status;
    }

    @Transient
    public boolean isPendente() {
        return StatusTitulo.PENDENTE.equals( this.status );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( codigo == null ) ? 0 : codigo.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        final Titulo other = (Titulo) obj;
        if ( codigo == null ) {
            if ( other.codigo != null )
                return false;
        } else if ( !codigo.equals( other.codigo ) )
            return false;
        return true;
    }

}
