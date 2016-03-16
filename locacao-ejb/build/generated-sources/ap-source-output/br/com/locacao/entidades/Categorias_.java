package br.com.locacao.entidades;

import br.com.locacao.entidades.Produtos;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-15T19:39:20")
@StaticMetamodel(Categorias.class)
public class Categorias_ { 

    public static volatile SingularAttribute<Categorias, String> nome;
    public static volatile ListAttribute<Categorias, Produtos> produtosList;
    public static volatile SingularAttribute<Categorias, Integer> idCategoria;

}