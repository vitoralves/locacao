/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.locacao.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vitor
 */
@Entity
@Table(name = "itensorcamento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItensOrcamento.findAll", query = "SELECT i FROM ItensOrcamento i")})
public class ItensOrcamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_itemorcamento", nullable = false)
    private Integer idItemOrcamento;
    @JoinColumn(name = "produto", referencedColumnName = "id_produto")
    @ManyToOne
    private Produtos produto;
    @JoinColumn(name = "orcamento", referencedColumnName = "id_orcamento")
    @ManyToOne
    private Orcamento orcamento;
    private Integer quantidade;

    public ItensOrcamento() {
    }

    public Integer getIdItemOrcamento() {
        return idItemOrcamento;
    }

    public void setIdItemOrcamento(Integer idItemOrcamento) {
        this.idItemOrcamento = idItemOrcamento;
    }

    public Produtos getProduto() {
        return produto;
    }

    public void setProduto(Produtos produto) {
        this.produto = produto;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idItemOrcamento != null ? idItemOrcamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItensOrcamento)) {
            return false;
        }
        ItensOrcamento other = (ItensOrcamento) object;
        if ((this.idItemOrcamento == null && other.idItemOrcamento != null) || (this.idItemOrcamento != null && !this.idItemOrcamento.equals(other.idItemOrcamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.locacao.entidades.ItensOrcamento[ idItemOrcamento=" + idItemOrcamento + " ]";
    }
}
