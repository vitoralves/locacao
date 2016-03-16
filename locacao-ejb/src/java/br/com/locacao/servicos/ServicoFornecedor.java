package br.com.locacao.servicos;

import br.com.locacao.entidades.Fornecedores;
import br.com.locacao.repositorio.RepositorioFornecedor;
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
public class ServicoFornecedor extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioFornecedor forRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        forRepositorio = new RepositorioFornecedor(em);
    }
    
    public ServicoFornecedor(){
        
    }
    
    public Fornecedores getFornecedor(int idFornecedor){
        return forRepositorio.getFornecedor(idFornecedor);
    }
    
    public Fornecedores setFornecedor(Fornecedores usuario){
        return forRepositorio.setFornecedor(usuario);
    }
    
    public void removeFornecedor(Fornecedores usuario){
        forRepositorio.removeFornecedor(usuario);
    }
    
    public Fornecedores addFornecedor(Fornecedores usuario){
        return forRepositorio.addFornecedor(usuario);
    }
    
    public List<Fornecedores> getFornecedores(){
        return forRepositorio.getFornecedor();
    }
}
