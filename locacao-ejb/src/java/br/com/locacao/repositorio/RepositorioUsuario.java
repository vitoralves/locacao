package br.com.locacao.repositorio;

import br.com.locacao.entidades.Usuarios;
import java.security.MessageDigest;
import java.util.List;
import javax.persistence.EntityManager;

public class RepositorioUsuario extends RepositorioBasico{
    
    public static final long serialVersionUID = 1L;
    
    public RepositorioUsuario(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Usuarios getUsuario(int id){
        return getEntity(Usuarios.class, id);
    }
    
    public Usuarios setUsuario(Usuarios usr){
        usr.setSenha(getMd5(usr.getSenha()));
        return setEntity(Usuarios.class, usr);
    }
    
    public Usuarios alterUsuario(Usuarios usr){
        return setEntity(Usuarios.class, usr);
    }
    
    public void removeUsuario(Usuarios usr){
        removeEntity(usr);
    }
    
    public Usuarios addUsuario(Usuarios usr){
        usr.setSenha(getMd5(usr.getSenha()));
        return addEntity(Usuarios.class, usr);
    }
    
    public Usuarios getUsuarioByLoginSenha(String login, String senha){
        return getPurePojo(Usuarios.class, "select u from Usuarios u where u.email = ?1 and u.senha = ?2", login, getMd5(senha));
    }
    
    public List<Usuarios> getUsuarioParametro(String nome){
        return getPureList(Usuarios.class, "select u from Usuarios u where upper(u.nome) like upper(?1) ", nome+"%");
    }
    
    public List<Usuarios> getUsuarios(){
        return getPureList(Usuarios.class, "select usr from Usuarios usr order by usr.nome");
    }
    
    public List<Usuarios> getUsuariosByNome(String nome){
        return getPureList(Usuarios.class, "select usr from Usuarios usr where usr.nome like ?1", nome+"%");
    }
    
    public void setSenha(String newSenha, int idOfUsuario){
        String np = getMd5(newSenha);
        Usuarios usr = getUsuario(idOfUsuario);
        usr.setSenha(np);
        setUsuario(usr);
    }
    
    private String getMd5(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return digest;
    }
}
