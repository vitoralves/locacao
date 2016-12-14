package br.com.locacao.servicos;

import br.com.locacao.entidades.Empresa;
import br.com.locacao.entidades.Usuarios;
import br.com.locacao.repositorio.RepositorioEmpresa;
import br.com.locacao.repositorio.RepositorioUsuario;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vitor
 */

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServicoEmpresa extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioEmpresa empRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        empRepositorio = new RepositorioEmpresa(em);
    }
    
    public ServicoEmpresa(){
        
    }
    
    public Empresa getEmpresa(int idEmpresa){
        return empRepositorio.getEmpresa(idEmpresa);
    }
    
    public Empresa setEmpresa(Empresa emp){
        return empRepositorio.setEmpresa(emp);
    }
    
}
