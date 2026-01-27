package com.example.app_lin_tem.Controllers;


import com.example.app_lin_tem.Componentes.Controller.HitoController;
import com.example.app_lin_tem.Componentes.Controller.PeriodoController;
import com.example.app_lin_tem.Componentes.Controller.ProyectosNubeController;
import com.example.app_lin_tem.Componentes.HitoUI;
import com.example.app_lin_tem.Componentes.Lineas;
import com.example.app_lin_tem.Componentes.PeriodoUI;
import com.example.app_lin_tem.Componentes.HitoLineas;
import com.example.app_lin_tem.Model.Hito;
import com.example.app_lin_tem.Model.JsonMaker;
import com.example.app_lin_tem.Model.Periodo;
import com.example.app_lin_tem.Model.Proyecto;
import com.example.app_lin_tem.Model.firebase.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrofit2.Response;


import javax.imageio.ImageIO;
import java.io.*;
import java.time.LocalTime;
import java.util.*;

public class MainViewController {

    private ArrayList<Periodo> periodos;
    private ArrayList<Hito> hitos;
    private Proyecto proyectoActual;
    private HashMap<String,Node> vistas;
    private HashMap<String,PeriodoController> controladoresPer;
    private HashMap<String,HitoController> controladoresHit;
    private double duracionMin=1,fechaMin=1,fechaMax;
    private int i=1;
    private String idToken,refreshToken,uID;
    private LocalTime timer;
    final private String APIKEY="AIzaSyDkC0ZFDN4dNcQCyaLdpRWZpUQ_p_r_O3U";
    private fireBaseData dbApi;
    private fireBaseAuth authApi;
    private fireBaseTokenApi tokenApi;

    final private HashMap<Integer, Color> tablaColores= new HashMap<>() {
            {put(1,Color.RED);
            put(2,Color.BLUE);
            put(3,Color.GREEN);
            put(4,Color.TEAL);
            put(5, Color.YELLOW);
            put(6, Color.MAROON);
            put(7, Color.PINK);
            put(8, Color.PURPLE);
            put(9, Color.ORANGE);
            put(10, Color.LIME);}
    };

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private VBox vboxData;

    @FXML
    private ScrollPane contendorLineas, scrollData;

    @FXML
    private AnchorPane jaulaLineas;

    @FXML
    public TextField nomProyecto;

    public MainViewController(){
        periodos= new ArrayList<Periodo>();
        hitos= new ArrayList<Hito>();
        proyectoActual= new Proyecto(UUID.randomUUID().toString(),"Sin nombre",periodos,hitos);
        vistas= new HashMap<>();
        controladoresPer = new HashMap<>();
        controladoresHit = new HashMap<>();


    }

    /**
     * Método llamado tras la carga del FXML.
     * Configura bindings visuales, APIs de Firebase y valores iniciales.
     */
    public void postInit(){
        contendorLineas.setScaleY(-1);
        contendorLineas.vvalueProperty().bind(jaulaLineas.heightProperty());
        contendorLineas.hvalueProperty().bind(jaulaLineas.widthProperty());
        idToken ="";
        dbApi=retroFitClient.databaseApi();
        authApi=retroFitClient.authApi();
        tokenApi=retroFitClient.tokenApi();
        nomProyecto.setText("Sin nombre");

    }

    /**
     * Actualiza el nombre del proyecto cuando se modifica el campo de texto.
     * Si el campo está vacío, se asigna "Sin nombre".
     */
    @FXML
    protected void onKeyPressNomProyecto(){
        if(!nomProyecto.getText().equals("")){
            proyectoActual.setNombre(nomProyecto.getText());
        }
        else{
            proyectoActual.setNombre("Sin nombre");
            nomProyecto.setText("Sin nombre");
        }
    }

    /**
     * Añade un nuevo periodo al proyecto y a la interfaz gráfica.
     */
    @FXML
    protected void onButtonPeridoClick() throws IOException {
        scrollData.setVisible(true);
        PeriodoUI periodoUI = new PeriodoUI();
        vboxData.getChildren().add(periodoUI);


        PeriodoController periodoController = periodoUI.ctr;
        periodos.add(periodoController.getPeriodo());
        vistas.put(periodoController.getPeriodo().getId(),periodoUI);
        controladoresPer.put(periodoController.getPeriodo().getId(),periodoController);


        periodoController.setMainViewController(this);
        periodoController.setPeriodos(periodos);
        periodoController.newColor(tablaColores.get(i));
        periodoController.setFechasIni();
        periodoUI.requestFocus();

        i=i+1;
        if(i>10){i=1;}
    }

    /**
     * Añade un nuevo hito al proyecto y a la interfaz gráfica.
     */
    @FXML
    protected void onButtonHitoClick() throws IOException {
        scrollData.setVisible(true);
        HitoUI hitoUI = new HitoUI();
        vboxData.getChildren().add(hitoUI);

        HitoController hitoController = hitoUI.ctr;
        hitos.add(hitoController.getHito());
        vistas.put(hitoController.getHito().getId(),hitoUI);
        controladoresHit.put(hitoController.getHito().getId(),hitoController);

        hitoController.setMainViewController(this);
        hitoController.setFechaIni();
        hitoController.setPeriodos(periodos);

    }

