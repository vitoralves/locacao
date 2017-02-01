package br.com.locacao.repositorio;

import br.com.locacao.biblioteca.ReportGenerator;
import br.com.locacao.entidades.Eventos;
import br.com.locacao.entidades.ItensOrcamento;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Asynchronous;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.ImageIcon;

/**
 *
 * @author vitor
 */
public class RepositorioEvento extends RepositorioBasico {

    public static final long serialversionUID = 1L;

    public RepositorioEvento(EntityManager entityManager) {
        super(entityManager);
    }

    public Eventos addEvento(Eventos evento) {
        return addEntity(Eventos.class, evento);
    }

    public Eventos setEvento(Eventos evento) {
        return setEntity(Eventos.class, evento);
    }

    public void removeEvento(Eventos evento) {
        removeEntity(evento);
    }

    public Eventos getEvento(int idEvento) {
        return getEntity(Eventos.class, idEvento);
    }

    public List<Eventos> getEvento() {
        return getPureList(Eventos.class, "select e from Eventos e where e.fgAtivo = 1 order by e.dataEntrega asc");
    }

    public int totalFinalizados(int ano) {
        long total = 0;
        Query query = entityManager.createQuery("select count(e.idEvento) from Eventos e where e.finalizado = true"
                + " and extract(year from e.dataDevolucao) = ?1 ");
        query.setParameter(1, ano);
        total = (long) query.getSingleResult();
        return (int) total;
    }

    public int totalPorAno(int ano) {
        long totalPorAno = 0;
        Query query = entityManager.createQuery("select count(e.idEvento) from Eventos e where extract(year from e.dataEvento) = ?1 ");
        query.setParameter(1, ano);
        totalPorAno = (long) query.getSingleResult();
        return (int) totalPorAno;
    }

    public void finalizaEvento(int id, boolean valor) {
        if (valor == true) {
            Query query = entityManager.createQuery("update Eventos e set e.finalizado = false where e.idEvento = ?1");
            query.setParameter(1, id);
            query.executeUpdate();
        } else {
            Query query = entityManager.createQuery("update Eventos e set e.finalizado = true where e.idEvento = ?1");
            query.setParameter(1, id);
            query.executeUpdate();
        }
    }

    public List<Eventos> getEventosEntregar() {
        Date data = new Date();
        Calendar dataInicial = Calendar.getInstance();
        dataInicial.setTime(data);

        Calendar dataFinal = Calendar.getInstance();
        dataFinal.setTime(data);
        dataFinal.add(Calendar.DAY_OF_MONTH, 7);

        Query query = entityManager.createQuery("select e from Eventos e where e.dataEntrega between ?1 and ?2 "
                + "and e.confirmado = true "
                + "and e.fgAtivo = 1 "
                + "order by e.dataEntrega desc");
        query.setParameter(1, dataInicial.getTime());
        query.setParameter(2, dataFinal.getTime());

        List<Eventos> listEventos = query.getResultList();
        return listEventos;
    }

    public List<Eventos> getEventosRecolher() {
        Date data = new Date();
        Calendar dataInicial = Calendar.getInstance();
        dataInicial.setTime(data);

        Calendar dataFinal = Calendar.getInstance();
        dataFinal.setTime(data);
        dataFinal.add(Calendar.DAY_OF_MONTH, 7);

        Query query = entityManager.createQuery("select e from Eventos e where e.dataDevolucao between ?1 and ?2 "
                + "and e.confirmado = true "
                + "and e.fgAtivo = 1 "
                + "order by e.dataEntrega desc");
        query.setParameter(1, dataInicial.getTime());
        query.setParameter(2, dataFinal.getTime());

        List<Eventos> listEventos = query.getResultList();
        return listEventos;
    }

    public void imprimirCheckList(String titulo, int id_evento, int empresa) {
        Map param = new HashMap();
        ArrayList<HashMap> lst = new ArrayList<HashMap>();
        byte[] img = null;
        byte[] logo = null;

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

        param.put("titulo", titulo);

        List<ItensOrcamento> list = getPureList(ItensOrcamento.class, "select i from ItensOrcamento i "
                + "join i.orcamento o "
                + "join o.evento e "
                + "where e.idEvento = ?1 ", id_evento);

        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                HashMap fields = new HashMap();
                fields.put("cod", list.get(i).getIdItemOrcamento());
                fields.put("nome", list.get(i).getProduto().getNome());
                fields.put("quantidade", list.get(i).getQuantidade());
                lst.add(fields);
            }
        }

        Query qr = entityManager.createQuery("select c.nome, c.cpf, e.endereco, e.localEvento, c.telefone, c.celular, e.idEvento, o.observacao "
                + "from Orcamento o "
                + "join o.evento e "
                + "join e.cliente c "
                + "where e.idEvento = ?1");
        qr.setParameter(1, id_evento);

        List<Object[]> result = qr.getResultList();

        if (result.size() > 0) {
            param.put("cliente", result.get(0)[0]);
            param.put("cpf", result.get(0)[1]);
            param.put("endereco", result.get(0)[2]);
            param.put("local", result.get(0)[3]);
            param.put("contato", result.get(0)[4] + " " + result.get(0)[5]);
            param.put("cod", result.get(0)[6]);
            param.put("obs", result.get(0)[7]);

            ReportGenerator rg = new ReportGenerator();
            rg.jasperReport("checkList", "application/pdf", lst, param, "checkList" + id_evento, "/relatorios/");
        }
    }

    public List<Eventos> getEventoPorCliente(String cliente) {
        return getPureList(Eventos.class, "select e from Eventos e "
                + "join e.cliente c "
                + "where upper(c.nome) like upper(?1) ", "%" + cliente + "%");

    }

    public int getNmrEvtMesAno(int mes, int ano) {
        mes = mes - 1;
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.MONTH, mes);
        gc.set(Calendar.YEAR, ano);
        gc.set(Calendar.DAY_OF_MONTH, 1);
        Date dataInicial = gc.getTime();

        GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.MONTH, mes);
        c.set(Calendar.YEAR, ano);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date dataFinal = c.getTime();

        return getPurePojo(Integer.class, "select cast(count(e.idEvento) as int) from Eventos e "
                + "where e.dataEvento between ?1 and ?2 and e.finalizado = true and e.fgAtivo = 1", dataInicial, dataFinal);
    }
}
