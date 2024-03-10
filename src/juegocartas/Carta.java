package juegocartas;

import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Carta {

    private int indice;
    private boolean formaGrupo;

    public boolean estaEnGrupo() {
        return formaGrupo;
    }

    public void setFormaGrupo(boolean formaGrupo) {
        this.formaGrupo = formaGrupo;
    }

    public Carta(Random r) {
        indice = r.nextInt(52) + 1;
      //si fuera a calcular la edad de peronas entre 15 y 21 años lo haría:
       //edad=r.nextInt(7)+15;
    }

    @Override
    public String toString() {
        return "Carta{" +getNombre()+  "pinta=" + getPinta() +"indice=" + indice + ", formaGrupo=" + formaGrupo + '}';
    }



    
    
    public Pinta getPinta() {
        if (indice <= 13) {
            return Pinta.TREBOL;
        } else if (indice <= 26) {
            return Pinta.PICA;
        } else if (indice <= 39) {
            return Pinta.CORAZON;
        } else {
            return Pinta.DIAMANTE;
        }
    }

    public int getIndice() {
        return indice;
    }

    public NombreCarta getNombre() {
        int numero = indice % 13;
        //lo anterior da residuos entre 0 y 12, si el residuo es cero la carta es K
        if (numero == 0) {
            numero = 13;
        }
        // los enumerados se pueden llevar a un vector y preguntar por su posición
        return NombreCarta.values()[numero - 1];
    }

        //x y y son coordenadas donde se muestran las cartas este método muestra la carta
    public void mostrar(JPanel pnl, int x, int y) {
        String nombreImagen = "/imagenes/carta" + String.valueOf(indice) + ".jpg";

        ImageIcon imagen = new ImageIcon(getClass().getResource(nombreImagen));
        JLabel lbl = new JLabel(imagen);
        lbl.setBounds(x, y, imagen.getIconWidth(), imagen.getIconHeight());

        pnl.add(lbl);

    }

}


