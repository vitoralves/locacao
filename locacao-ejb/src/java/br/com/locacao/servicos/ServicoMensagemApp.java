package br.com.locacao.servicos;

import br.com.locacao.entidades.MensagemApp;
import br.com.locacao.repositorio.RepositorioMensagemApp;
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
public class ServicoMensagemApp extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioMensagemApp msgAppRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        msgAppRepositorio = new RepositorioMensagemApp(em);
    }
    
    public ServicoMensagemApp(){
        
    }
    
    public long getQuantidadeMensagens(){
        return msgAppRepositorio.getQuantidadeMensagens();
    }
    
    public List<MensagemApp> getTodasMensagens(){
        return msgAppRepositorio.getTodasMensagens();
    }
    
    public void marcarEnviado(MensagemApp msg){
        msgAppRepositorio.marcarEnviado(msg);
    }
}