    /**
     * Construye la línea temporal visual:
     * - Calcula alturas
     * - Posiciones
     * - Añade periodos e hitos al panel gráfico
     */
    @FXML
    protected void onButtonCrearCLick(){
        scrollData.setVisible(false) ;

        jaulaLineas.getChildren().clear();
        calcularAlturasPrimarios(crearArrayOrdenadoLineas());
        alturaHitos();

        for(Periodo periodo:periodos){
            Lineas linea = new Lineas(periodo);
            linea.setLayoutX(getPosicion(periodo.getFecha1()));
            if(!periodo.getHitosDependientes().isEmpty()){
                //periodo.setAlturaMaxHit();
            }

            linea.setLayoutY(periodo.getAltura());
            linea.setScaleY(-1);
            linea.ctr.setTamaño(getTamaño(periodo.getDuracion()));

            jaulaLineas.getChildren().add(linea);
            setDurFchMinFchMax(periodo.getDuracion(),periodo.getFecha1(),periodo.getFecha2());
        }
        for(Hito hito:hitos) {
            if(hito.getAlturaDep()==0){
                hito.setAlturaDep(10);
                hito.setAltura(58);
                for(Periodo periodo: crearArrayOrdenadoLineas()){
                    if(hito.getFecha()>=periodo.getFecha1()&&hito.getFecha()<=periodo.getFecha2()){
                        hito.setAltura(Math.max(hito.getAlturaDep(),periodo.getAlturaMax()-78));
                        hito.setAlturaDep(-10);
                        if(hito.getAltura()==0){
                            hito.setAltura(58);
                            hito.setAlturaDep(10);
                        }
                    }
                }
                setDurFchMinFchMax(0.0,hito.getFecha(),hito.getFecha());
            }
            HitoLineas hitoLinea= new HitoLineas(hito);
            double x =getPosicion(hito.getFecha());
            double l=hitoLinea.getPrefWidth()/2;
            x= x-(l);

            hitoLinea.setLayoutX(x) ;
            hitoLinea.setLayoutY(hito.getAltura());
            hitoLinea.setScaleY(-1);

            jaulaLineas.getChildren().add(hitoLinea);
            hitoLinea.inicioHito(this);

        }
    }

    /**
     * Envía un nodo al frente o al fondo del contenedor.
     * @param node Nodo a modificar
     * @param Front true para traer al frente, false para enviar atrás
     */
    public void sendFrontOrBack(Node node,boolean Front){
        if(Front){
            node.toFront();}
        else{
            node.toBack();
        }
    }

    /**
     * Calcula la altura de los hitos evitando colisiones.
     */
    public void alturaHitos(){
        for(Hito hito: crearArrayOrdenadoHito()){
            hito.setAltura(40);
            hito.setAlturaDep(146-40);
            hito.setAlturaFinal(146-40);
            for(Periodo periodo: crearArrayOrdenadoLineas()){
                if(hito.getFecha()>=periodo.getFecha1()&&hito.getFecha()<=periodo.getFecha2()){
                    hito.setAlturaDep(Math.max(hito.getAlturaDep(),periodo.getAlturaMax()+48));
                    hito.setAlturaFinal(hito.getAlturaDep());
                }
            }
        }
        for(Hito hito: crearArrayOrdenadoHito()){
            for(Hito hit: crearArrayOrdenadoHito()){
                if(((hito.getFecha()==hit.getFecha())&&(hit.getAlturaFinal()== hito.getAlturaFinal()))&&hito!=hit){
                    hit.setAlturaDep(hit.getAlturaDep()+30);
                    hit.setAlturaFinal(hit.getAlturaDep());
                }
            }
        }
    }

    /**
     * Calcula las alturas de los hitos dependientes de un periodo.
     * @param periodo Periodo padre
     */
    public void calcularAlturasHitosDep(Periodo periodo){
        ArrayList<Hito> cajaHitos = new ArrayList<>();
        for(Hito hito:hitos){
            if(hito.getPadreCaja(hito.getDependencia())==periodo){
                cajaHitos.add(hito);
            }
        }
        for(Hito hito:cajaHitos){
            hito.setAltura(hito.getDependencia().getAltura()+58);
            double alturaFinal= (periodo.getAlturaMax()+88)-hito.getAltura();
            hito.setAlturaFinal(periodo.getAlturaMax()+88);
            hito.setAlturaDep(alturaFinal);

        }
        for(Hito hito:cajaHitos){
            for(Hito hit:cajaHitos){
                if(((hito.getFecha()==hit.getFecha())&&(hit.getAlturaFinal()== hito.getAlturaFinal()))&&hito!=hit){
                    hit.setAlturaDep(hit.getAlturaDep()+30);
                    hit.setAlturaFinal(hit.getAlturaDep());

                }
            }
            hito.getPadreCaja(hito.getDependencia()).setAlturaMaxcaja(hito.getAlturaFinal());
        }
    }

