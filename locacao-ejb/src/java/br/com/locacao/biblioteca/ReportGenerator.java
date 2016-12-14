/**
 *
 * @author Vitor
 * @version 2.0
 */
package br.com.locacao.biblioteca;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;


public class ReportGenerator {

    private static String prefix = "/relatorios/";
    private static final String SUFIX = ".jasper";
    private static final String[] VALID_TYPES = {"text/html", "application/pdf", "application/rtf", "application/msword", "application/xls"};

    public void jasperReport(String name, String type, Collection data, String nameReport) {
        jasperReport(name, type, data, new HashMap(), nameReport);
    }

    public void jasperReport(String name, String type, Collection data, Map params, String nameReport, String path) {
        prefix = path;
        jasperReport(name, type, data, params, nameReport);
    }

    public void jasperReport(String name, String type, Collection data, Map params, String nameReport) {
        // Valida o tipo de formato
        boolean found = false;
        for (int i = 0; i < VALID_TYPES.length; i++) {
            if (VALID_TYPES[i].equals(type)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Tipo de relatório inválido");
        }
        // Procura pelo relatório no diretório
        ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
        InputStream stream = econtext.getResourceAsStream(prefix + name + SUFIX);
        if (stream == null) {
            throw new IllegalArgumentException("Caminho de relatório desconhecido " + prefix + name + SUFIX + " requisitado");
        }
        //Preeenche os parâmetros do relatório com o especificado
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(stream, params,
                    new JRBeanCollectionDataSource(data, false));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }

        // Configura o exportador que sera utilizado

        JRExporter exporter = null;
        HttpServletResponse response = (HttpServletResponse) econtext.getResponse();
        FacesContext fcontext = FacesContext.getCurrentInstance();
        try {
            if ("application/pdf".equals(type)) {
                response.setContentType(type);
//                response.setHeader("content-disposition:attachment;","name="+ nameReport + SUFIX); 
                response.setContentType("application/pdf");
                response.setHeader("content-disposition",
                        "inline; name=\"" + nameReport + "\"; "
                        + "filename=\"" + nameReport + ".pdf\"");
                response.setHeader("Cache-Control", "no-cache");

                // foi colocado pois com se trabalha com iframe esta sendo aberto dentro da mesma pagina
                // colocando a instrução no header para que a “disposição” da resposta seja “attachment” (anexo), o browser ao invés de renderizar apenas o conteudo, retorna um arquivo com este texto
                // content-disposition:inline; abre na mesma janela
                exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
                fcontext.responseComplete();
            } else if ("application/rtf".equals(type)) {
                response.setContentType(type);
//                response.setHeader("content-disposition:attachment;","name="+ nameReport + SUFIX); 
                response.setContentType("application/rtf");
                response.setHeader("content-disposition",
                        "inline; name=\"" + nameReport + "\"; "
                        + "filename=\"" + nameReport + ".rtf\"");
                response.setHeader("Cache-Control", "no-cache");

                // foi colocado pois com se trabalha com iframe esta sendo aberto dentro da mesma pagina
                // colocando a instrução no header para que a “disposição” da resposta seja “attachment” (anexo), o browser ao invés de renderizar apenas o conteudo, retorna um arquivo com este texto
                // content-disposition:inline; abre na mesma janela
                exporter = new JRRtfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
                fcontext.responseComplete();
            } else if ("application/xls".equals(type)) {
                response.setContentType(type);
//                response.setHeader("content-disposition:attachment;","name="+ nameReport + SUFIX); 
                response.setContentType("application/rtf");
                response.setHeader("content-disposition",
                        "inline; name=\"" + nameReport + "\"; "
                        + "filename=\"" + nameReport + ".xls\"");
                response.setHeader("Cache-Control", "no-cache");

                // foi colocado pois com se trabalha com iframe esta sendo aberto dentro da mesma pagina
                // colocando a instrução no header para que a “disposição” da resposta seja “attachment” (anexo), o browser ao invés de renderizar apenas o conteudo, retorna um arquivo com este texto
                // content-disposition:inline; abre na mesma janela
                exporter = new JRXlsExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
                fcontext.responseComplete();
            } else if ("application/msword".equals(type)) {
                response.setContentType(type);
//                response.setHeader("content-disposition:attachment;","name="+ nameReport + SUFIX); 
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                response.setHeader("content-disposition",
                        "inline; name=\"" + nameReport + "\"; "
                        + "filename=\"" + nameReport + ".docx\"");
                response.setHeader("Cache-Control", "no-cache");

                // foi colocado pois com se trabalha com iframe esta sendo aberto dentro da mesma pagina
                // colocando a instrução no header para que a “disposição” da resposta seja “attachment” (anexo), o browser ao invés de renderizar apenas o conteudo, retorna um arquivo com este texto
                // content-disposition:inline; abre na mesma janela
                //exporter = new JRRtfExporter();
                exporter = new JRDocxExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
                fcontext.responseComplete();
            } else if ("text/html".equals(type)) {
                exporter = new JRHtmlExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, response.getWriter());
                //Torna as imagens disponiveis para HTML
                HttpServletRequest request = (HttpServletRequest) fcontext.getExternalContext().getRequest();
                request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
                exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());

                // O codigo a seguir requer mapping/image no imageServlet em web.xml
                //
                // Esse servlet inclui imagens de 1 px para espacamento
                //
                // Serve a imagem diretamente, evitando trabalho associado com requisicoes JSF ou nao JSF
                exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath() + "/img?image=");
                fcontext.responseComplete();
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

        try {
            exporter.exportReport();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

        fcontext.responseComplete();

    }
}
