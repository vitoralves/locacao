package br.com.locacao.repositorio;

import br.com.locacao.entidades.Produtos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author vitor
 */
public class RepositorioProduto extends RepositorioBasico{
    
    public static final long serialversionUID = 1L;
    
    public RepositorioProduto(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Produtos addProduto(Produtos produto){
        return setEntity(Produtos.class, produto);
    }
    
    public Produtos setProduto(Produtos produto){
        return setEntity(Produtos.class, produto);
    }
    
    public void removeProduto(Produtos produto){
        removeEntity(produto);
    }
    
    public Produtos getProduto(int idProduto){
        return getEntity(Produtos.class, idProduto);
    }
    
    public List<Produtos> getProduto(){
        return getPureList(Produtos.class, "select p from Produtos p");
    }
    
    public List<Produtos> getProdutosByName(String nome){
        String nomeFormat = "%"+nome.toUpperCase()+"%";
        return getPureList(Produtos.class, "select p from Produtos p "
                + " where upper(p.nome) like ?1", nomeFormat);
    }
    
    public int getQuantidadeEstoque(){
        Query query = entityManager.createQuery("select sum(p.quantidade) from Produtos p");
        
        List lista = query.getResultList();
        long valor = 0;
        if(lista.get(0) != null){
            valor = (long) lista.get(0);
            return (int) valor;
        }else{
            return 0;
        }
    }
    
    public List<Produtos> get10Produtos(){
        List lista;
        Query query = entityManager.createQuery("select p from Produtos p");
        query.setMaxResults(10);
        lista = query.getResultList();
        return lista;
    }

}
