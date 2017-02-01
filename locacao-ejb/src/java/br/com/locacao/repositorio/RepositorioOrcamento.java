package br.com.locacao.repositorio;

import br.com.locacao.biblioteca.ReportGenerator;
import br.com.locacao.entidades.Orcamento;
import br.com.locacao.repositorio.RepositorioEvento;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.swing.ImageIcon;

public class RepositorioOrcamento extends RepositorioBasico {

    public static final long serialVersionUID = 1L;
    
    @EJB
    private RepositorioEvento repositorioEvt;

    public RepositorioOrcamento(EntityManager entityManager) {
        super(entityManager);
    }

    public Orcamento getOrcamento(int id) {
        return getEntity(Orcamento.class, id);
    }

    public Orcamento setOrcamento(Orcamento orc) {
        return setEntity(Orcamento.class, orc);
    }

    public void removeOrcamento(Orcamento orc) {
        removeEntity(orc);
    }

    public Orcamento addOrcamento(Orcamento orc) {
        return addEntity(Orcamento.class, orc);
    }

    public List<Orcamento> getOrcamentoParametro(String nome) {
        return getPureList(Orcamento.class, "select u from Orcamento u where upper(u.nome) like upper(?1) ", nome + "%");
    }

    public List<Orcamento> getOrcamento() {
        return getPureList(Orcamento.class, "select orc from Orcamento orc order by orc.nome");
    }

    public Orcamento getOrcamentoPorEvento(int idEvento) {
        Query query = entityManager.createQuery("select o from Orcamento o where o.evento.idEvento = ?1");
        query.setParameter(1, idEvento);
        List<Orcamento> orcamento = query.getResultList();

        if (orcamento.size() > 0) {
            return orcamento.get(0);
        } else {
            return null;
        }
    }

    public void confirma(int idEvento) {
        Query qr = entityManager.createQuery("update Eventos e set e.confirmado = true where e.idEvento = ?1");
        qr.setParameter(1, idEvento);
        qr.executeUpdate();
    }

