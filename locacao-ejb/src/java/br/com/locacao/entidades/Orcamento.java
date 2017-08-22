/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.locacao.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vitor
 */
@Entity
@Table(name = "orcamento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orcamento.findAll", query = "SELECT o FROM Orcamento o")})
public class Orcamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_orcamento", nullable = false)
    private Integer idOrcamento;
    @Column(name = "val_frete")
    private Double valFrete;
    private Integer diarias;
    private Double desconto;
    @Column(name = "val_total")
    private Double valTotal;
    @Size(max = 1)
    @Column(name = "forma_pagto", length = 1)
    private String formaPagto;
    @Size(max = 500)
    @Column(name = "observacao", length = 500)
    private String observacao;
    @Size(max = 100)
    @Column(name = "observacao_pagamento", length = 100)
    private String observacaoPagamento;
    @JoinColumn(name = "evento", referencedColumnName = "id_evento")
    @ManyToOne
    private Eventos evento;

    public Orcamento() {
    }

    public Orcamento(Integer idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public Integer getIdEvento() {
        return idOrcamento;
    }

    public void setIdEvento(Integer idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public Integer getDiarias() {
        return diarias;
    }

    public void setDiarias(Integer diarias) {
        this.diarias = diarias;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getValFrete() {
        return valFrete;
    }

    public void setValFrete(Double valFrete) {
        this.valFrete = valFrete;
    }

    public Double getValTotal() {
        return valTotal;
    }

    public void setValTotal(Double valTotal) {
        this.valTotal = valTotal;
    }
    
    public String getFormaPagto() {
        return formaPagto;
    }

    public void setFormaPagto(String formaPagto) {
        this.formaPagto = formaPagto;
    }
  
    public Integer getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(Integer idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public Eventos getEvento() {
        return evento;
    }

    public void setEvento(Eventos evento) {
        this.evento = evento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getObservacaoPagamento() {
        return observacaoPagamento;
    }

    public void setObservacaoPagamento(String observacaoPagamento) {
        this.observacaoPagamento = observacaoPagamento;
    }
        
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrcamento != null ? idOrcamento.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orcamento)) {
            return false;
        }
        Orcamento other = (Orcamento) object;
        if ((this.idOrcamento == null && other.idOrcamento != null) || (this.idOrcamento != null && !this.idOrcamento.equals(other.idOrcamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.locacao.entidades.Orcamento[ idOrcamento=" + idOrcamento + " ]";
    }
    
}
