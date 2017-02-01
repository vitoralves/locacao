package br.com.locacao.servicos;

import br.com.locacao.entidades.Orcamento;
import br.com.locacao.repositorio.RepositorioOrcamento;
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
public class ServicoOrcamento extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioOrcamento orcRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        orcRepositorio = new RepositorioOrcamento(em);
    }
    
    public ServicoOrcamento(){
        
    }
    
    public Orcamento getOrcamento(int idOrcamento){
        return orcRepositorio.getOrcamento(idOrcamento);
    }
    
    public Orcamento setOrcamento(Orcamento orcamento){
        return orcRepositorio.setOrcamento(orcamento);
    }
    
    public void removeOrcamento(Orcamento orcamento){
        orcRepositorio.removeOrcamento(orcamento);
    }
    
    public Orcamento addOrcamento(Orcamento orcamento){
        return orcRepositorio.addOrcamento(orcamento);
    }
    
    public List<Orcamento> getOrcamento(){
        return orcRepositorio.getOrcamento();
    }
    
    public Orcamento getOrcamentoPorEvento(int idEvento){
        return orcRepositorio.getOrcamentoPorEvento(idEvento);
    }
    
    public void imprimeOrcamento(int orc, int evt, String usuario, int empresa){
        orcRepositorio.imprimeOrcamento(orc, evt, usuario, empresa);
    }
    
    public void imprimeContrato(int evt, int empresa) throws InterruptedException{
        orcRepositorio.imprimeContrato(evt, empresa);
    }
    
    public void confirma(int idEvento){
        orcRepositorio.confirma(idEvento);
    }
}
