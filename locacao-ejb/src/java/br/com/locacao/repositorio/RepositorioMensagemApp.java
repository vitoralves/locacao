package br.com.locacao.repositorio;

import br.com.locacao.entidades.MensagemApp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author vitor
 */
public class RepositorioMensagemApp extends RepositorioBasico {

    public static final long serialversionUID = 1L;

    public RepositorioMensagemApp(EntityManager entityManager) {
        super(entityManager);
    }

    public long getQuantidadeMensagens(){
        Query q = entityManager.createQuery("select count(m.idMensagem) from MensagemApp m");
        return (long) q.getSingleResult();
    }
    
    public List<MensagemApp> getTodasMensagens(){
        Query q = entityManager.createQuery("select m from MensagemApp m where m.respondido = false");
        return q.getResultList();
    }
    
    public void marcarEnviado(MensagemApp msg){
        entityManager.merge(msg);
    }
}
