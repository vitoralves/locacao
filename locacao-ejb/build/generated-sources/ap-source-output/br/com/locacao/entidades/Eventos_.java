package br.com.locacao.entidades;

import br.com.locacao.entidades.Clientes;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-10-27T22:42:45")
@StaticMetamodel(Eventos.class)
public class Eventos_ { 

    public static volatile SingularAttribute<Eventos, String> tipo;
    public static volatile SingularAttribute<Eventos, String> cidade;
    public static volatile SingularAttribute<Eventos, String> estado;
    public static volatile SingularAttribute<Eventos, Integer> numero;
    public static volatile SingularAttribute<Eventos, String> endereco;
    public static volatile SingularAttribute<Eventos, String> bairro;
    public static volatile SingularAttribute<Eventos, Integer> idEvento;
    public static volatile SingularAttribute<Eventos, Integer> fgAtivo;
    public static volatile SingularAttribute<Eventos, String> localEvento;
    public static volatile SingularAttribute<Eventos, Date> dataDevolucao;
    public static volatile SingularAttribute<Eventos, String> devolucao;
    public static volatile SingularAttribute<Eventos, String> cep;
    public static volatile SingularAttribute<Eventos, Date> dataEvento;
    public static volatile SingularAttribute<Eventos, Clientes> cliente;
    public static volatile SingularAttribute<Eventos, Date> dataEntrega;
    public static volatile SingularAttribute<Eventos, Boolean> confirmado;
    public static volatile SingularAttribute<Eventos, Boolean> finalizado;
    public static volatile SingularAttribute<Eventos, String> retirada;

}