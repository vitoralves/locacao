package br.com.locacao.repositorio;

import br.com.locacao.entidades.ItensOrcamento;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author vitor
 */
public class RepositorioItensOrcamento extends RepositorioBasico {

    public static final long serialversionUID = 1L;

    public RepositorioItensOrcamento(EntityManager entityManager) {
        super(entityManager);
    }

    public ItensOrcamento addItemOrcamento(ItensOrcamento itemItemOrcamento) {
        return setEntity(ItensOrcamento.class, itemItemOrcamento);
    }

    public void addItemOrcamentoParametros(int evento, int produto, int quantidade) {
        String sqlQuery = "inset into ItensOrcamento values (?,?,?)";
        Query q = entityManager.createNativeQuery(sqlQuery);
        q.setParameter(1, produto);
        q.setParameter(2, quantidade);
        q.setParameter(1, evento);
        q.executeUpdate();
    }

    public ItensOrcamento setItemOrcamento(ItensOrcamento itemItemOrcamento) {
        return setEntity(ItensOrcamento.class, itemItemOrcamento);
    }

    public void removeItemOrcamento(ItensOrcamento itemItemOrcamento) {
        removeEntity(itemItemOrcamento);
    }

    public ItensOrcamento getItemOrcamento(int idItemOrcamento) {
        return getEntity(ItensOrcamento.class, idItemOrcamento);
    }

    public List<ItensOrcamento> getItemOrcamento() {
        return getPureList(ItensOrcamento.class, "select i from ItensOrcamento i");
    }

    public List<ItensOrcamento> getItemOrcamentoPorIdOrcamento(int orcamento) {
        return getPureList(ItensOrcamento.class, "select i from ItensOrcamento i where i.orcamento.idOrcamento = ?1", orcamento);
    }

    public int getQuantidadeProdutoPorData(Date dtEntrega, Date dtDevolucao, int produto) {
        Query query = entityManager.createNativeQuery("select sum(i.quantidade) from\n"
                + "itensorcamento i\n"
                + "join orcamento o on i.orcamento = o.id_orcamento\n"
                + "join eventos e on o.evento = e.id_evento\n"
                + "where e.data_entrega >= ?1 and e.data_entrega <= ?2\n"
                + "and i.produto = ?3\n"
                + "and e.fg_ativo = 1 "
                + "and e.finalizado = false ");
        query.setParameter(1, dtEntrega);
        query.setParameter(2, dtDevolucao);
        query.setParameter(3, produto);

        List resultado = query.getResultList();
        long resposta = 0;
        if (resultado.isEmpty()) {
            return 0;
        } else if(resultado.get(0) != null){
            resposta = (long) resultado.get(0);
            return (int) resposta;
        }else{
            return 0;
        }

    }

}