    public void imprimeOrcamento(int orc, int evt, String usuario, int empresa) {
        Date data = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = dt.format(data);
        byte[] img = null;
        byte[] logo = null;

        Map param = new HashMap();
        ArrayList<HashMap> lst = new ArrayList<HashMap>();

        //comando para buscar logo no BD
        Query queryLogo = entityManager.createNativeQuery("select e.logo from Empresa e where e.id_empresa = ?1");
        queryLogo.setParameter(1, empresa);
        byte[] l = (byte[]) queryLogo.getSingleResult();
        if (l.length > 0) {
            logo = l;
            if (logo.length > 0) {
                ImageIcon imgLogo = new ImageIcon();
                imgLogo.setImage(Toolkit.getDefaultToolkit().createImage(logo));
                if (imgLogo.getImage() != null) {
                    param.put("logo", imgLogo.getImage());
                }
            }
        }

        Query query1 = entityManager.createNativeQuery("select e.id_evento, e.data_evento, e.data_entrega, e.data_devolucao, e.retirada, e.devolucao, "
                + "e.endereco, e.bairro, e.cidade, e.finalizado, c.id_cliente, c.nome, e.numero, e.local_evento\n"
                + "from Eventos e\n"
                + "join Clientes c  on e.cliente = c.id_cliente "
                + "where e.id_evento = ?1 "
                + "and fg_ativo = 1");
        query1.setParameter(1, evt);
        List<Object[]> r = query1.getResultList();
        //tirar o for
        if (!r.isEmpty()) {
            param.put("atendente", usuario);
            param.put("data", dataFormatada);
            param.put("cod_orc", r.get(0)[0]);
            param.put("dt_evento", r.get(0)[1]);
            param.put("dt_entrega", r.get(0)[2]);
            param.put("dt_devolucao", r.get(0)[3]);
            param.put("retirada", r.get(0)[4]);
            param.put("devolucao", r.get(0)[5]);
            param.put("endereco", r.get(0)[6]);
            param.put("bairro", r.get(0)[7]);
            param.put("cidade", r.get(0)[8]);
            param.put("situacao", r.get(0)[9]);
            param.put("cod_cliente", r.get(0)[10]);
            param.put("nome_cli", r.get(0)[11]);
            param.put("nro", r.get(0)[12]);
            param.put("local", r.get(0)[13]);

        }

        //Pesquisa dados do orcamento        
        Query query2 = entityManager.createNativeQuery("select o.desconto,o.val_frete,o.val_total, o.forma_pagto, o.observacao, "
                + "(o.val_total+o.desconto)-o.val_frete\n"
                + "from orcamento o\n"
                + "where o.id_orcamento = ?1");
        query2.setParameter(1, orc);
        List<Object[]> o = query2.getResultList();
        if (!o.isEmpty()) {
//            HashMap fields = new HashMap();
            param.put("desconto", o.get(0)[0]);
            param.put("frete", o.get(0)[1]);
            param.put("total", o.get(0)[2]);
            param.put("forma_pagto", o.get(0)[3]);
            param.put("observacao", o.get(0)[4]);
            param.put("subtotal", o.get(0)[5]);
//            lst.add(fields);
        }

        //Pesquisa produtos        
        Query query3 = entityManager.createNativeQuery("select i.produto, p.nome, i.quantidade, p.val_diaria,p.imagem, i.quantidade*p.val_diaria\n"
                + "from ItensOrcamento i\n"
                + "join produtos p on i.produto = p.id_produto\n"
                + "where i.orcamento = ?1");
        query3.setParameter(1, orc);
        List<Object[]> p = query3.getResultList();
        //tirar o for
        if (!p.isEmpty()) {
            for (int i = 0; i < p.size(); i++) {
                HashMap fields = new HashMap();
                fields.put("cod_prod", p.get(i)[0]);
                fields.put("descricao_prod", p.get(i)[1]);
                fields.put("qntd", p.get(i)[2]);
                fields.put("val_unit", p.get(i)[3]);
                // se não achar nada inicializa como 0
                img = (byte[]) (p.get(i)[4] == null ? new byte[0] : (byte[]) p.get(i)[4]);
                if (img.length > 0) {
                    ImageIcon imgProd = new ImageIcon();
                    imgProd.setImage(Toolkit.getDefaultToolkit().createImage(img));
                    if (imgProd.getImage() != null) {
                        fields.put("img_prod", imgProd.getImage());
                    }
                } else {
//                    //resgatar o caminho do contexto para passar para o relatório e não zikar em produção
//                    FacesContext facesContext = FacesContext.getCurrentInstance();
//                    ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
//                    String pathRel = servletContext.getRealPath("/resouces/dist/img/imgPadrao.png");
//                    fields.put("img_prod", new byte[0]);
                }
                fields.put("val_total", p.get(i)[5]);
                lst.add(fields);
            }
        }

        ReportGenerator rg = new ReportGenerator();
        rg.jasperReport("orcamento", "application/pdf", lst, param, "orcamento", "/relatorios/");
    }

