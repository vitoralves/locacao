package br.com.locacao.servicos;

import br.com.locacao.entidades.ItensOrcamento;
import br.com.locacao.repositorio.RepositorioItensOrcamento;
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
public class ServicoItensOrcamento extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioItensOrcamento evtRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        evtRepositorio = new RepositorioItensOrcamento(em);
    }
    
    public ServicoItensOrcamento(){
        
    }
    
    public ItensOrcamento getItemOrcamento(int idItemOrcamento){
        return evtRepositorio.getItemOrcamento(idItemOrcamento);
    }
    
    public ItensOrcamento setItemOrcamento(ItensOrcamento itemEvento){
        return evtRepositorio.setItemOrcamento(itemEvento);
    }
    
    public void removeItemOrcamento(ItensOrcamento itemEvento){
        evtRepositorio.removeItemOrcamento(itemEvento);
    }
    
    public ItensOrcamento addItemOrcamento(ItensOrcamento itemEvento){
        return evtRepositorio.addItemOrcamento(itemEvento);
    }
    
    public List<ItensOrcamento> getItemOrcamentos(){
        return evtRepositorio.getItemOrcamento();
    }
    
    public List<ItensOrcamento> getItemOrcamentosPorIdOrcamento(int orcamento){
        return evtRepositorio.getItemOrcamentoPorIdOrcamento(orcamento);
    }
    
    public void addItemOrcamentoParametros(int idEvento, int idProduto, int qnt){
       evtRepositorio.addItemOrcamentoParametros(idEvento,idProduto,qnt);
    }
    
    public int getQuantidadeProdutoPorData(Date dtEntrega, Date dtDevolucao, int produto){
        return evtRepositorio.getQuantidadeProdutoPorData(dtEntrega, dtDevolucao, produto);
    }
}
