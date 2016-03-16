package br.com.locacao.repositorio;

import br.com.locacao.entidades.Fornecedores;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author vitor
 */
public class RepositorioFornecedor extends RepositorioBasico{
    
    public static final long serialversionUID = 1L;
    
    public RepositorioFornecedor(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Fornecedores addFornecedor(Fornecedores fornecedor){
        return setEntity(Fornecedores.class, fornecedor);
    }
    
    public Fornecedores setFornecedor(Fornecedores fornecedor){
        return setEntity(Fornecedores.class, fornecedor);
    }
    
    public void removeFornecedor(Fornecedores fornecedor){
        removeEntity(fornecedor);
    }
    
    public Fornecedores getFornecedor(int idFornecedor){
        return getEntity(Fornecedores.class, idFornecedor);
    }
    
    public List<Fornecedores> getFornecedor(){
        return getPureList(Fornecedores.class, "select f from Fornecedores f");
    }
    
}
