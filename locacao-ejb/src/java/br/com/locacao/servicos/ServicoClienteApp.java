package br.com.locacao.servicos;

import br.com.locacao.entidades.Clientes;
import br.com.locacao.repositorio.RepositorioCliente;
import br.com.locacao.repositorio.RepositorioClienteApp;
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
public class ServicoClienteApp extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioClienteApp cliAppRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        cliAppRepositorio = new RepositorioClienteApp(em);
    }
    
    public ServicoClienteApp(){
        
    }
    
    public long getQuantidadeClientes(){
        return cliAppRepositorio.getQuantidadeClientes();
    }
}
