package br.com.locacao.controle;

import br.com.locacao.servicos.ServicoEvento;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author vitor
 */
@Named
@ViewScoped
public class ComumControl extends BasicControl implements java.io.Serializable {
    @EJB
    private ServicoEvento evtService;
    private String dataFormatada;
    
    @PostConstruct
    public void init() {
        
    }

    public String formataData(Date data) {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        if (data != null) {
            dataFormatada = dt.format(data);
            return dataFormatada;
        } else {
            return "";
        }
    }

    public String getDataFormatada() {
        return dataFormatada;
    }

    public void setDataFormatada(String dataFormatada) {
        this.dataFormatada = dataFormatada;
    }

    public String anoAtual() {
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy");
        dataFormatada = dt.format(date.getTime());
        return dataFormatada;
    }

    public int getEventosAnoAnterior(int i){
        String ano = anoAtual();
        
        return evtService.getNmrEvtMesAno(i, Integer.parseInt(ano)-1);
    
    } 
    
    public int getEventosAnoAtual(int i){
        String ano = anoAtual();
        
        return evtService.getNmrEvtMesAno(i, Integer.parseInt(ano));
    }
}
