package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Model.Hito;
import com.example.app_lin_tem.Model.Periodo;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.web.WebView;


import java.util.ArrayList;

public class VistaDatoController {

   @FXML
   private WebView Vista;

   private String html ;


    public void setDataPer(String titulo, String data, int fecha1, int fecha2, ArrayList<Periodo> dependecias, String imagen,ArrayList<Hito> HitoDep){
        String periodosDep="";
        String hitosDep="";
        for(Periodo per:dependecias){
            periodosDep=periodosDep+"<p>"+"*"+per.getTitulo()+"</p>";
        }
        for(Hito hit:HitoDep){
            hitosDep=hitosDep+"<p>"+"*"+hit.getTitulo()+"</p>";
        }
        html="<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>"+titulo+"</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .clase1 {\n" +
                "            padding: 50px;\n" +
                "            text-align: justify;\n" +
                "        }\n" +
                "\n" +
                "        #contenedor {\n" +
                "            display: flex;\n" +
                "            flex-direction: row;\n" +
                "            flex-wrap: wrap;\n" +
                "        }\n" +
                "\n" +
                "        #contenedor > div {\n" +
                "            width: 50%;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>"+titulo+" "+fecha1+"-"+fecha2+"</h1>\n" +
                "   <div class=\"clase1\"><p>"+data+"</p></div>\n" +
                "    <img src=\"file:///"+imagen+"\" alt=\"\">\n" +
                "   \n" +
                " \n" +
                "    <div id=\"contenedor\">\n" +
                "    <div>\n" +
                "        <p>Periodos dependientes</p>\n" +
                        periodosDep+

                "    </div>\n" +
                "    <div>\n" +
                "        <p>Hitos dependientes</p>\n" +
                        hitosDep+

                "    </div>\n" +
                "    </div>\n" +
                "\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

    }

    public void setDataHit(String titulo,String data,int fecha,String imagen){
        html="<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>"+titulo+"</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .clase1 {\n" +
                "            padding: 50px;\n" +
                "            text-align: justify;\n" +
                "        }\n" +
                "\n" +
                "        #contenedor {\n" +
                "            display: flex;\n" +
                "            flex-direction: row;\n" +
                "            flex-wrap: wrap;\n" +
                "        }\n" +
                "\n" +
                "        #contenedor > div {\n" +
                "            width: 50%;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>"+titulo+" "+fecha+"</h1>\n" +
                "   <div class=\"clase1\"><p>"+data+"</p></div>\n" +
                "    <img src=\"file:///"+imagen+"\" alt=\"\">\n" +
                "   \n" +
                " \n" +


                "</div>\n" +
                "\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

    }

    public void cargarHtml(){
        Vista.getEngine().loadContent(html);
    }
}