    /**
     * Calcula las alturas base de los periodos principales y sus dependencias.
     * @param periodosCalcular Lista de periodos padre
     */
    public void calcularAlturasPrimarios(ArrayList<Periodo> periodosCalcular){
        double alturaMax=0;
        for(Periodo periodo:periodosCalcular){
            periodo.setAltura(58);
            periodo.setAlturaMax();

            if(!periodo.getPeriodosDependientes().isEmpty()){
                calcularAlturaDependientes(periodo.getAltura()+58,periodo.getPeriodosDependientes());
                periodo.setAlturaMax();
            }

        }
        for(Periodo periodo:periodosCalcular){
            calcularAlturasHitosDep(periodo);
        }
        for(Periodo periodo: periodosCalcular){
            ArrayList<Periodo> periodosCoincidentes= new ArrayList<>();

            for(Periodo per:periodosCalcular){
                double f1p=periodo.getFecha1();
                double f2p=periodo.getFecha2();

                double f1E=per.getFecha1();
                double f2E=per.getFecha2();

                if(periodo!=per){
                    //per que la primera fecha es compartida por el periodo
                    if(((f1E>=f1p)&(f1E<f2p))){

                        periodosCoincidentes.add(per);
                    }
                }
            }

            for(Periodo altura:periodosCoincidentes){
                //subimos a los periodos coincidentes
                if((periodo.getAltura()<=altura.getAlturaMax())){
                    altura.addAlturaColision(periodo.getAlturaMax()+78);
                    altura.setAlturaMax();
                }
            }
        }

        ArrayList<Periodo> periodosCalcularInver = periodosCalcular;
        periodosCalcularInver.sort((linea1,linea2)->{
            return linea2.getFecha2()-linea1.getFecha2();
        });
        for(Periodo periodo: periodosCalcularInver){
            ArrayList<Periodo> periodosCoincidentes= new ArrayList<>();

            for(Periodo per:periodosCalcularInver){
                double f1p=periodo.getFecha1();
                double f2p=periodo.getFecha2();

                double f1E=per.getFecha1();
                double f2E=per.getFecha2();

                if(periodo!=per){
                    //per que la primera fecha es compartida por el periodo
                    if(((f2E>f1p)&(f2E<=f2p))&&(periodo.getAltura()<=per.getAlturaMax())){
                        periodosCoincidentes.add(per);

                    }
                }
            }

            for(Periodo altura:periodosCoincidentes){
                //subimos a los periodos coincidentes
                if((altura.getAltura()<=periodo.getAlturaMax())){
                    altura.addAlturaColision(periodo.getAlturaMax()+78);
                    altura.setAlturaMax();
                }
            }
        }
    }

    /**
     * Calcula alturas de periodos dependientes evitando colisiones.
     * @param alturaIni Altura inicial
     * @param lista Lista de periodos dependientes
     */
    public void calcularAlturaDependientes(double alturaIni, ArrayList<Periodo> lista){


        for(Periodo periodo: lista){
            int iter=0;
            periodo.setAltura(alturaIni);
            periodo.setAlturaMax();

            ArrayList<Periodo> periodosCoincidentes = new ArrayList<>();
            boolean colisiona=false;
            while(periodo!=lista.get(iter)){
                int f1d=periodo.getFecha1();
                int f2d=periodo.getFecha2();

                int f1p=lista.get(iter).getFecha1();
                int f2p=lista.get(iter).getFecha2();


                if(((f1p<=f1d)&(f1d<f2p))|((f1p<f2d)&(f2d<=f2p))){
                    periodosCoincidentes.add(lista.get(iter));
                    colisiona=true;
                }
                iter++;
            }

            boolean hueco=true;


            for(Periodo altura:periodosCoincidentes){
                if((altura.getAltura()==periodo.getAltura())|((altura.getAltura()<=periodo.getAltura())&&(periodo.getAltura()<=altura.getAlturaMax()))){
                    periodo.addAltura(altura.getAlturaMax()+58);
                    periodo.setAlturaMax();
                    hueco=false;
                }
            }
            if(!periodo.getPeriodosDependientes().isEmpty()){
                calcularAlturaDependientes(periodo.getAltura()+58,periodo.getPeriodosDependientes());
                periodo.setAlturaMax();
            }

            if(colisiona&&(!periodo.getPeriodosDependientes().isEmpty()&&hueco)){
                for (Periodo per:periodosCoincidentes){
                    per.addAltura(periodo.getAlturaMax()+58);
                    per.setAlturaMax();
                }
            }
            periodo.setAlturaMax();
        }


    }

    /**
     * Calcula el ancho visual de un periodo en función de su duración.
     * @param Duracion Duración del periodo
     * @return Ancho en píxeles
     */
    private double getTamaño(double Duracion){
        return 200*(Duracion /duracionMin);
    }

    /**
     * Calcula la posición horizontal en función de la fecha.
     * @param Fecha1 Fecha inicial
     * @return Posición X
     */
    private double getPosicion(int Fecha1){

        return Math.abs((((Fecha1)/duracionMin)*200+20)-(((fechaMin)/duracionMin)*200)) ;
    }

    /**
     * Muestra o Oculta scrollData
     */
    @FXML
    protected void onActionBottonMostrar(){
        scrollData.setVisible(!scrollData.isVisible());

    }

    /**
     * Genera un ArrayList con los periodos que no tienen dependencia (sin padre),
     * ordenados primero por la fecha inicial (fecha1) de forma ascendente y,
     * en caso de empate, por la duración en orden descendente.
     *
     * @return lista de periodos sin dependencia ordenados por fecha y duración
     */
    public ArrayList<Periodo> crearArrayOrdenadoLineas(){
        ArrayList<Periodo> lineas = new ArrayList<>();
        for(Periodo periodo : periodos){
            if (periodo.getDependencia()==null){
                lineas.add(periodo);
            }
        }
        lineas.sort( Comparator.comparingInt(Periodo::getFecha1)
                .thenComparing(Comparator.comparingDouble(Periodo::getDuracion).reversed()));
        return lineas;
    }

