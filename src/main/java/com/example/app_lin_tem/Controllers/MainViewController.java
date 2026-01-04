package com.example.app_lin_tem.Controllers;


import com.example.app_lin_tem.Componentes.Controller.HitoController;
import com.example.app_lin_tem.Componentes.Controller.PeriodoController;
import com.example.app_lin_tem.Componentes.HitoUI;
import com.example.app_lin_tem.Componentes.Lineas;
import com.example.app_lin_tem.Componentes.PeriodoUI;
import com.example.app_lin_tem.Model.Hito;
import com.example.app_lin_tem.Model.JsonMaker;
import com.example.app_lin_tem.Model.Periodo;
import com.example.app_lin_tem.Model.Proyecto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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


import java.io.IOException;
import java.util.*;

public class MainViewController {


    private ArrayList<Periodo> periodos;
    private ArrayList<Hito> hitos;
    private HashMap<String,Node> vistas;
    private HashMap<String,PeriodoController> ControladoresPer;
    private HashMap<String,HitoController> ControladoresHit;
    private double duracionMin=0,fechaMin,fechaMax;
    private int i=1;

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

    @FXML
    private ComboBox Buscador;



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
    private void cargarBuscador(){
        ArrayList nombres = new ArrayList<>();
        nombres.add("Buscador");
        for(Periodo per:crearArrayOrdenadoLineas()){
            nombres.add(per.getTitulo());
            if(!per.getPeriodosDependientes().isEmpty()){
                ArrayList<Periodo> periodosDep=per.getPeriodosDependientes();
                periodosDep.sort((per1,per2)->{return (per1.getFecha1()-per2.getFecha1());});
                for(Periodo perDep:periodosDep){
                    nombres.add(" "+perDep.getTitulo());
                }
            }
        }
        for(Hito hit: hitos){
            nombres.add(hit.getTitulo());
        }
        Buscador.setItems(FXCollections.observableList(nombres));

    }

    @FXML
    public void buscar(){
        Node target=null;
        for(Periodo per:periodos){
            if(Buscador.getValue().toString().equals(per.getTitulo())){
                target= vistas.get(per.getId());
            }
        }
        for(Hito hit : hitos){
            if(Buscador.getValue().toString().equals(hit.getTitulo())){
                target= vistas.get(hit.getId());
            }
        }
        if(target!=null){
            double y = target.getBoundsInParent().getMinY();
            double altura = ScrollData.getContent().getBoundsInLocal().getHeight();

            ScrollData.setVvalue(y/altura);
            Buscador.setValue(null);
        }
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
       ScrollData.setVisible(false);
       Buscador.setVisible(false);
       jaulaLineas.getChildren().clear();
       calcularAlturasPrimarios(crearArrayOrdenadoLineas());


       for(Periodo periodo:periodos){

           Lineas linea = new Lineas(periodo);
           linea.setLayoutX(getPosicion(periodo.getFecha1()));
           linea.setLayoutY(periodo.getAltura());
           linea.setScaleY(-1);
           linea.ctr.setTamaño(getTamaño(periodo.getDuracion()));

           jaulaLineas.getChildren().add(linea);


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
                    //System.out.println(periodo.getTitulo()+" ha hecho subir a: "+altura.getTitulo());
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
        return Math.abs((((Fecha1)/duracionMin)*200+20)-(((fechaMin)/duracionMin)*200));
    }

    @FXML
    protected void onActionBottonMostrar(){
        ScrollData.setVisible(!ScrollData.isVisible());
        Buscador.setVisible(!Buscador.isVisible());
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

    public String getDivPer(Periodo periodo,int pad){
        return "<div style=\"padding-left: "+pad+"px;\" >"

                    +"<h3>"+"<b>"+periodo.getTitulo()+"  "+periodo.getFecha1()+"-"+periodo.getFecha2()+"</b>"+"</h3>"
                    +"<div style=\"padding-left: "+(pad+25)+"px;\" >"

                        +"<p>"+periodo.getDatos()+"</p>"+

                    "</div>"+

                "</div>";
    }

    public String getDivHit(Hito hito,int pad){
        return "<div style=\"padding-left: "+pad+"px;\" >"+"<p>"+"<b>"+hito.getTitulo()+"</b>"+"</p>"+"</div>";
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
                            texto=texto+"<div style=\"padding-left: "+(pad+50)+"px;\" >"+"<p>"+"<b>"+listaHit.get(iterH).getTitulo()+"</b>"+"</p>"+"</div>";
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
                        texto=texto+"<div style=\"padding-left: "+(pad+50)+"px;\" >"+"<p>"+"<b>"+listaHit.get(iterH).getTitulo()+"</b>"+"</p>"+"</div>";
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
                            texto=texto+"<div style=\"padding-left: "+(pad+50)+"px;\" >"+"<p>"+"<b>"+listaHit.get(iterH).getTitulo()+"</b>"+"</p>"+"</div>";
                            iterH++;
                        }
                    }
                    else {
                        texto=texto+"<div style=\"padding-left: "+(pad+50)+"px;\" >"+"<p>"+"<b>"+listaHit.get(iterH).getTitulo()+"</b>"+"</p>"+"</div>";
                        iterH++;
                    }
                }
            }
        }
        return texto;
    }

    @FXML
    public void prueba(){

        String texto="";
        int pad=10;
        ArrayList<Periodo> listaOrdenada=crearArrayOrdenadoLineas();
        for (Periodo periodo:listaOrdenada){
            for(Periodo per:listaOrdenada){
                texto=texto+getTexto(per,pad);
            }


        }
        for(Hito dato:hitos){

        }
        HTMLEditor.setHtmlText(texto);
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
        duracionMin=duracion;
        fechaMin= Fch1;
        fechaMax=Fch2;
        for(Periodo per:periodos){
            duracionMin=Math.min(duracionMin,per.getDuracion());
            fechaMin=Math.min(fechaMin,per.getFecha1());
            fechaMax=Math.max(fechaMax,per.getFecha2());
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
    public void iniciarSesion(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/app_lin_tem/Login.fxml"));
        try {
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            LoginController ctr= loader.getController();
            ctr.setVista(true);
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
}