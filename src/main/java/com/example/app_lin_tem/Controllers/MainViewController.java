package com.example.app_lin_tem.Controllers;


import com.example.app_lin_tem.Componentes.Controller.HitoController;
import com.example.app_lin_tem.Componentes.Controller.PeriodoController;
import com.example.app_lin_tem.Componentes.HitoUI;
import com.example.app_lin_tem.Componentes.Lineas;
import com.example.app_lin_tem.Componentes.PeriodoUI;
import com.example.app_lin_tem.Model.Hito;
import com.example.app_lin_tem.Model.Periodo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;


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
            if(!per.getDependientes().isEmpty()){
                ArrayList<Periodo> periodosDep=per.getDependientes();
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
                target= vistas.get(hit.getID());
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
        vistas.put(hitoController.getHito().getID(),hitoUI);
        ControladoresHit.put(hitoController.getHito().getID(),hitoController);

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

            if(!periodo.getDependientes().isEmpty()){
                calcularAlturaDependientes(periodo.getAltura()+58,periodo.getDependientes());
                periodo.setAlturaMax();
            }
            int iter=0;
            ArrayList<Periodo> periodosCoincidentes = new ArrayList<>();
            boolean colisiona=false;
            while(periodosCalcular.size()!=iter){
                if(periodo!=periodosCalcular.get(iter)){
                    int f1o=periodo.getFecha1();
                    int f2o=periodo.getFecha2();

                    int f1p=periodosCalcular.get(iter).getFecha1();
                    int f2p=periodosCalcular.get(iter).getFecha2();

                    if((((f1p<=f1o)&(f1p<f2o))|((f2p<f1o)&(f2p<=f2o)))){
                        periodosCoincidentes.add(periodosCalcular.get(iter));
                        colisiona=true;
                    }
                }
                iter++;

            }
            boolean hueco=true;
            for(Periodo altura:periodosCoincidentes){
                if((altura.getAltura()==periodo.getAltura())|((altura.getAltura()<=periodo.getAltura())&&(periodo.getAltura()<=altura.getAlturaMax()))){
                    periodo.addAlturaColision(altura.getAlturaMax()+78);
                    periodo.setAlturaMax();
                    hueco=false;
                }
            }
            if(colisiona&&(!periodo.getDependientes().isEmpty())&&hueco){
                anadidoColisiones(periodo,periodosCoincidentes);
                //boolean subir=false;
                //for (Periodo per:periodosCoincidentes){

                //    if(per.getAltura()<=(periodo.getAlturaMax()+20)){
                //    subir=true;}
                //}
                //if(subir){
                //    for(Periodo per:periodosCoincidentes){
                //        per.addAlturaColision(((per.getAltura()+periodo.getAlturaMax())+116)-(periodo.getAlturaMax()));
                //        per.setAlturaMax();
                //    }
               // }
            }
            periodo.setAlturaMax();

        }
        for(Periodo periodo:periodosCalcular){
            int iter=0;
            ArrayList<Periodo> periodosCoincidentes = new ArrayList<>();
            boolean colisiona=false;
            while(periodosCalcular.size()!=iter){
                if(periodo!=periodosCalcular.get(iter)){
                    int f1o=periodo.getFecha1();
                    int f2o=periodo.getFecha2();

                    int f1p=periodosCalcular.get(iter).getFecha1();
                    int f2p=periodosCalcular.get(iter).getFecha2();

                    if((((f1p<=f1o)&(f1p<f2o))|((f2p<f1o)&(f2p<=f2o)))){
                        periodosCoincidentes.add(periodosCalcular.get(iter));
                        colisiona=true;
                    }
                    //el periodo esta dentro de per //
                    if(((f2p>f1E)&(f2p<=f2E))){
                        periodosInterior.add(per);
                        System.out.println(periodo.getTitulo() + " esta dentro de: " + per.getTitulo());
                    }
                }
                iter++;

            }
            boolean hueco=true;
            for(Periodo altura:periodosCoincidentes){
                if((altura.getAltura()==periodo.getAltura())|((altura.getAltura()<=periodo.getAltura())&&(periodo.getAltura()<=altura.getAlturaMax()))){
                    periodo.addAlturaColision(altura.getAlturaMax()+78);
                    periodo.setAlturaMax();
                    hueco=false;
                }
            }
            if(colisiona&&(!periodo.getDependientes().isEmpty())&&hueco){
                anadidoColisiones(periodo,periodosCoincidentes);
                //boolean subir=false;
                //for (Periodo per:periodosCoincidentes){

                //    if(per.getAltura()<=(periodo.getAlturaMax()+20)){
                //    subir=true;}
                //}
                //if(subir){
                //    for(Periodo per:periodosCoincidentes){
                //        per.addAlturaColision(((per.getAltura()+periodo.getAlturaMax())+116)-(periodo.getAlturaMax()));
                //        per.setAlturaMax();
                //    }
                // }
            }
            periodo.setAlturaMax();
        }
    }

    private void anadidoColisiones(Periodo periodo,ArrayList<Periodo> periodosCoincidentes){
        for (Periodo per:periodosCoincidentes){
            double dato1=per.getAltura();
            double dato2=periodo.getAlturaMax()+20;

            if((dato1<=dato2)&&(per!=periodo)){
                per.addAlturaColision(((per.getAltura()+periodo.getAlturaMax())+116)-(periodo.getAlturaMax()));
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
            if(!periodo.getDependientes().isEmpty()){
                calcularAlturaDependientes(periodo.getAltura()+58,periodo.getDependientes());
                periodo.setAlturaMax();
            }
            if(colisiona&&(!periodo.getDependientes().isEmpty()&&hueco)){
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



    @FXML
    public void prueba(){
        String texto="";
        int pad=10;
        for (Periodo dato:periodos){
            texto=texto+"<div style=\"padding-left: "+pad+"px;\" >"+"<p>"+"<b>"+dato.getTitulo()+"</b>"+"</p>"+"</div>";
            pad+=50;
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
                   ControladoresHit.get(depencia.getID()).setComboBox(" ");
                }
            } catch (NullPointerException e) {}
        }
        periodos.remove(periodo);

        VboxData.getChildren().remove(vistas.get(id));

    }

    public void delVistaHit(Hito hito){
        String id=hito.getID();
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
}