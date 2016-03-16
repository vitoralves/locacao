package br.com.locacao.entidades;

import br.com.locacao.entidades.Categorias;
import br.com.locacao.entidades.Eventos;
import br.com.locacao.entidades.Fornecedores;
import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-15T19:39:20")
@StaticMetamodel(Produtos.class)
public class Produtos_ { 

    public static volatile SingularAttribute<Produtos, BigInteger> valCusto;
    public static volatile SingularAttribute<Produtos, BigInteger> valDiaria;
    public static volatile SingularAttribute<Produtos, Integer> idProduto;
    public static volatile ListAttribute<Produtos, Eventos> eventosList;
    public static volatile SingularAttribute<Produtos, Categorias> categoria;
    public static volatile SingularAttribute<Produtos, String> nome;
    public static volatile SingularAttribute<Produtos, Fornecedores> fornecedor;
    public static volatile SingularAttribute<Produtos, Integer> quantidade;
    public static volatile SingularAttribute<Produtos, String> descricao;

}