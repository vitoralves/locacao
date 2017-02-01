package br.com.locacao.entidades;

import br.com.locacao.entidades.Empresa;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-19T19:07:26")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-05T22:31:43")
>>>>>>> 60a63e0de8dceb26987bbbb977f0cfea0a59e304
@StaticMetamodel(Usuarios.class)
public class Usuarios_ { 

    public static volatile SingularAttribute<Usuarios, String> senha;
    public static volatile SingularAttribute<Usuarios, String> telefone;
    public static volatile SingularAttribute<Usuarios, Integer> idUsuario;
    public static volatile SingularAttribute<Usuarios, String> cpf;
    public static volatile SingularAttribute<Usuarios, byte[]> imagem;
    public static volatile SingularAttribute<Usuarios, String> celular;
    public static volatile SingularAttribute<Usuarios, Boolean> admin;
    public static volatile SingularAttribute<Usuarios, String> nome;
    public static volatile SingularAttribute<Usuarios, String> sobrenome;
    public static volatile SingularAttribute<Usuarios, Empresa> empresa;
    public static volatile SingularAttribute<Usuarios, String> email;

}