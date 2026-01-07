package com.example.app_lin_tem.Controllers;


import com.example.app_lin_tem.Componentes.Controller.HitoController;
import com.example.app_lin_tem.Componentes.Controller.PeriodoController;
import com.example.app_lin_tem.Componentes.HitoUI;
import com.example.app_lin_tem.Componentes.Lineas;
import com.example.app_lin_tem.Componentes.PeriodoUI;
import com.example.app_lin_tem.Componentes.hitoLineas;
import com.example.app_lin_tem.Model.Hito;
import com.example.app_lin_tem.Model.JsonMaker;
import com.example.app_lin_tem.Model.Periodo;
import com.example.app_lin_tem.Model.Proyecto;
import com.example.app_lin_tem.Model.firebase.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MainViewController {


    private ArrayList<Periodo> periodos;
    private ArrayList<Hito> hitos;
    private HashMap<String,Node> vistas;
    private HashMap<String,PeriodoController> ControladoresPer;
    private HashMap<String,HitoController> ControladoresHit;
    private double duracionMin=2,fechaMin,fechaMax;
    private int i=1;
    private String IdToken;
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
    private HTMLEditor HTMLEditor;

    @FXML
    private VBox VboxData;

    @FXML
    private ScrollPane contendorLineas,ScrollData;

    @FXML
    private AnchorPane jaulaLineas;




    public MainViewController(){
        periodos= new ArrayList<Periodo>();

        hitos= new ArrayList<Hito>();
        vistas= new HashMap<>();
        ControladoresPer= new HashMap<>();
        ControladoresHit= new HashMap<>();


    }

    public void postInit(){
        contendorLineas.setScaleY(-1);
        contendorLineas.vvalueProperty().bind(jaulaLineas.heightProperty());
        contendorLineas.hvalueProperty().bind(jaulaLineas.widthProperty());
        dbApi=retroFitClient.databaseApi();
        authApi=retroFitClient.authApi();
        tokenApi=retroFitClient.tokenApi();

    }

    @FXML
    protected void onButtonPeridoClick() throws IOException {
        PeriodoUI periodoUI = new PeriodoUI();
        VboxData.getChildren().add(periodoUI);


        PeriodoController periodoController = periodoUI.ctr;
        periodos.add(periodoController.getPeriodo());
        vistas.put(periodoController.getPeriodo().getId(),periodoUI);
        ControladoresPer.put(periodoController.getPeriodo().getId(),periodoController);


        periodoController.setMainViewController(this);
        periodoController.setPeriodos(periodos);
        periodoController.newColor(tablaColores.get(i));
        periodoUI.requestFocus();

        i=i+1;
        if(i>10){i=1;}
    }

    @FXML
    protected void onButtonHitoClick() throws IOException {
        HitoUI hitoUI = new HitoUI();
        VboxData.getChildren().add(hitoUI);

        HitoController hitoController = hitoUI.ctr;
        hitos.add(hitoController.getHito());
        vistas.put(hitoController.getHito().getId(),hitoUI);
        ControladoresHit.put(hitoController.getHito().getId(),hitoController);

        hitoController.setMainViewController(this);
        hitoController.setPeriodos(periodos);

    }

    @FXML
    protected void onButtonCrearCLick(){
       ScrollData.setVisible(false) ;

       jaulaLineas.getChildren().clear();
       calcularAlturasPrimarios(crearArrayOrdenadoLineas());


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

       }
       for(Hito hito:hitos) {
           if(hito.getAlturaDep()==0){
               hito.setAlturaNDep(10);
               hito.setAltura(58);
               for(Periodo periodo: crearArrayOrdenadoLineas()){
                   if(hito.getFecha()>=periodo.getFecha1()&&hito.getFecha()<=periodo.getFecha2()){
                       hito.setAltura(Math.max(hito.getAlturaDep(),periodo.getAlturaMax())-58);
                   }
               }

           }
            hitoLineas hitoLinea= new hitoLineas(hito);
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

    public void sendFront(Node node){
        node.toFront();
    }

    public void alturaHitos(){
        for(Hito hito:hitos){hito.setAltura(0);hito.setAlturaNDep(0);}
        for(Periodo per:crearArrayOrdenadoLineas()){
            ArrayList<Hito> cajaHitos= new ArrayList<>();
            for (Hito hito:hitos){
                if(per== hito.getPadreCaja(hito.getDependencia())){
                    hito.setAlturaDep();
                    hito.setAltura(per.getAlturaMax());
                    cajaHitos.add(hito);
                }
            }
            for(Hito hito:cajaHitos){
                for(Hito hit:cajaHitos){
                    if((hito.getAltura()==hit.getAltura()&&hit.getFecha()==hito.getFecha())&&(hit!=hito)){
                        hit.addAltura(30);
                    }
                }
            }
        }
    }

    public void calcularAlturasPrimarios(ArrayList<Periodo> periodosCalcular){
        double alturaMax=0;
        for(Periodo periodo:periodosCalcular){
            periodo.setAltura(58);
            periodo.setAlturaMax();

            if(!periodo.getPeriodosDependientes().isEmpty()){
                calcularAlturaDependientes(periodo.getAltura()+58,periodo.getPeriodosDependientes());
                periodo.setAlturaMax();
            }
            alturaHitos();
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
                        System.out.println(periodo.getTitulo()+" tiene a: "+per.getTitulo()+" dentro");
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

    private void anadidoColisiones(Periodo periodo,ArrayList<Periodo> periodosCoincidentes){
        for (Periodo per:periodosCoincidentes){
            double dato1=per.getAltura();
            double dato2=periodo.getAlturaMax()+78;

            if((dato1<=dato2)&&(per!=periodo)){
                per.addAlturaColision((((per.getAltura()+periodo.getAlturaMax())+20)-(periodo.getAlturaMax())));
                per.setAlturaMax();

                ArrayList<Periodo> salida = new ArrayList<>();
                salida.addAll(periodosCoincidentes);
                salida.remove(per);

                anadidoColisiones(per,salida);
            }
        }
    }

    public double calcularAlturaDependientes(double alturaIni,ArrayList<Periodo> lista){
        double alturaMax=alturaIni;

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
            if(!periodo.getPeriodosDependientes().isEmpty()|!periodo.getHitosDependientes().isEmpty()){
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
        for(Periodo per:lista){
            alturaMax=Math.max(alturaMax,per.getAlturaMax());
        }


        return alturaMax;
    }

    //calculo de tamaño en funcion de el periodo con menor duracion
    private double getTamaño(double Duracion){
        return 200*(Duracion /duracionMin);
    }

    private double getPosicion(int Fecha1){
        System.out.println(Math.abs((((Fecha1)/duracionMin)*200+20)-(((fechaMin)/duracionMin)*200)));
        return Math.abs((((Fecha1)/duracionMin)*200+20)-(((fechaMin)/duracionMin)*200)) ;
    }

    @FXML
    protected void onActionBottonMostrar(){
        ScrollData.setVisible(!ScrollData.isVisible());

    }

    public ArrayList<Periodo> crearArrayOrdenadoLineas(){
        ArrayList<Periodo> lineas = new ArrayList<>();
        for(Periodo periodo : periodos){
            if (periodo.getDependencia()==null){
                lineas.add(periodo);
            }
        }
        lineas.sort((line1,linea2)->{
            return line1.getFecha1()-linea2.getFecha1();
        });
        return lineas;
    }

    public ArrayList<Hito> creArrayOrdenadoHito(){
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

    public String getDivPer(Periodo periodo,int pad){
        String imagen="";
        if(periodo.getImagen()!=null){
            imagen="<p>"+"<img src=\"file:///"+periodo.getImagen()+"\" width=\"200\" height=\"150\">"+"</p>";
        }
        return "<div style=\"padding-left: "+pad+"px;\" >"

                    +"<h3>"+"<b>"+periodo.getTitulo()+"  "+periodo.getFecha1()+"-"+periodo.getFecha2()+"</b>"+"</h3>"
                    +"<div style=\"padding-left: "+(35)+"px;\" >"

                        +"<p>"+periodo.getDatos()+"</p>"
                        +imagen+
                    "</div>"+

                "</div>";
    }

    public String getDivHit(Hito hito,int pad){
        String imagen="";
        if(hito.getImagen()!=null){
            imagen="<p>"+"<img src=\"file:///"+hito.getImagen()+"\" width=\"200\" height=\"150\">"+"</p>";
        }
        return "<div style=\"padding-left: "+pad+"px;\" >"
                +"<h3>"+"<b>"+hito.getTitulo()+" "+hito.getFecha()+"</b>"+"</h3>"
                +"<div style=\"padding-left: "+(35)+"px;\" >"

                +"<p>"+hito.getDatos()+"</p>"
                +imagen+
                "</div>"
                +"</div>";
    }

    public String getTexto(Periodo periodo, int pad){
        String texto="";
        texto=getDivPer(periodo,pad);
        //texto="<div style=\"padding-left: "+pad+"px;\" >"+"<p>"+"<b>"+periodo.getTitulo()+"</b>"+"</p>"+"</div>";
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

    @FXML
    public void cargarEsquema(){
        String texto="";
        int pad=10;
        ArrayList<Periodo> listaOrdenada=crearArrayOrdenadoLineas();
        for (Periodo periodo:listaOrdenada){
                texto=texto+getTexto(periodo,pad);
        }
        for(Hito dato:hitos){

        }

        HTMLEditor.setHtmlText(texto);

    }

    //"C:\Users\victo\OneDrive\Escritorio\"
    private String hmlToXhtml(String Html){
        String salida=Html;

        return Html;
    }
    private String htmlToXhtml(final String html) {
        String salida;
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
    @FXML
    public void imprimirPdf(){
        String html=htmlToXhtml(HTMLEditor.getHtmlText());
        
        try (OutputStream os = new FileOutputStream("C:\\Users\\victo\\OneDrive\\Escritorio\\documento.pdf")) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void delVistaPer(Periodo periodo){
        String id= periodo.getId();
        for(Periodo depencia:periodos){
            try{
                if(depencia.getDependencia().getId().equals(id)){
                    depencia.setDependencia(null);

                    ControladoresPer.get(depencia.getId()).setComboBox(" ");

                }
            } catch (NullPointerException e) {}
        }
        for(Hito depencia:hitos){
            try{
                if(depencia.getDependencia().getId().equals(id)){
                    depencia.setDependencia(null);
                   ControladoresHit.get(depencia.getId()).setComboBox(" ");
                }
            } catch (NullPointerException e) {}
        }
        periodos.remove(periodo);

        VboxData.getChildren().remove(vistas.get(id));

    }

    public void delVistaHit(Hito hito){
        String id=hito.getId();
        hitos.remove(hito);
        VboxData.getChildren().remove(vistas.get(id));
    }

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

    public void bloqCamposPorDepencdencia(Periodo periodo){
        ControladoresPer.get(periodo.getId()).bloquearHabilitarCampos();
    }

    @FXML
    public void Guardar(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("selecciona el fichero");
        fileChooser.showOpenDialog(null);
        Proyecto proyecto = new Proyecto("1","Hola",periodos,hitos);
        JsonMaker gson = new JsonMaker();
        System.out.println( gson.getJson(proyecto));
    }

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
            ctr.setVista(true);
            ctr.setCtr(this);
            stage.showAndWait();



        } catch (IOException e) {System.out.println(e);}
    }

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
            ctr.setVista(false);
            ctr.setCtr(this);
            stage.showAndWait();



        } catch (IOException e) {System.out.println(e);}
    }

    public void toast(String texto){
        Notifications.create()
                .text(texto)
                .hideAfter(Duration.seconds(2))
                .position(Pos.BOTTOM_RIGHT)
                .showInformation();
    }


    public void crearSesionUI(String email,String contraseña) throws IOException {
        AuthRequest request= new AuthRequest(email,contraseña);

        Response<AuthResponse> response =
                authApi.crearCuenta(APIKEY, request).execute();
        AuthResponse auth = authApi.crearCuenta(APIKEY,request).execute().body();

        if (response.isSuccessful() && response.body() != null) {
            IdToken = response.body().idToken;
            System.out.println("✅ Sesión iniciada correctamente");
            System.out.println("Usuario: " + response.body().email);
            return;
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

    }

    public void iniciarSesionUI(String email, String contraseña) throws IOException {
        AuthRequest request= new AuthRequest(email,contraseña);

        Response<AuthResponse> response =
                authApi.iniciarSesion(APIKEY,request).execute();

        if(response.isSuccessful()&&response.body()!=null){
            IdToken = response.body().idToken;
            System.out.println("✅ Sesión iniciada correctamente");
            System.out.println("Usuario: " + response.body().email);
            return;
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
    }

    public void guardarDatos(){

    }


    public void actualizar(){

    }

    public void obtenerProyectos(){

    }

    public void obtenerProyecto(){

    }
}