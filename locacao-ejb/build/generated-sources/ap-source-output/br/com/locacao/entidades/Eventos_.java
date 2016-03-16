package br.com.locacao.entidades;

import br.com.locacao.entidades.Clientes;
import br.com.locacao.entidades.Produtos;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-15T19:39:20")
@StaticMetamodel(Eventos.class)
public class Eventos_ { 

    public static volatile SingularAttribute<Eventos, String> tipo;
    public static volatile SingularAttribute<Eventos, String> formaPagto;
    public static volatile SingularAttribute<Eventos, BigInteger> valTotal;
    public static volatile SingularAttribute<Eventos, Integer> idEvento;
    public static volatile SingularAttribute<Eventos, BigInteger> valFrete;
    public static volatile SingularAttribute<Eventos, Date> dataDevolucao;
    public static volatile SingularAttribute<Eventos, Integer> diarias;
    public static volatile SingularAttribute<Eventos, Date> dataEvento;
    public static volatile SingularAttribute<Eventos, Clientes> cliente;
    public static volatile SingularAttribute<Eventos, Produtos> produto;
    public static volatile SingularAttribute<Eventos, Integer> descontos;
    public static volatile SingularAttribute<Eventos, Date> dataEntrega;
    public static volatile SingularAttribute<Eventos, String> retirada;

}