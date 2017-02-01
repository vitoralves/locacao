package br.com.locacao.repositorio;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;


public abstract class RepositorioBasico {
    
    public static final long serialVersionUID = 1L;
    
    final EntityManager entityManager;
    public RepositorioBasico(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    
    protected EntityManager getEntityManager(){
        return entityManager;
    }
    
    protected <T> T addEntity(Class<T> classToCast,Object entity){
        getEntityManager().persist(entity);
        return (T) entity;
    }
    
    protected <T> T getEntity(Class<T> classToCast, Serializable pk){
        return getEntityManager().find(classToCast, pk);
    }
    
    protected <T> T setEntity(Class<T> classToCast,Object entity){
        Object updateObj = getEntityManager().merge(entity);
        return (T) updateObj;
    }
    
    protected void removeEntity(Object entity){
        Object updateObj = getEntityManager().merge(entity);
        getEntityManager().remove(updateObj);
    }
    
    protected <T> List<T> getPureList(Class<T> classToCast, String query, Object... values){ //ejbql linguagem jpa
        Query qr = createQuery(query, values);
        return qr.getResultList();
    }
    
    protected <T> T getPurePojo(Class<T> classToCast,String query,Object... values){
        Query qr = createQuery(query, values);
        return (T) qr.getSingleResult();
    }
    
    protected int executeCommand(String query, Object... values){//comandos (insert , update, delete
        Query qr = createQuery(query, values);
        return qr.executeUpdate();
    }
    
    private Query createQuery(String query, Object... values){
        Query qr = getEntityManager().createQuery(query);
        for(int i = 0; i < values.length; i++){
            qr.setParameter(i+1, values[i]);
        }
        return qr;
    }
}
