package br.com.locacao.servicos;

import br.com.locacao.entidades.Eventos;
import br.com.locacao.repositorio.RepositorioEvento;
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
public class ServicoEvento extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioEvento evtRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        evtRepositorio = new RepositorioEvento(em);
    }
    
    public ServicoEvento(){
        
    }
    
    public Eventos getEvento(int idEvento){
        return evtRepositorio.getEvento(idEvento);
    }
    
    public Eventos setEvento(Eventos usuario){
        return evtRepositorio.setEvento(usuario);
    }
    
    public void removeEvento(Eventos usuario){
        evtRepositorio.removeEvento(usuario);
    }
    
    public Eventos addEvento(Eventos usuario){
        return evtRepositorio.addEvento(usuario);
    }
    
    public List<Eventos> getEventos(){
        return evtRepositorio.getEvento();
    }
}
