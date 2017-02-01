package br.com.locacao.repositorio;

import br.com.locacao.entidades.ClientesApp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author vitor
 */
public class RepositorioClienteApp extends RepositorioBasico {

    public static final long serialversionUID = 1L;

    public RepositorioClienteApp(EntityManager entityManager) {
        super(entityManager);
    }

    public long getQuantidadeClientes(){
        Query q = entityManager.createQuery("select count(c.idCliente) from ClientesApp c");
        return (long) q.getSingleResult();
    }
}
