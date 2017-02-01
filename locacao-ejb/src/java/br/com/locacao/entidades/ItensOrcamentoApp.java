/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.locacao.entidades;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vitor
 */
@Entity
@Table(name = "itensorcamentoapp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItensOrcamentoApp.findAll", query = "SELECT i FROM ItensOrcamentoApp i")})
public class ItensOrcamentoApp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_item", nullable = false)
    private Integer idItem;
    @JoinColumn(name = "produto", referencedColumnName = "id_produto")
    @ManyToOne
    private Produtos produto;
    @JoinColumn(name = "orcamento", referencedColumnName = "id_orcamento")
    @ManyToOne
    private OrcamentoApp orcamento;
    private Integer quantidade;

    public ItensOrcamentoApp() {
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public Produtos getProduto() {
        return produto;
    }

    public void setProduto(Produtos produto) {
        this.produto = produto;
    }

    public OrcamentoApp getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(OrcamentoApp orcamento) {
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
        hash += (idItem != null ? idItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItensOrcamentoApp)) {
            return false;
        }
        ItensOrcamentoApp other = (ItensOrcamentoApp) object;
        if ((this.idItem == null && other.idItem != null) || (this.idItem != null && !this.idItem.equals(other.idItem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.locacao.entidades.ItensOrcamentoApp[ idItem=" + idItem + " ]";
    }
}
