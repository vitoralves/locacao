/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.locacao.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vitor
 */
@Entity
@Table(name = "orcamentoapp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrcamentoApp.findAll", query = "SELECT o FROM OrcamentoApp o")})
public class OrcamentoApp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_orcamento", nullable = false)
    private Integer idOrcamento;
    @JoinColumn(name = "cliente", referencedColumnName = "id_cliente")
    @ManyToOne
    private ClientesApp cliente;
    @Column(name = "data_entrega")
    @Temporal(TemporalType.DATE)
    private Date dataEntrega;
    @Column(name = "data_devolucao")
    @Temporal(TemporalType.DATE)
    private Date dataDevolucao;
    @Column(name = "data_evento")
    @Temporal(TemporalType.DATE)
    private Date dataEvento;
    @Size(max = 90)
    @Column(length = 90)
    private String localEvento;
    @Size(max = 90)
    @Column(length = 90)
    private String endereco;
    @Size(max = 20)
    @Column(length = 20)
    private String situacao;
    @Size(max = 60)
    @Column(length = 60)
    private String cidade;
    @Column(name = "fg_ativo")
    private boolean fgAtivo;
    private boolean enviado;
    @Column(name = "val_frete")
    private Double frete;
    private Integer diarias;
    private Double desconto;
    @Column(name = "val_total")
    private Double total;
    @Size(max = 1)
    @Column(name = "forma_pagto", length = 1)
    private String formaPagto;
    @Size(max = 500)
    @Column(name = "observacao", length = 500)
    private String observacao;
    
    public OrcamentoApp() {
    }

    public OrcamentoApp(Integer idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public Integer getIdEvento() {
        return idOrcamento;
    }

    public void setIdEvento(Integer idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public Integer getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(Integer idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public ClientesApp getCliente() {
        return cliente;
    }

    public void setCliente(ClientesApp cliente) {
        this.cliente = cliente;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Date getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(Date dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getLocalEvento() {
        return localEvento;
    }

    public void setLocalEvento(String localEvento) {
        this.localEvento = localEvento;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public boolean isFgAtivo() {
        return fgAtivo;
    }

    public void setFgAtivo(boolean fgAtivo) {
        this.fgAtivo = fgAtivo;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Double getFrete() {
        return frete;
    }

    public void setFrete(Double frete) {
        this.frete = frete;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getFormaPagto() {
        return formaPagto;
    }

    public void setFormaPagto(String formaPagto) {
        this.formaPagto = formaPagto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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
        if (!(object instanceof OrcamentoApp)) {
            return false;
        }
        OrcamentoApp other = (OrcamentoApp) object;
        if ((this.idOrcamento == null && other.idOrcamento != null) || (this.idOrcamento != null && !this.idOrcamento.equals(other.idOrcamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.locacao.entidades.OrcamentoApp[ idOrcamento=" + idOrcamento + " ]";
    }
    
}
