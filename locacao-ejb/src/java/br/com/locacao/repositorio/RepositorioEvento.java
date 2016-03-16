package br.com.locacao.repositorio;

import br.com.locacao.entidades.Eventos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author vitor
 */
public class RepositorioEvento extends RepositorioBasico{
    
    public static final long serialversionUID = 1L;
    
    public RepositorioEvento(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Eventos addEvento(Eventos evento){
        return setEntity(Eventos.class, evento);
    }
    
    public Eventos setEvento(Eventos evento){
        return setEntity(Eventos.class, evento);
    }
    
    public void removeEvento(Eventos evento){
        removeEntity(evento);
    }
    
    public Eventos getEvento(int idEvento){
        return getEntity(Eventos.class, idEvento);
    }
    
    public List<Eventos> getEvento(){
        return getPureList(Eventos.class, "select e from Eventos e");
    }
    
}
