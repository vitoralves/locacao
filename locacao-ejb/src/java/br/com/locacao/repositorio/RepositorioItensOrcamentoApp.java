package br.com.locacao.repositorio;

import br.com.locacao.entidades.ItensOrcamento;
import br.com.locacao.entidades.ItensOrcamentoApp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author vitor
 */
public class RepositorioItensOrcamentoApp extends RepositorioBasico {

    public static final long serialversionUID = 1L;

    public RepositorioItensOrcamentoApp(EntityManager entityManager) {
        super(entityManager);
    }

    public List<ItensOrcamentoApp> getItemOrcamentoPorIdOrcamento(int orcamento) {
        return getPureList(ItensOrcamentoApp.class, "select i from ItensOrcamentoApp i where i.orcamento.idOrcamento = ?1", orcamento);
    }
    
    public double calculaTotalPorIdOrcamento(int orc){
        double total = 0;
        Query q = entityManager.createQuery("select i from ItensOrcamentoApp i "
                + "where i.orcamento.idOrcamento = ?1 ");
        q.setParameter(1, orc);
        
        List<ItensOrcamentoApp> l = q.getResultList();
        for(ItensOrcamentoApp o : l){
            total += o.getProduto().getValDiaria() * o.getQuantidade();
        }
        
        return total;
    }

}
