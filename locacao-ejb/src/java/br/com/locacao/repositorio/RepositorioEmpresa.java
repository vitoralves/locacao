package br.com.locacao.repositorio;

import br.com.locacao.entidades.Empresa;
import br.com.locacao.entidades.Usuarios;
import java.security.MessageDigest;
import java.util.List;
import javax.persistence.EntityManager;

public class RepositorioEmpresa extends RepositorioBasico{
    
    public static final long serialVersionUID = 1L;
    
    public RepositorioEmpresa(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Empresa getEmpresa(int id){
        return getEntity(Empresa.class, id);
    }
    
    public Empresa setEmpresa(Empresa emp){
        return setEntity(Empresa.class, emp);
    }
    
}