    /**
     * Genera un ArrayList con los hitos que no tienen dependencia (sin padre),
     * ordenados por la fecha de forma ascendente
     *
     * @return lista de hitos sin dependencia ordenados por fecha
     */
    public ArrayList<Hito> crearArrayOrdenadoHito(){
        ArrayList<Hito> ordenada= new ArrayList<>();
        for(Hito hito : hitos){
            if(hito.getDependencia()==null){
                ordenada.add(hito);
            }
        }
        ordenada.sort((hit1,hit2)->{
            return hit1.getFecha() - hit2.getFecha();
        });
        return ordenada;
    }

    /**
     * Genera un bloque HTML con la información de un periodo.
     */
    public String getDivPer(Periodo periodo,int pad){
        String imagen="";
        if(periodo.getImagen()!=null){
            imagen="<p>"+"<img src=\"file:///"+periodo.getImagen()+"\" width=\"200\" height=\"150\">"+"</p>";
        }
        return "<div style=\"padding-left: "+pad+"px;\" >"

                    +"<h3>"+"<b>"+periodo.getTitulo()+"  "+Math.abs(periodo.getFecha1())+"-"+Math.abs(periodo.getFecha2())+"</b>"+"</h3>"
                    +"<div style=\"padding-left: "+(35)+"px;\" >"

                        +"<p>"+periodo.getDatos()+"</p>"
                        +imagen+
                    "</div>"+

                "</div>";
    }

    /**
     * Genera un bloque HTML con la información de un hito.
     */
    public String getDivHit(Hito hito,int pad){
        String imagen="";
        if(hito.getImagen()!=null){
            imagen="<p>"+"<img src=\"file:///"+hito.getImagen()+"\" width=\"200\" height=\"150\">"+"</p>";
        }
        return "<div style=\"padding-left: "+pad+"px;\" >"
                +"<h3>"+"<b>"+hito.getTitulo()+" "+Math.abs(hito.getFecha())+"</b>"+"</h3>"
                +"<div style=\"padding-left: "+(35)+"px;\" >"

                +"<p>"+hito.getDatos()+"</p>"
                +imagen+
                "</div>"
                +"</div>";
    }

    /**
     * Genera recursivamente el texto HTML
     * de un periodo con sus dependencias.
     */
    public String getTexto(Periodo periodo, int pad){
        String texto="";
        texto=getDivPer(periodo,pad);
        if(!periodo.getHitosDependientes().isEmpty()||!periodo.getPeriodosDependientes().isEmpty()){

            ArrayList<Hito> listaHit = periodo.getHitosDependientes();
            listaHit.sort((line1,linea2)->{
                return line1.getFecha()-linea2.getFecha();
            });

            ArrayList<Periodo> listaPer = periodo.getPeriodosDependientes();
            listaPer.sort((line1,linea2)->{
                return line1.getFecha1()-linea2.getFecha1();
            });
            //hay mas periodos
            if(periodo.getPeriodosDependientes().size()>periodo.getHitosDependientes().size()){
                int iterP=0,iterH=0;
                while(iterP!=listaPer.size()){
                    if(iterH!=listaHit.size()){
                        //el periodo es anterior al hito
                        if(listaPer.get(iterP).getFecha1()<=listaHit.get(iterH).getFecha()){
                            texto=texto+getDivPer(listaPer.get(iterP),pad+50);
                            if(!listaPer.get(iterP).getPeriodosDependientes().isEmpty()){
                                for(Periodo per:listaPer.get(iterP).getPeriodosDependientes()){
                                    texto=texto+getTexto(per,pad+100);
                                }

                            }
                            iterP++;
                        }
                        else{
                            texto=texto+getDivHit(listaHit.get(iterH),pad+100);
                            iterH++;
                        }
                    }
                    else {
                        texto=texto+getDivPer(listaPer.get(iterP),pad+50);
                        if(!listaPer.get(iterP).getPeriodosDependientes().isEmpty()){
                            for(Periodo per:listaPer.get(iterP).getPeriodosDependientes()){
                                texto=texto+getTexto(per,pad+100);
                            }

                        }
                        iterP++;
                    }
                }
                if(iterH!=listaHit.size()){
                    while(iterH!=listaHit.size()){
                        texto=texto+getDivHit(listaHit.get(iterH),pad+100);
                        iterH++;
                    }
                }
            }
            //hay mas hitos
            else{
                int iterP=0,iterH=0;
                while(iterH!=listaHit.size()){
                    if(iterP!=listaPer.size()){
                        //el periodo es anterior al hito
                        if(listaPer.get(iterP).getFecha1()<=listaHit.get(iterH).getFecha()){

                            texto=texto+getDivPer(listaPer.get(iterP),pad+50);

                            if(!listaPer.get(iterP).getPeriodosDependientes().isEmpty()){
                                for(Periodo per:listaPer.get(iterP).getPeriodosDependientes()){
                                    getTexto(per,pad+50);
                                }

                            }
                            iterP++;
                        }
                        else{
                            texto=texto+getDivHit(listaHit.get(iterH),pad+100);
                            iterH++;
                        }
                    }
                    else {
                        texto=texto+getDivHit(listaHit.get(iterH),pad+100);
                        iterH++;
                    }
                }
            }
        }
        return texto;
    }

