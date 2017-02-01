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
@Table(name = "mensagemapp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MensagemApp.findAll", query = "SELECT m FROM MensagemApp m")})
public class MensagemApp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_mensagem", nullable = false)
    private Integer idMensagem;
    @JoinColumn(name = "cliente", referencedColumnName = "id_cliente")
    @ManyToOne
    private ClientesApp cliente;
    @Size(max = 30)
    @Column(length = 30)
    private String assunto;
    @Size(max = 500)
    @Column(length = 500)
    private String mensagem;
    private boolean respondido;
    
    public MensagemApp() {
    }

    public MensagemApp(Integer idMensagem) {
        this.idMensagem = idMensagem;
    }

    public Integer getIdEvento() {
        return idMensagem;
    }

    public void setIdEvento(Integer idMensagem) {
        this.idMensagem = idMensagem;
    }

    public Integer getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(Integer idMensagem) {
        this.idMensagem = idMensagem;
    }

    public boolean isRespondido() {
        return respondido;
    }

    public void setRespondido(boolean respondido) {
        this.respondido = respondido;
    }

    public ClientesApp getCliente() {
        return cliente;
    }

    public void setCliente(ClientesApp cliente) {
        this.cliente = cliente;
    }

    
    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMensagem != null ? idMensagem.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MensagemApp)) {
            return false;
        }
        MensagemApp other = (MensagemApp) object;
        if ((this.idMensagem == null && other.idMensagem != null) || (this.idMensagem != null && !this.idMensagem.equals(other.idMensagem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.locacao.entidades.MensagemApp[ idMensagem=" + idMensagem + " ]";
    }
    
}
