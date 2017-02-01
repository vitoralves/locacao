package br.com.locacao.servicos;

import br.com.locacao.entidades.ItensOrcamento;
import br.com.locacao.entidades.ItensOrcamentoApp;
import br.com.locacao.repositorio.RepositorioItensOrcamento;
import br.com.locacao.repositorio.RepositorioItensOrcamentoApp;
import java.util.Date;
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
public class ServicoItensOrcamentoApp extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioItensOrcamentoApp evtRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        evtRepositorio = new RepositorioItensOrcamentoApp(em);
    }
    
    public ServicoItensOrcamentoApp(){
        
    }
    
    public List<ItensOrcamentoApp> getItemOrcamentoAppPorIdOrcamento(int orcamento){
        return evtRepositorio.getItemOrcamentoPorIdOrcamento(orcamento);
    }
    
    public double calculaTotalPorIdOrcamento(int orcamento){
        return evtRepositorio.calculaTotalPorIdOrcamento(orcamento);
    }
    
}