    /**
     * Carga el esquema HTML completo en el editor.
     */
    @FXML
    public void cargarEsquema(){
        String texto="";
        int pad=10;
        ArrayList<Periodo> listPer=crearArrayOrdenadoLineas();
        ArrayList<Hito> listHit= crearArrayOrdenadoHito();
        //hay mas Hitos
        int iterP=0,iterH=0,fchAntMen;
        while (iterP < listPer.size() && iterH < listHit.size()){
            fchAntMen=Math.min(listPer.get(iterP).getFecha1(),listHit.get(iterH).getFecha());

            if(listPer.get(iterP).getFecha1()==fchAntMen){
                texto=texto+getTexto(listPer.get(iterP),pad);
                iterP++;
            }
            else{
                texto=texto+getDivHit(listHit.get(iterH),pad);
                iterH++;
            }
        }
        while (iterP < listPer.size()){
            texto=texto+getTexto(listPer.get(iterP),pad);
            iterP++;
        }
        while(iterH < listHit.size()){
            texto=texto+getDivHit(listHit.get(iterH),pad);
            iterH++;
        }



        htmlEditor.setHtmlText(texto);

    }

    /**
     * Convierte HTML generado a XHTML compatible con OpenHTMLToPDF.
     */
    private String htmlToXhtml(final String html) {
        final Document document = Jsoup.parse(html);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        Document doc = Jsoup.parse(document.html());
        Elements imgs= doc.select("img");
        for(Element img:imgs){
            String original = img.attr("src");

            File file=new File(original);

            String rutaAbsoluta= file.toString().replace("\\", "/");

            String formatoRuta=rutaAbsoluta.replace(" ", "%20")
                    .replace("á", "%C3%A1")
                    .replace("é", "%C3%A9")
                    .replace("í", "%C3%AD")
                    .replace("ó", "%C3%B3")
                    .replace("ú", "%C3%BA")
                    .replace("Á", "%C3%81")
                    .replace("É", "%C3%89")
                    .replace("Í", "%C3%8D")
                    .replace("Ó", "%C3%93")
                    .replace("Ú", "%C3%9A")
                    .replace("ñ", "%C3%B1")
                    .replace("Ñ", "%C3%91");
            img.attr("src",formatoRuta);
        }

        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        return doc.html();
    }

    /**
     * Exporta el contenido HTML a un archivo PDF.
     */
    @FXML
    public void imprimirPdf(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("selecciona el fichero");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter(
                        "Archivos pdf (*.pdf)",
                        "*.pdf"
                );

        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setSelectedExtensionFilter(imageFilter);
        fileChooser.setInitialFileName(proyectoActual.getNombre()+".pdf");
        File file = fileChooser.showSaveDialog(null);
        try {
            if (!file.toString().endsWith(".pdf")) {
                file = new File(file.toString() + ".pdf");
            }
            String html = htmlToXhtml(htmlEditor.getHtmlText());

            try (OutputStream os = new FileOutputStream(file)) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.withHtmlContent(html, null);
                builder.toStream(os);
                builder.run();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }catch (NullPointerException _){}
    }

