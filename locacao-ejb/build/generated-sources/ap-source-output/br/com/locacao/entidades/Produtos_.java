package br.com.locacao.entidades;

import br.com.locacao.entidades.Categorias;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-10-27T22:42:45")
@StaticMetamodel(Produtos.class)
public class Produtos_ { 

    public static volatile SingularAttribute<Produtos, Double> valCusto;
    public static volatile SingularAttribute<Produtos, Double> valDiaria;
    public static volatile SingularAttribute<Produtos, Integer> idProduto;
    public static volatile SingularAttribute<Produtos, Categorias> categoria;
    public static volatile SingularAttribute<Produtos, byte[]> imagem;
    public static volatile SingularAttribute<Produtos, String> nome;
    public static volatile SingularAttribute<Produtos, Integer> quantidade;
    public static volatile SingularAttribute<Produtos, String> descricao;

}