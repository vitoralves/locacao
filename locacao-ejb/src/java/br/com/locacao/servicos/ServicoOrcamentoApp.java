package br.com.locacao.servicos;

import br.com.locacao.entidades.OrcamentoApp;
import br.com.locacao.repositorio.RepositorioClienteApp;
import br.com.locacao.repositorio.RepositorioOrcamentoApp;
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
public class ServicoOrcamentoApp extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioOrcamentoApp orcAppRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        orcAppRepositorio = new RepositorioOrcamentoApp(em);
    }
    
    public ServicoOrcamentoApp(){
        
    }
    
    public long getQuantidadeOrcamentos(){
        return orcAppRepositorio.getQuantidadeOrcamentos();
    }
    
    public void salvar(OrcamentoApp o){
        orcAppRepositorio.salvar(o);
    }
    
    public List<OrcamentoApp> getTodosOrcamentosAtivos(){
        return orcAppRepositorio.getTodosOrcamentosAtivos();
    }
    
    public void marcarEnviado(OrcamentoApp orc){
        orcAppRepositorio.marcarEnviado(orc);
    }
    
    public void imprimeOrcamento(OrcamentoApp orc, String usuario, int empresa){
        orcAppRepositorio.imprimeOrcamento(orc, usuario, empresa);
    }
}
