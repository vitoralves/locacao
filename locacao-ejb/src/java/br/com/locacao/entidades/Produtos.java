/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.locacao.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author vitor
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produtos.findAll", query = "SELECT p FROM Produtos p")})
public class Produtos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_produto", nullable = false)
    private Integer idProduto;
    @Size(max = 60)
    @Column(length = 60)
    private String nome;
    @Size(max = 60)
    @Column(length = 60)
    private String descricao;
    @Column(name = "val_custo")
    private double valCusto;
    @Column(name = "val_diaria")
    private double valDiaria;
    private Integer quantidade;
//    @OneToMany(mappedBy = "produto")
//    private List<Eventos> eventosList;
    @JoinColumn(name = "categoria", referencedColumnName = "id_categoria")
    @ManyToOne
    private Categorias categoria;
//    @JoinColumn(name = "fornecedor", referencedColumnName = "id_fornecedor")
//    @ManyToOne
//    private Fornecedores fornecedor;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "imagem")
    private byte[] imagem;

    public Produtos() {
    }

    public Produtos(Integer idProduto, String nome) {
        this.idProduto = idProduto;
        this.nome = nome;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValCusto() {
        return valCusto;
    }

    public void setValCusto(double valCusto) {
        this.valCusto = valCusto;
    }

    public double getValDiaria() {
        return valDiaria;
    }

    public void setValDiaria(double valDiaria) {
        this.valDiaria = valDiaria;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

//    @XmlTransient
//    public List<Eventos> getEventosList() {
//        return eventosList;
//    }
//
//    public void setEventosList(List<Eventos> eventosList) {
//        this.eventosList = eventosList;
//    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }

//    public Fornecedores getFornecedor() {
//        return fornecedor;
//    }
//
//    public void setFornecedor(Fornecedores fornecedor) {
//        this.fornecedor = fornecedor;
//    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProduto != null ? idProduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produtos)) {
            return false;
        }
        Produtos other = (Produtos) object;
        if ((this.idProduto == null && other.idProduto != null) || (this.idProduto != null && !this.idProduto.equals(other.idProduto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.locacao.entidades.Produtos[ idProduto=" + idProduto + " ]";
    }
    
}