    public void imprimeContrato(int evt, int empresa) throws InterruptedException {
        BigDecimal somaValorBD = BigDecimal.ZERO;
        BigDecimal somaValorD = BigDecimal.ZERO;
        double somaValorT = 0;
        double somaValor = 0; //valor total de venda dos itens
        double somaValorDiaria = 0; //valor total de venda dos itens
        Date data = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = dt.format(data);
        byte[] img = null;
        byte[] logo = null;

        Map param = new HashMap();
        ArrayList<HashMap> lst = new ArrayList<HashMap>();

        //resgatar o caminho do contexto para passar para o relatório e não zikar em produção
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String pathRel = servletContext.getRealPath("/relatorios/");

        param.put("SUBREPORT_DIR", pathRel);

        //comando para buscar logo no BD
        Query queryLogo = entityManager.createNativeQuery("select e.logo from Empresa e where e.id_empresa = ?1");
        queryLogo.setParameter(1, empresa);
        byte[] l = (byte[]) queryLogo.getSingleResult();
        if (l.length > 0) {
            logo = l;
            if (logo.length > 0) {
                ImageIcon imgLogo = new ImageIcon();
                imgLogo.setImage(Toolkit.getDefaultToolkit().createImage(logo));
                if (imgLogo.getImage() != null) {
                    param.put("logo", imgLogo.getImage());
                }
            }
        }

        Query query1 = entityManager.createNativeQuery("select c.nome, c.endereco, c.bairro, c.estado, c.cpf, c.numero, e.data_entrega,"
                + " e.data_devolucao, e.endereco, e.numero, e.bairro, e.cidade, e.estado, o.forma_pagto, o.val_total, o.val_frete, o.desconto, e.local_evento, e.id_evento, c.telefone, c.celular \n"
                + "from itensorcamento i\n"
                + "join orcamento o on i.orcamento = o.id_orcamento\n"
                + "join produtos p on i.produto = p.id_produto\n"
                + "join eventos e on o.evento = e.id_evento\n"
                + "join clientes c on e.cliente = c.id_cliente\n"
                + "where e.id_evento = ?1\n"
                + "group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18, 19,20,21");
        query1.setParameter(1, evt);
        List<Object[]> r = query1.getResultList();
        //tirar o for
        if (!r.isEmpty()) {
            BigDecimal bd = (BigDecimal) r.get(0)[14];
            BigDecimal ft = (BigDecimal) r.get(0)[15];
            BigDecimal de = (BigDecimal) r.get(0)[16];
            String endereco = r.get(0)[8] == null ? "" : r.get(0)[8].toString()+" ";
            String numero = r.get(0)[9] == null ? "" : r.get(0)[9].toString()+" ";
            String bairro = r.get(0)[10] == null ? "" : r.get(0)[10].toString()+" ";
            String cidade =  r.get(0)[11] == null ? "" : r.get(0)[11].toString()+" ";
            String estado = r.get(0)[12] == null ? "" : r.get(0)[12].toString()+" ";
            String local = r.get(0)[17] == null ? "" :  r.get(0)[17].toString();
            String telefone = r.get(0)[19].toString();
            String celular = r.get(0)[20].toString();
            String contato = telefone.length() > 0 ? ", contato: "+telefone : "";
            contato += celular.length() > 0 ? " "+celular : "";
            if (telefone.length() == 0 && celular.length() > 0){
                contato = ", contato "+celular;
            }

            param.put("cliente", r.get(0)[0]);
            param.put("cpf", r.get(0)[4]);
            param.put("endereco", r.get(0)[1].toString() + " " + r.get(0)[5].toString() + " " + r.get(0)[2].toString() + " " + r.get(0)[3].toString());
            param.put("dataEntrega", r.get(0)[6]);
            param.put("dataDevolucao", r.get(0)[7]);
            param.put("localEvento",  endereco + numero + bairro + cidade + estado + local);
            param.put("formaPagmto", r.get(0)[13]);            
            param.put("totalLocacao", bd.doubleValue());
            param.put("frete", ft.doubleValue());
            param.put("desconto", de.doubleValue());
            param.put("cod", r.get(0)[18]);
            param.put("telefone", contato);

        }
        //Pesquisa produtos        
        Query query3 = entityManager.createNativeQuery("select i.quantidade, p.nome, p.val_custo,p.val_diaria, p.val_diaria*i.quantidade\n"
                + "from itensorcamento i\n"
                + "join orcamento o on i.orcamento = o.id_orcamento\n"
                + "join produtos p on i.produto = p.id_produto\n"
                + "join eventos e on o.evento = e.id_evento\n"
                + "where e.id_evento = ?1");
        query3.setParameter(1, evt);
        List<Object[]> p = query3.getResultList();
        //tirar o for
        if (!p.isEmpty()) {
            for (int i = 0; i < p.size(); i++) {
                HashMap fields = new HashMap();
                fields.put("qntd", p.get(i)[0]);
                fields.put("produto", p.get(i)[1]);
                fields.put("valUnitario", p.get(i)[2]);
                fields.put("valDiaria", p.get(i)[3]);
                fields.put("valTotal", p.get(i)[4]);
                somaValorBD = (BigDecimal) p.get(i)[2];
                somaValorD = (BigDecimal) p.get(i)[3]; //somar o valor da diária
                BigDecimal val =  (BigDecimal) p.get(i)[4];
                somaValorT += val.doubleValue(); //somar o Total                
                somaValor += somaValorBD.doubleValue();
                somaValorDiaria += somaValorD.doubleValue();
                lst.add(fields);
            }
        }

        param.put("somaValor", somaValor);
        param.put("somaValorDiaria", somaValorDiaria);
        param.put("somaValorTotal", somaValorT);
        param.put("texto", "1) A LOCADORA loca para o(a) LOCATÁRIO(A) os móveis e objetos relacionados no campo 3, pelo prazo constante no campo 4 e pelo preço e forma de pagamento constante no campo 5, todos do presente contrato, assumindo, portanto e desde já, o(a) LOCATÁRIO(A), o encargo e responsabilidade civil e criminal de fiel depositário de tais móveis e objetos locados, na forma da lei. O local do evento e ou da entrega/remoção dos bens locados, é o constante do campo 4 do presente contrato.\n"
                + "1.1)Fica certo e ajustado entre as partes que, será acrescido no preço total da locação, as despesas que a LOCADORA tiver com a entrega e ou com a remoção de tais móveis e objetos locados, onde serão cobrados os respectivos fretes, obrigando-se desde já o(a) LOCATÁRIO(A), pelo respectivo pagamento, no ato da realização de tal(is) frete(s), cuja quitação será outorgada pela LOCADORA, através de fornecimento de recibo ou de nota fiscal à parte, no ato do pagamento.\n"
                + "1.1.2)Em decorrência do que está previsto nas cláusulas 1.5; 7; 7.1; 7.1.2 e 8 deste contrato, fica certo e entendido pelo(a) LOCATÁRIO(A), que este se obriga desde já à disponibilizar no mínimo 01 (uma) pessoa de sua confiança, podendo ser seu funcionário, preposto ou terceiro por ela contratado ou subcontratado, para acompanhar tanto a entrega/retirada quanto a devolução/remoção, quer por parte da LOCADORA quer por parte do(a) LOCATÁRIO(A), dos bens locados, até ou junto ao local do evento, inclusive durante todos o percurso de ida e de volta, sob pena de não poder efetuar qualquer tipo de reclamação, ressalva e ou exigência de eventuais direitos que entenda ter, especialmente nos casos em que a LOCADORA tiver que acionar qualquer uma das cláusulas 1.5;7;7.1.2 e 8 do presente contrato.\n"
                + "1.2-) Fica certo e entendido entre as partes que, em havendo necessidade da(o) LOCATÁRIA(O) de efetuar a locação de móveis ou objetos extras, seja à pedido de terceiros contratados ou subcontratados por ela, tais como exemplos, cerimonialistas, decoradores, etc. deverá ao LOCATÁRlA(O) comunicar a LOCADORA tempestivamente e necessariamente por escrito, sendo que, em sendo possível e aceito pela LOCADORA o documento que será emitido pela mesma, retratando esses moveis ou objetos extras, bem como, o valor total da locação extra, valerá como aditamento ao presente contrato, para todos fins, e efeitos de direito, independentemente de conter ou não a assinatura do(a) LOCATARIA(O), podendo esse aditamento, inclusive, ser documentado por fax simile, e-mail, telegrama, etc., permanecendo inalteradas, no mais, as demais cláusulas e condições do presente contrato.\n"
                + "1.3-) O não pagamento de uma parcela autoriza o vencimento antecipado das demais parcelas, sendo que, além da execução ou cobrança judicial da divida, sera cobrado ainda da(o) LOCATARIA(O) uma multa de 10% (dez por cento), mais juros de mora de 1% (um por cento) ao mês e correção monetária plena, calculada pela variação plena do IGP-M, da Fundação Getúlio Vargas, e, ainda, 10% (dez por cento) de honorários advocatícios, tudo à ser calculado sobre o total devido.\n"
                + "1.4-) Tambem será cobrado da(o) LOCATÁRlA(O) uma multa de 50% (cinquenta por cento) do valor do presente contrato, por dia de atraso na devolução dos móveis e objetos locados.\n"
                + "1.4.1-) Caso a(o) LOCATÁRIA(O) desejar prorrogar a locação de tais móveis ou objetos, deverá consultar primeiro a LOCADORA, afim de obter informação sobre tal possibilidade, sendo que, em sendo possível e ou ser aceito pela LOCADORA, deverá a(o) LOCATARIA(O) obter autorização expressa da LOCADORA, hipótese em que. as partes deverão necessariamente emitir e assinar um termo de aditamento ao presente contrato. podendo, em caráter excepcional, ser documentado esse aditamento, também via fax simile. e-mail, telegrama ou outra forma que as partes convencionarem.\n"
                + "1.5-) Fica certo e ajustado entre as partes. que a(o) LOCATÁRIA(O) arcará também com eventuais despesas que a LOCADORA tiver para lavar, passar, encerar, polir, lustrar, desinfetar, higienizar, etc., através de lavanderias ou demais empresas competentes, qualquer móveis ou objeto locado, inclusive para devolver-lhe ao seu estado original, inclusive de uso, higiene, limpeza e consenlação, inclusive com relação as peças de tecidos em geral, tais como, cortinas, toalhas, almofadas, tapetes, carpetes, tendas, puff's. etc., que forem devolvidos pela(o) LOCATÁRIA(O) sujos, manchados, amassados, etc. Para tanto, a LOCADORA exibirá á(ao) LOCATÁRlA(O) o orçamento, para que esta(e) efetue o respectivo pagamento, no prazo máximo e improrrogável de 24:O0hs (vinte e quatro horas), também sob pena das providências previstas no presente contrato e na lei civil e processual civil brasileira.sob pena das providências previstas no presente contrato e na lei civil e processual civil brasileira.\n"
                + "1.6-) É expressamente vedado a(ao) LOCATÁRlA(O) o pagamento de qualquer valor, de qualquer natureza, diretamente a funcionários, motoristas ou prepostos da LOCADORA, que não expressamente autorizados pela mesma, ou ainda a terceiros contratados ou subcontratados tanto pela LOCADORA quanto pela(o) LOCATARlA(O), sob pena de não ser considerado como válido eventual pagamento, caso esse não chegue as mãos e poder da LOCADORA, por qualquer motivo que seja.\n"
                + "2-) O valor total da locação dos móveis e objetos locados, bem como, de eventuais fretes realizados pela LOCADORA e ou terceiros subcontratados pela mesma, será devido pela(o) LOCATÁRIA(O), mesmo que esta(e) não venha utilizar os bens locados por qualquer motivo que seja, tais como exemplos: desistência da locação fora do prazo previsto no codigo de defesa do consumidor, não realização do evento (festa, congresso, reunião, convenção, etc), por qualquer motivo que seja, mesmo que por motivos de caso fortuito e ou de força maior.\n"
                + "2.1-) Em havendo desistência tempestiva por parte da(o) LOCATÁRIA(O), ou seja, dentro do prazo previsto pelo codigo de defesa do consumidor, ficará este obrigado a ressarcir a LOCADORA, apenas eventuais despesas com materiais, locomoções, fretes, subcontratação com terceiros, etc., devidamente comprovados pela LOCADORA.\n"
                + "3-) A(O) LOCATÁRlA(O) autoriza expressamente a LOCADORA à emitir e enviar-lhe, inclusive via boletos bancários, a cobrança dos valores devidos pela locação e ou por outros serviços prestados (executados), tais como por exemplos, fretes,  locação extras de móveis e objetos, danos nos bens locados (ver cláusulas 7 e seguintes), etc., sendo que, os boletos bancários poderão conter autorização à instituição bancária de proceder o apontamento e o protesto em caso de não pagamento pela(o) LOCATARIA(O), sem que isso implique em alegação de danos de quaisquer natureza, inclusive morais ou materiais, hipotese em que, a LOCADORA adotará ainda as demais medidas legais cabíveis, podendo, inclusive, emitir e sacar duplicatas mercantis contra a(o) LOCATARlA(O), inclusive para fins de execução judicial, sem prejuízo do que está previsto na cláusula seguinte (cláusula 5) do presente contrato.\n"
                + "4-) As partes atribuem, de comum acordo, força executiva ao presente contrato, nos termos da lei civil e processual civil brasileira, onde a(o) LOCATARIA(O) desde já confessa expressamente dever à LOCADORA, o valor total do presente contrato, inclusive, valores de eventuais fretes e de locações extras de móveis e objetos, reconhecendo-os, desde já, como totalmente líquidos, certos e exigiveis.\n"
                + "5-) A(O) LOCATÁRlA(O) declara já ter vistoriado as condições de uso, limpeza, higiene e conservaação dos móveis e objetos locados, reconhecendo que os mesmos estäo em perfeitoestado de uso e conservação, para assim serem devolvidos a LOCADORA, findo ou rescindido o presente contrato, autorizando expressamente a LOCADORA à deixar os bens locados serem retirados e ou devolvidos, por funcionarios. prepostos ou terceiros contratados ou subcontratados pela(o) LOCATÁRIA(O), o(s) quaI(is) sera(ao) previamente indicado(s) e ou informado(s) verbalmente pela(o) LOCATÁRIA(O), podendo ser por escrito, caso queira optar por esse procedimento, hipotese em que, deverá alertar a LOCADORA dessa opçao, também por escrito.\n"
                + "6-) Eventuais danos, de quaisquer naturezas, que forem causados nos móveis ou objetos locados, seja por culpa ou dolo da(o) LOCATARIA(O), de seus funcionarios, prepostos e ou ainda de eventuais terceiros subcontratados pela(o) mesma(o) será de inteira responsabilidade da(o) LOCATÁRIA(O), onde a LOCADORA poderá, à seu livre criterio, exigir o pagamento do valor comercial de cada unidade danificada (valor comercial que foi estipulado de comum acordo entre as partes, no campo 3 do presente contrato) ou a substituição do móvel ou objeto danificado, por outro idêntico (marca, material, cor, modelo, tamanho, espessura, brilho, qualidade, etc.), também sob pena de adotar as medidas legais cabíveis, ficando ressalvado a(o) LOCATÁRIA(O), eventual direito de regresso contra terceiros, contratados ou subcontratados, etc.\n"
                + "6.1-) Fica certo e livremente ajustado entre as partes que, a(o) LOCATÁRIA(O) se responsabilizará também por eventuais danos, de quaisquer naturezas, que forem causados nos mesmos moveis ou objetos locados, em decorrência de casos fortuitos e ou de força maior, tais como, atos de vandalismos, brigas, discussões, assaltos, furtos, roubos ou extravios dos bens locados, provocados ou não por seus convidados ou por terceiros, contratados ou subcontratados, bem como, danos decorrentes de fenômenos naturais, tais como, chuvas ou tempestades, torrenciais ou não, com granizos ou näo, ventos ou ventanias, tornados ou furacões, abalos sismicos, tremores de terras ou terremotos, desabamentos, explosões, incêndios, quedas de raios, quedas de energia eletrica, rompimentos de canos de água, enfim, por todo e qualquer fenômeno natural ou provocado pelo ser humano, quer de forma culposa, acidental ou quer de forma dolosa.ou não, com granizos ou näo, ventos ou ventanias, tornados ou furacões, abalos sismicos, tremores de terras ou terremotos, desabamentos, explosões, incêndios, quedas de raios, quedas de energia eletrica, rompimentos de canos de água, enfim, por todo e qualquer fenômeno natural ou provocado pelo ser humano, quer de forma culposa, acidental ou quer de forma dolosa.\n"
                + "6.1.2-) Entende-se como danos nos móveis ou objetos locados, quaisquer avarias, cortes, arranhões, trincos, riscos, furos, amassados, quebras, torções, manchas permanentes, descascados, falta de peças, parafusos ou demais acessórios, enfim, qualquer ocorrência que tire a originalidade, inclusive da marca, sua qualidade, tamanho, espessura, cor, brilho, etc. do bem locado.\n"
                + "7-) Fica sob a responsabilidade exclusiva da(o) LOCATÁRIA(O) a remoção, remanejamento, manuseio, instalação e desinstalação, montagem e desmontagem, etc. dos bens locados junto ao local do evento, bem como, toda a orientação tecnica e disciplinar, direçao e organização, para tanto, mesmo que tais serviços sejam efetuado por terceiros, contratados e ou dos subcontratados pela(o) LOCATÁRIA(O), tais como, cerimonialistas, decoradores, instaladores, montadores, etc., ficando ressalvado a(ao) LOCATARlA(0) direito de regresso contra quem de direito.\n"
                + "8-) É expressamente vedada a cessão ou transferencia, total ou parcial, do presente contrato, sob pena de imediata rescisão e demais medidas legais cabíveis.\n"
                + "9-) O presente contrato não têm caráter de exclusividade, podendo, ambas as partes, contratar com terceiros, desde que respeitadas as cláusulas e condições do presente instrumento.\n"
                + "10-) A infração das obrigações assumidas sujeitará a parte infratora, além do pagamento da multa sobre a parcela vencida e mora, acima previstos, ao pagamento de eventuais perdas, danos e lucros cessantes que se apurarem.\n"
                + "11-) A eventual tolerância do presente contrato não constituirá renúncia aos direitos que são conferidos e ambas as partes.\n"
                + "12-) O presente contrato obriga as parte por si, seus herdeiros ou sucessores, na forma da lei.\n"
                + "13-) Fica eleito o foro da cidade de Franca - SP, com renúncia a qualquer outro. E por assim estarem justos, contratados e acordados, as partes firmam o presente instrumento em 2 vias, com um só conteúdo, na presença de 2 (duas) testemunhas abaixo assinados.");

        ReportGenerator rg = new ReportGenerator();
        rg.jasperReport("contrato", "application/pdf", lst, param, "contrato" + evt, "/relatorios/");

    }

}
