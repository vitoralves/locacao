package br.com.locacao.entidades;

import br.com.locacao.entidades.Produtos;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-15T19:39:20")
@StaticMetamodel(Fornecedores.class)
public class Fornecedores_ { 

    public static volatile SingularAttribute<Fornecedores, String> telefone;
    public static volatile SingularAttribute<Fornecedores, Integer> idFornecedor;
    public static volatile SingularAttribute<Fornecedores, String> inscricao;
    public static volatile SingularAttribute<Fornecedores, String> endereco;
    public static volatile SingularAttribute<Fornecedores, String> cpf;
    public static volatile SingularAttribute<Fornecedores, String> nome;
    public static volatile ListAttribute<Fornecedores, Produtos> produtosList;
    public static volatile SingularAttribute<Fornecedores, String> cnpj;
    public static volatile SingularAttribute<Fornecedores, String> email;
    public static volatile SingularAttribute<Fornecedores, String> cep;

}