    /**
     * Exporta la línea temporal como imagen PNG.
     */
    @FXML
    public void imprimirLinea(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("selecciona el fichero");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter(
                        "Archivos de imagen (*.png)",
                        "*.png"
                );

        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setSelectedExtensionFilter(imageFilter);
        fileChooser.setInitialFileName(proyectoActual.getNombre()+".png");
        File file = fileChooser.showSaveDialog(null);
        try {
        if(!file.toString().endsWith(".png")){
            file= new File(file.toString()+".png");
        }
        SnapshotParameters parameters = new SnapshotParameters();
        WritableImage imagen= new WritableImage((int)jaulaLineas.getWidth(),(int)jaulaLineas.getHeight());

        jaulaLineas.setScaleY(-1);
        jaulaLineas.snapshot(parameters,imagen);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(imagen,null),"png",file);
        } catch (IOException _) {}
        jaulaLineas.setScaleY(1);
        }catch (NullPointerException _){}
    }

    /**
     * Elimina un periodo y limpia sus dependencias asociadas.
     * @param periodo Periodo a eliminar
     */
    public void delVistaPer(Periodo periodo){
        String id= periodo.getId();
        for(Periodo depencia:periodos){
            try{
                if(depencia.getDependencia().getId().equals(id)){
                    depencia.setDependencia(null);

                    controladoresPer.get(depencia.getId()).setComboBox(" ");

                }
            } catch (NullPointerException e) {}
        }
        for(Hito depencia:hitos){
            try{
                if(depencia.getDependencia().getId().equals(id)){
                    depencia.setDependencia(null);
                   controladoresHit.get(depencia.getId()).setComboBox(" ");
                }
            } catch (NullPointerException e) {}
        }
        periodos.remove(periodo);

        vboxData.getChildren().remove(vistas.get(id));

    }

    /**
     * Elimina un hito de la vista y del modelo.
     * @param hito Hito a eliminar
     */
    public void delVistaHit(Hito hito){
        String id=hito.getId();
        hitos.remove(hito);
        vboxData.getChildren().remove(vistas.get(id));
    }

    /**
     * Actualiza los valores mínimos y máximos de fechas y duración.
     */
    public void setDurFchMinFchMax(double duracion,int Fch1,int Fch2){
        if(Fch1!=Fch2){
        duracionMin=duracion;}
        fechaMin= Fch1;
        fechaMax=Fch2;
        for(Periodo per:periodos){
            duracionMin=Math.min(duracionMin,per.getDuracion());
            fechaMin=Math.min(fechaMin,per.getFecha1());
            fechaMax=Math.max(fechaMax,per.getFecha2());
        }
        for(Hito hit:hitos){
            fechaMin=Math.min(fechaMin,hit.getFecha());
            fechaMax=Math.max(fechaMax,hit.getFecha());
        }
    }

    /**
     * Bloquea los campos de los periodos padre
     * @param periodo padre
     */
    public void bloqCamposPorDepencdencia(Periodo periodo){
        controladoresPer.get(periodo.getId()).bloquearHabilitarCampos();
    }

    /**
     * Crea un nuevo proyecto vacío tras confirmación del usuario.
     * Elimina toda la información actual del proyecto.
     */
    @FXML
    public void nuevoProyecto(){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("Al crear un nuevo proyecto toda la informacion del proyecto se perdera");
        confirmation.setContentText("¿Estas seguro de continuar?");
        ButtonType btn =confirmation.showAndWait().get();
        if(btn==ButtonType.OK){
            periodos= new ArrayList<Periodo>();
            hitos= new ArrayList<Hito>();
            proyectoActual=new Proyecto(UUID.randomUUID().toString(),"Sin nombre",periodos,hitos);
            vboxData.getChildren().clear();
            jaulaLineas.getChildren().clear();
            htmlEditor.setHtmlText("");
            nomProyecto.setText("Sin nombre");
            vistas.clear();
            controladoresPer.clear();
            controladoresHit.clear();
        }
    }

    /**
     * Guarda el proyecto actual en un archivo JSON local.
     */
    @FXML
    public void GuardarLocal(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("selecciona el fichero");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter(
                        "Archivos de linea tiempo (*.json)",
                        "*.json"
                );
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setSelectedExtensionFilter(imageFilter);
        fileChooser.setInitialFileName(proyectoActual.getNombre()+".json");
        File file = fileChooser.showSaveDialog(null);
        try {
            if(!file.toString().endsWith(".json")){
                file= new File(file.toString()+".json");
            }
            JsonMaker gson = new JsonMaker();
            BufferedWriter fichero = new BufferedWriter(new FileWriter(file));
            fichero.write(gson.getJson(proyectoActual));
            fichero.close();
        }catch (NullPointerException _){} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Carga un proyecto y reemplaza el actual.
     */
    @FXML
    public void cargarLocal(){
       proyectoCargado(cargarJson());
       rellenarVbox(proyectoActual.getPeriodos(),proyectoActual.getHitos());
    }

    /**
     * Añade un proyecto cargado al proyecto actual.
     */
    @FXML
    public void anadirLocal(){
        Proyecto proyecto = cargarJson();
        if(proyecto!=null){
            anadirProyecto(proyecto);
        }
    }

    public void anadirProyecto(Proyecto proyecto){
            asignarDependecias(proyecto.getPeriodos(),proyecto.getHitos());
            for(Periodo periodo:proyecto.getPeriodos()){
                for(Periodo per:periodos){
                    if(periodo.getId().equals(per.getId())){
                        periodo.setId(UUID.randomUUID().toString());
                    }
                }
            }
            periodos.addAll(proyecto.getPeriodos());
            for(Hito hito:proyecto.getHitos()){
                for(Hito hit:hitos){
                    if(hito.getId().equals(hit.getId())){
                        hito.setId(UUID.randomUUID().toString());
                    }
                }
            }
            setIdDependecias(proyecto.getPeriodos());
            hitos.addAll(proyecto.getHitos());
            vboxData.getChildren().clear();
            jaulaLineas.getChildren().clear();
            rellenarVbox(periodos,hitos);

    }

    /**
     * Actualiza los IDs de dependencias tras añadir proyectos.
     * @param listaPeriodos lista de periodos
     */
    public void setIdDependecias(ArrayList<Periodo> listaPeriodos){
        for(Periodo periodo:listaPeriodos){
            if(!periodo.getPeriodosDependientes().isEmpty()){
                periodo.getIdDependientes().clear();
                for(Periodo perDep:periodo.getPeriodosDependientes()){
                    periodo.getIdDependientes().add(perDep.getId());
                    perDep.setDependeciaId(periodo.getId());
                }
            }
            if(!periodo.getHitosDependientes().isEmpty()){
                periodo.getIdHitos().clear();
                for(Hito hito:periodo.getHitosDependientes()){
                    periodo.getIdHitos().add(hito.getId());
                    hito.setDependencia(periodo);
                }
            }
        }
    }

    public void proyectoCargado(Proyecto proyecto){
        vboxData.getChildren().clear();
        jaulaLineas.getChildren().clear();

        proyectoActual=proyecto;
        vistas.clear();
        controladoresPer.clear();
        controladoresHit.clear();
        nomProyecto.setText(proyectoActual.getNombre());
        periodos=proyectoActual.getPeriodos();
        hitos=proyectoActual.getHitos();
        asignarDependecias(proyectoActual.getPeriodos(),proyectoActual.getHitos());
    }

    /**
     * Rellena el vbox con los nuevos hitos y periodos
     * @param listaPeriodos lista de periodos
     * @param listaHitos lista de hitos
     */
    public void rellenarVbox(ArrayList<Periodo> listaPeriodos,ArrayList<Hito> listaHitos){
        listaPeriodos.sort((per1,per2)->{
            return per1.getFecha1()-per2.getFecha1();
        });
        listaHitos.sort((hit1,hit2)->{
            return hit1.getFecha()-hit2.getFecha();
        });
        for(Periodo periodo:listaPeriodos){
            PeriodoUI periodoUI = new PeriodoUI();
            vboxData.getChildren().add(periodoUI);


            PeriodoController periodoController = periodoUI.ctr;




            periodoController.setMainViewController(this);
            periodoController.setPeriodos(periodos);
            periodoController.setPeriodo(periodo);
            vistas.put(periodoController.getPeriodo().getId(),periodoUI);
            controladoresPer.put(periodoController.getPeriodo().getId(),periodoController);

            periodoController.setCampos();
        }

        for(Hito hito:listaHitos){
            HitoUI hitoUI = new HitoUI();
            vboxData.getChildren().add(hitoUI);

            HitoController hitoController = hitoUI.ctr;



            hitoController.setMainViewController(this);
            hitoController.setHito(hito);
            hitoController.setDatos();
            vistas.put(hitoController.getHito().getId(),hitoUI);
            controladoresHit.put(hitoController.getHito().getId(),hitoController);
            hitoController.setPeriodos(periodos);

        }
        onButtonCrearCLick();

    }

    /**
     * Asigna dependencias entre periodos e hitos
     * a partir de los IDs cargados.
     * @param listaPeriodos lista de periodos
     * @param listaHitos lista de hitos
     */
    public void asignarDependecias(ArrayList<Periodo> listaPeriodos,ArrayList<Hito> listaHitos){
        for(Periodo periodo: listaPeriodos){
            for (Periodo per : listaPeriodos){
                try{
                if(periodo.getIdDependientes().contains(per.getId())){
                    periodo.getPeriodosDependientes().add(per);
                }}catch (NullPointerException _){}
                try{
                if(periodo.getDependeciaId().equals(per.getId())){
                    periodo.setDependencia(per);
                }}catch (NullPointerException _){}
            }
            for(Hito hito:listaHitos){
                try{
                if(periodo.getIdHitos().contains(hito.getId())){
                    periodo.getHitosDependientes().add(hito);
                    hito.setDependencia(periodo);
                }}catch (NullPointerException _){}
            }
        }
    }

    /**
     * Carga un proyecto desde un archivo JSON.
     * @return Proyecto cargado o null si falla
     */
    public Proyecto cargarJson(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("selecciona el fichero");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter(
                        "Archivos de linea tiempo (*.json)",
                        "*.json"
                );
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setSelectedExtensionFilter(imageFilter);
        File file = fileChooser.showOpenDialog(null);
        try {
            String json="";
            String linea;
            JsonMaker gson = new JsonMaker();
            BufferedReader lector= new BufferedReader(new FileReader(file));
            while ((linea=lector.readLine())!=null){
                json+=linea;
            }
            Proyecto proyecto= gson.getProyecto(json);
            if(proyecto==null){
                toast("Error al cargar el proyecto");
            }
            return proyecto;
        }catch (NullPointerException _){} catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        toast("Error al cargar el proyecto");
        return null;
    }

    /**
     * Abre la ventana de inicio de sesión.
     */
    @FXML
    public void iniciarSesionUI(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/app_lin_tem/Login.fxml"));
        try {
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            LoginController ctr= loader.getController();
            ctr.setVista(true,stage);
            ctr.setCtr(this);
            stage.showAndWait();



        } catch (IOException e) {}
    }

    /**
     * Muestra una notificación emergente.
     */
    public void toast(String texto){
        Notifications.create()
                .text(texto)
                .hideAfter(Duration.seconds(2))
                .position(Pos.BOTTOM_RIGHT)
                .showInformation();
    }

    /**
     * Abre la ventana de crear sesión.
     */
    @FXML
    public void crearSesion(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/app_lin_tem/Login.fxml"));
        try {
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);

            LoginController ctr= loader.getController();
            ctr.setVista(false,stage);
            ctr.setCtr(this);
            stage.showAndWait();



        } catch (IOException e) {}
    }

    /**
     * Crea una cuenta nueva en Firebase Authentication.
     */
    public boolean crearSesionUI(String email,String contraseña) throws IOException {
        AuthRequest request= new AuthRequest(email,contraseña);

        Response<AuthResponse> response =
                authApi.crearCuenta(APIKEY, request).execute();
        AuthResponse auth = authApi.crearCuenta(APIKEY,request).execute().body();

        if (response.isSuccessful() && response.body() != null) {
            idToken = response.body().idToken;
            refreshToken = response.body().refreshToken;
            uID=response.body().localId;
            timer = LocalTime.now();


            return true;
        }

        if (response.errorBody() != null) {
            String errorJson = response.errorBody().string();

            if (errorJson.contains("EMAIL_EXISTS")) {
               toast("El correo ya está registrado");
            } else if (errorJson.contains("WEAK_PASSWORD")) {
                toast("La contraseña es demasiado débil");
            } else if (errorJson.contains("INVALID_EMAIL")) {
                toast("El correo no es válido");
            } else {
                toast("Error creando cuenta" );
            }
        }

        else {
            toast("Error desconocido al crear la sesión pruebe mas tarde o pongase en contanto con el servicio tecnico");

        }
        return false;
    }

    /**
     * Inicia sesión con Firebase Authentication.
     */
    public boolean iniciarSesionUI(String email, String contraseña) throws IOException {
        AuthRequest request= new AuthRequest(email,contraseña);

        Response<AuthResponse> response =
                authApi.iniciarSesion(APIKEY,request).execute();

        if(response.isSuccessful()&&response.body()!=null){
            idToken = response.body().idToken;
            refreshToken = response.body().refreshToken;
            uID=response.body().localId;
            timer = LocalTime.now();


            return true;
        }
        if (response.errorBody() != null) {
            String errorJson = response.errorBody().string();

            if (errorJson.contains("INVALID_LOGIN_CREDENTIALS")) {
                toast("El correo o la contraseña no son correrctos");
            } else if (errorJson.contains("INVALID_PASSWORD")) {
                toast("El correo o la contraseña no son correrctos");
            } else if (errorJson.contains("USER_DISABLED")) {
                toast("El usuario ha sido deshabilitado");
            }
        }

        else {
            toast("Error desconocido al iniciar sesión pruebe mas tarde o pongase en contanto con el servicio tecnico");

        }
        return false;
    }

    /**
     * Guarda el proyecto actual en Firebase.
     */
    @FXML
    public void guardarDatosNube() throws IOException {

        if(idToken.equals("")){
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setHeaderText("Tienes que iniciar sesion");
            confirmation.showAndWait();
        }
        else{
            LocalTime timerS= LocalTime.now();
            java.time.Duration diferencia= java.time.Duration.between(timer,timerS);
        if(diferencia.toHours()>=1){
            RefreshTokenResponse response=tokenApi.refreshToken(
                    APIKEY,
                    "refresh_token",
                    refreshToken).execute().body();
            idToken =response.id_token;
            refreshToken=response.refresh_token;
            timer = LocalTime.now();
        }
        Response<Proyecto> response = dbApi.saveProyecto(uID,proyectoActual.getId(), idToken,proyectoActual).execute();
        if(response.isSuccessful()){
            toast("Archivo guardado correctamente");
        }
        else{
            toast("Se ha producido un error: "+response.code()+" al guardar");
        }
        }
    }

    /**
     * Carga proyectos almacenados en la nube.
     */
    @FXML
    public void obtenerProyectosCargar() throws IOException {
        cargarProyectosNube(true,false);
    }

    /**
     * Añade un proyecto almacenado en la nube.
     */
    @FXML
    public void obtenerProyectosAnadir() throws IOException {
        cargarProyectosNube(false,false);
    }

    /**
     * Elimina un proyecto almacenado en Firebase.
     */
    @FXML
    public void obtenerProyectoEliminar() throws IOException {
        cargarProyectosNube(false,true);
    }

    /**
     * Carga proyectos desde la nube.
     * @param cargar sí se debe cargar el proyecto
     * @param eliminar Si se debe eliminar
     */
    public void cargarProyectosNube(boolean cargar,boolean eliminar) throws IOException {
        if(idToken.equals("")){
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setHeaderText("Tienes que iniciar sesion");
            confirmation.showAndWait();
        }
        else{
            LocalTime timerS= LocalTime.now();
            java.time.Duration diferencia= java.time.Duration.between(timer,timerS);
            if(diferencia.toHours()>=1){
                RefreshTokenResponse response=tokenApi.refreshToken(
                        APIKEY,
                        "refresh_token",
                        refreshToken).execute().body();
                idToken =response.id_token;
                refreshToken=response.refresh_token;
                timer = LocalTime.now();
            }
            Response<Map<String, Proyecto>> response = dbApi.getProyectos(uID, idToken).execute();
            if(response.isSuccessful()){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/app_lin_tem/Componentes/proyectosNube.fxml"));

                try{
                    Parent root = loader.load();

                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setTitle("Proyectos");
                    stage.setScene(scene);
                    stage.show();

                    ProyectosNubeController ctr = loader.getController();
                    ctr.setCtr(this);
                    ctr.setProyectos(response.body());
                    ctr.setEliminar(eliminar);
                    ctr.setCargar(cargar);
                    ctr.proyectosBtn();

                }catch (IOException _) {}
            }
            else{
                toast("Se ha producido un error: "+response.code()+" al cargar");
            }
        }
    }

    /**
     * Elimina un proyecto almacenado en la nube.
     * @param id ID del proyecto
     * @return true si se elimina correctamente
     */
    public boolean eliminarProyecto(String id){
        try {
            if(idToken.equals("")){
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setHeaderText("Tienes que iniciar sesion");
                confirmation.showAndWait();
            }
            else{
                LocalTime timerS= LocalTime.now();
                java.time.Duration diferencia= java.time.Duration.between(timer,timerS);
                if(diferencia.toHours()>=1){
                    RefreshTokenResponse response=tokenApi.refreshToken(
                            APIKEY,
                            "refresh_token",
                            refreshToken).execute().body();
                    idToken =response.id_token;
                    refreshToken=response.refresh_token;
                    timer = LocalTime.now();
                }
            Response<Void> response= dbApi.deleteProyecto(uID,id, idToken).execute();
            if(response.isSuccessful()){
                return true;
            }
            else {
                return false;
            }
        }} catch (IOException e) {
            return false;
        }
        return false;
    }
}