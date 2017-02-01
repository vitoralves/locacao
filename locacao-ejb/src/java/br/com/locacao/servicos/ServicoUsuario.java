package br.com.locacao.servicos;

import br.com.locacao.entidades.Usuarios;
import br.com.locacao.repositorio.RepositorioUsuario;
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
public class ServicoUsuario extends ServicoBasico{
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager em;    
    private RepositorioUsuario usrRepositorio;
    
    @PostConstruct
    @PostActivate
    private void postConstruct(){
        usrRepositorio = new RepositorioUsuario(em);
    }
    
    public ServicoUsuario(){
        
    }
    
    public Usuarios getUsuario(int idUsuario){
        return usrRepositorio.getUsuario(idUsuario);
    }
    
    public Usuarios setUsuario(Usuarios usuario){
        return usrRepositorio.setUsuario(usuario);
    }
    
    public Usuarios alterUsuario(Usuarios usuario){
        return usrRepositorio.alterUsuario(usuario);
    }
    
    public void removeUsuario(Usuarios usuario){
        usrRepositorio.removeUsuario(usuario);
    }
    
    public void setPassword(int idOfUsuario, String password){
        usrRepositorio.setSenha(password, idOfUsuario);
    }
    
    public Usuarios addUsuario(Usuarios usuario){
        return usrRepositorio.addUsuario(usuario);
    }
    
    public Usuarios getUsuarioByLoginSenha(String login, String senha){
        try{
            return usrRepositorio.getUsuarioByLoginSenha(login, senha);
        }catch(Exception e){
            return null;
        }
    }
    
    public List<Usuarios> getUsuarioParametro(String nome){
        try{
            return usrRepositorio.getUsuarioParametro(nome);
        }catch(Exception e){
            return null;
        }
    }
    
    public List<Usuarios> getUsuarios(){
        return usrRepositorio.getUsuarios();
    }
}
