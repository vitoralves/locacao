package br.com.locacao.entidades;

import br.com.locacao.entidades.Eventos;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-15T19:39:20")
@StaticMetamodel(Clientes.class)
public class Clientes_ { 

    public static volatile SingularAttribute<Clientes, String> tipo;
    public static volatile SingularAttribute<Clientes, String> cidade;
    public static volatile SingularAttribute<Clientes, String> telefone;
    public static volatile SingularAttribute<Clientes, String> estado;
    public static volatile SingularAttribute<Clientes, String> inscricao;
    public static volatile SingularAttribute<Clientes, String> numero;
    public static volatile SingularAttribute<Clientes, String> endereco;
    public static volatile SingularAttribute<Clientes, String> bairro;
    public static volatile SingularAttribute<Clientes, String> nome;
    public static volatile SingularAttribute<Clientes, Integer> cep;
    public static volatile SingularAttribute<Clientes, Integer> idCliente;
    public static volatile ListAttribute<Clientes, Eventos> eventosList;
    public static volatile SingularAttribute<Clientes, String> cpf;
    public static volatile SingularAttribute<Clientes, String> celular;
    public static volatile SingularAttribute<Clientes, String> razaoSocial;
    public static volatile SingularAttribute<Clientes, String> email;

}