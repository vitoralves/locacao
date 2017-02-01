package br.com.locacao.repositorio;

import br.com.locacao.biblioteca.ReportGenerator;
import br.com.locacao.entidades.ItensOrcamentoApp;
import br.com.locacao.entidades.OrcamentoApp;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.ImageIcon;

/**
 *
 * @author vitor
 */
public class RepositorioOrcamentoApp extends RepositorioBasico {

    public static final long serialversionUID = 1L;

    public RepositorioOrcamentoApp(EntityManager entityManager) {
        super(entityManager);
    }

    public long getQuantidadeOrcamentos() {
        Query q = entityManager.createQuery("select count(o.idOrcamento) from OrcamentoApp o");
        return (long) q.getSingleResult();
    }
    
    public void salvar(OrcamentoApp o){
        entityManager.merge(o);
    }

    public List<OrcamentoApp> getTodosOrcamentosAtivos() {
        Query q = entityManager.createQuery("select o from OrcamentoApp o where o.fgAtivo = true");
        return (List<OrcamentoApp>) q.getResultList();
    }

    public void marcarEnviado(OrcamentoApp orc) {
        entityManager.merge(orc);
    }

    public void imprimeOrcamento(OrcamentoApp orc, String usuario, int empresa) {
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

        param.put("atendente", usuario);
        param.put("data", dataFormatada);
        param.put("cod_orc", orc.getIdOrcamento());
        param.put("dt_evento", orc.getDataEvento());
        param.put("dt_entrega", orc.getDataEntrega());
        param.put("dt_devolucao", orc.getDataDevolucao());
        param.put("retirada", "LOCADORA");
        param.put("devolucao", "LOCADORA");
        param.put("endereco", orc.getEndereco() == null ? "" : orc.getEndereco().toUpperCase());
        param.put("bairro", "");
        param.put("cidade", orc.getCidade());
        param.put("situacao", orc.getSituacao());
        param.put("cod_cliente", orc.getCliente().getIdCliente());
        param.put("nome_cli", orc.getCliente().getNome().toUpperCase()+" "+orc.getCliente().getSobrenome().toUpperCase());
        param.put("nro", "");
        param.put("local", orc.getLocalEvento());
        param.put("desconto", orc.getDesconto());
        param.put("frete", orc.getFrete());
        param.put("total", orc.getTotal());
        param.put("forma_pagto", orc.getFormaPagto());
        param.put("observacao", orc.getObservacao());
        param.put("subtotal", (orc.getTotal()+orc.getDesconto()) - orc.getFrete());


        //Pesquisa produtos        
        Query query3 = entityManager.createQuery("select i from ItensOrcamentoApp i\n"
                + "where i.orcamento.idOrcamento = ?1");
        query3.setParameter(1, orc.getIdOrcamento());
        List<ItensOrcamentoApp> p = query3.getResultList();
        //tirar o for
        if (!p.isEmpty()) {
            for (ItensOrcamentoApp i : p) {
                HashMap fields = new HashMap();
                fields.put("cod_prod", i.getProduto().getIdProduto());
                fields.put("descricao_prod", i.getProduto().getNome());
                fields.put("qntd", i.getQuantidade());
                fields.put("val_unit", i.getProduto().getValDiaria());
                // se não achar nada inicializa como 0
                img = (byte[]) (i.getProduto().getImagem() == null ? new byte[0] : (byte[]) i.getProduto().getImagem());
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
                fields.put("val_total", i.getProduto().getValDiaria() * i.getQuantidade());
                lst.add(fields);
            }
        }

        ReportGenerator rg = new ReportGenerator();
        rg.jasperReport("orcamento", "application/pdf", lst, param, "orcamento", "/relatorios/");
    }
}
