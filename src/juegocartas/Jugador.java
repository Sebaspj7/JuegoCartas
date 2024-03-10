package juegocartas;

import java.util.Arrays;
import java.util.Random;
import javax.swing.JPanel;

public class Jugador {

    public int TOTAL_CARTAS = 10;
    private Random r = new Random();
    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    // Declarar las variables de acceso global para Trebol y cantidadTrebol
    private Carta[] cartasTrebol, cartasPica, cartasDiamante, cartasCorazon;
    private int cantidadTrebol, cantidadPica, cantidadDiamante, cantidadCorazon;
    private int[] numerosConsecutivosTrebol, numerosConsecutivosPica, numerosConsecutivosDiamante, numerosConsecutivosCorazon;
 

    public void repartir() {
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        for (int i = 0; i < cartas.length; i++) {
            cartas[i].mostrar(pnl, 5 + i * 40, 5);
        }
        //para que ejecute el método
        pnl.repaint();
    }

    public String getGrupos() {

        cantidadTrebol = contarCartasPorPinta(cartas, Pinta.TREBOL);
        cantidadPica = contarCartasPorPinta(cartas, Pinta.PICA);
        cantidadDiamante = contarCartasPorPinta(cartas, Pinta.DIAMANTE);
        cantidadCorazon = contarCartasPorPinta(cartas, Pinta.CORAZON);

        cartasTrebol = ordenarCartasPorPinta(cartas, Pinta.TREBOL, cantidadTrebol);
        cartasPica = ordenarCartasPorPinta(cartas, Pinta.PICA, cantidadPica);
        cartasDiamante = ordenarCartasPorPinta(cartas, Pinta.DIAMANTE, cantidadDiamante);
        cartasCorazon = ordenarCartasPorPinta(cartas, Pinta.CORAZON, cantidadCorazon);

        //se tendrá un vector de contadores que arranca con todos los vectores valiendo cero, 
        //en cada vector se va a almacenar el número de cartas iguales, por ejemplo si hay 3 reyes el contador 12 va a tener el valor de 3
        //los enumerados tienen un mètodo para ver la posición dónde están
        //getNombre devuleve el nombre de la carta y ordinal devuelve la posición donde está ese nombre
        int[] contadores = new int[NombreCarta.values().length];
        for (int i = 0; i < cartas.length; i++) {
            contadores[cartas[i].getNombre().ordinal()]++;
        }

        int totalGrupos = 0;
        for (int i = 0; i < contadores.length; i++) {
            if (contadores[i] >= 2) {
                totalGrupos++;
            }
        }

             //Este código recorre el arreglo cartas, obtiene el ordinal de la carta y verifica si su contador en la posición 
             //correspondiente del arreglo contadores es mayor o igual a 2. Si es así, se establece la propiedad formaGrupo de esa carta como true.
        for (int i = 0; i < cartas.length; i++) {
            int ordinal = cartas[i].getNombre().ordinal();
            if (contadores[ordinal] >= 2) {
                cartas[i].setFormaGrupo(true);
            }

        }

        String[] mensajesEscaleras = {
            mostrarNumerosConsecutivos(encontrarCartasConsecutivas(cartasTrebol), Pinta.TREBOL),
            mostrarNumerosConsecutivos(encontrarCartasConsecutivas(cartasPica), Pinta.PICA),
            mostrarNumerosConsecutivos(encontrarCartasConsecutivas(cartasDiamante), Pinta.DIAMANTE),
            mostrarNumerosConsecutivos(encontrarCartasConsecutivas(cartasCorazon), Pinta.CORAZON),};

        return mensajeCompleto(mensajesEscaleras, totalGrupos, contadores);

    }

    public String getPuntaje() {

         numerosConsecutivosTrebol = obtenerNumerosConsecutivos(encontrarCartasConsecutivas(cartasTrebol));
         numerosConsecutivosPica = obtenerNumerosConsecutivos(encontrarCartasConsecutivas(cartasPica));
         numerosConsecutivosDiamante = obtenerNumerosConsecutivos(encontrarCartasConsecutivas(cartasDiamante));
         numerosConsecutivosCorazon = obtenerNumerosConsecutivos(encontrarCartasConsecutivas(cartasCorazon));

        marcarNumerosConsecutivos(numerosConsecutivosTrebol, cartas, Pinta.TREBOL);
        marcarNumerosConsecutivos(numerosConsecutivosPica, cartas, Pinta.PICA);
        marcarNumerosConsecutivos(numerosConsecutivosDiamante, cartas, Pinta.DIAMANTE);
        marcarNumerosConsecutivos(numerosConsecutivosCorazon, cartas, Pinta.CORAZON);

        return mostrarPuntaje(cartas);
    }

    public static String mensajeCompleto(String[] mensajesEscaleras, int totalGrupos, int[] contadores) {
        String mensaje = "";

        // Agregar mensaje de grupos si hay grupos encontrados
        if (totalGrupos > 0) {
            mensaje += "Los grupos encontrados fueron:\n";
            for (int i = 0; i < contadores.length; i++) {
                if (contadores[i] >= 2) {
                    mensaje += Grupo.values()[contadores[i]] + " de " + NombreCarta.values()[i] + "\n";
                }
            }
        }

        String mensajeCompleto = "";
        for (int i = 0; i < mensajesEscaleras.length; i++) {
            mensajeCompleto += mensajesEscaleras[i];
        }

        if (!mensajeCompleto.equals("")) {
            mensaje += "Las escaleras encontradas fueron:\n";
            // Agregar mensajes de escaleras
            for (String mensajeEscalera : mensajesEscaleras) {
                mensaje += mensajeEscalera + "\n";
            }
        }

        // Si no se encontraron grupos ni escaleras, mostrar "No hay grupos"
        if (mensaje.equals("")) {
            mensaje = "No hay grupos";
        }
        return mensaje;
    }

    public void ordenarPorBurbuja() {
        for (int i = 0; i < cartas.length - 1; i++) {
            for (int j = i + 1; j < cartas.length; j++) {
                if (cartas[i].getIndice() > cartas[j].getIndice()) {
                    Carta temp = cartas[i];
                    cartas[i] = cartas[j];
                    cartas[j] = temp;
                }
            }

        }

    }

    public Carta[] ordenarPorSeleccion(Carta cartas[]) {
        int n = cartas.length;

        // Iterar a través del arreglo
        for (int i = 0; i < n - 1; i++) {
            int indiceMinimo = i;

            // Encontrar el índice del elemento más pequeño en el resto del arreglo
            for (int j = i + 1; j < n; j++) {
                if (cartas[j].getIndice() < cartas[indiceMinimo].getIndice()) {
                    indiceMinimo = j;
                }
            }

            // Intercambiar el elemento más pequeño con el elemento en la posición actual
            Carta temp = cartas[indiceMinimo];
            cartas[indiceMinimo] = cartas[i];
            cartas[i] = temp;
        }

        return cartas;
    }

    public Carta[] ordenarCartasPorPinta(Carta cartas[], Pinta pinta, int numeroCartas) {
        int indicePinta = 0;
        Carta[] cartasPinta = new Carta[numeroCartas];

        for (int i = 0; i < cartas.length; i++) {
            if (cartas[i].getPinta() == pinta) {
                cartasPinta[indicePinta] = cartas[i];
                indicePinta++;

                // Verifica si ya hemos copiado suficientes cartas
                if (indicePinta >= numeroCartas) {
                    break;
                }
            }
        }

        return ordenarPorSeleccion(cartasPinta);
    }

    public int contarCartasPorPinta(Carta cartas[], Pinta pinta) {
        int n = cartas.length;
        int contador = 0;
        for (int i = 0; i < cartas.length; i++) {
            if (cartas[i].getPinta() == pinta) {
                contador++;
            }
        }
        return contador;
    }

    public int[][] encontrarCartasConsecutivas(Carta cartas[]) {
        int[][] gruposConsecutivos = new int[cartas.length][2];
        int numGrupos = 0;
        if (cartas.length == 0) {
            return new int[0][2];
        }

        int inicioGrupo = cartas[0].getNombre().ordinal();
        int finGrupo = inicioGrupo;

        for (int i = 1; i < cartas.length; i++) {
            if (cartas[i].getNombre().ordinal() == finGrupo + 1) {
                finGrupo = cartas[i].getNombre().ordinal();
            } else {
                gruposConsecutivos[numGrupos][0] = inicioGrupo;
                gruposConsecutivos[numGrupos][1] = finGrupo;
                numGrupos++;

                inicioGrupo = finGrupo = cartas[i].getNombre().ordinal();
            }
        }

        gruposConsecutivos[numGrupos][0] = inicioGrupo;
        gruposConsecutivos[numGrupos][1] = finGrupo;
        numGrupos++;

        // Crear un nuevo arreglo con el tamaño correcto para los grupos
        int[][] resultado = new int[numGrupos][2];
        for (int i = 0; i < numGrupos; i++) {
            resultado[i][0] = gruposConsecutivos[i][0];
            resultado[i][1] = gruposConsecutivos[i][1];
        }

        return resultado;
    }

    public static String mostrarNumerosConsecutivos(int[][] grupos, Pinta pinta) {
        String resultado = "";
        NombreCarta[] nombres = NombreCarta.values(); // Obtener todos los nombres de las cartas

        for (int i = 0; i < grupos.length; i++) {
            int inicio = grupos[i][0] + 1;
            int fin = grupos[i][1] + 1;

            // Verificar si inicio y fin son diferentes
            if (inicio != fin) {
                resultado += "Escalera de " + pinta + ": ";

                // Mostrar todos los números en el rango
                for (int num = inicio; num <= fin; num++) {
                    resultado += nombres[num - 1];
                    if (num < fin) {
                        resultado += ", ";
                    }
                }

                resultado += "\n";
            }
        }
        return resultado;
    }

    public static int[] obtenerNumerosConsecutivos(int[][] grupos) {
        int[] numerosConsecutivos = new int[grupos.length * 10]; // Tamaño inicial
        int resultadoIndex = 0;

        for (int i = 0; i < grupos.length; i++) {
            int inicio = grupos[i][0] + 1;
            int fin = grupos[i][1] + 1;

            // Verificar si inicio y fin son diferentes
            if (inicio != fin) {
                for (int num = inicio; num <= fin; num++) {
                    if (resultadoIndex == numerosConsecutivos.length) {
                        // Aumentar el tamaño del arreglo dinámicamente
                        int[] nuevoArreglo = new int[numerosConsecutivos.length * 2];
                        for (int j = 0; j < numerosConsecutivos.length; j++) {
                            nuevoArreglo[j] = numerosConsecutivos[j];
                        }
                        numerosConsecutivos = nuevoArreglo;
                    }
                    numerosConsecutivos[resultadoIndex++] = num;
                }
            }
        }

        // Redimensionar el arreglo al tamaño real
        int[] resultado = new int[resultadoIndex];
        for (int i = 0; i < resultadoIndex; i++) {
            resultado[i] = numerosConsecutivos[i];
        }

        return resultado;
    }

    public static void marcarNumerosConsecutivos(int[] numerosConsecutivos, Carta[] cartas, Pinta pinta) {

        for (int i = 0; i < numerosConsecutivos.length; i++) {
            int num = numerosConsecutivos[i];

            for (int j = 0; j < cartas.length; j++) {
                if (cartas[j].getNombre().ordinal() == (num - 1) && cartas[j].getPinta() == pinta) {
                    cartas[j].setFormaGrupo(true);
                    break; // No necesitamos seguir buscando en las cartas
                }
            }
        }
    }

    public String mostrarPuntaje(Carta[] cartas) {
        int suma = 0;
        String cartasNoFormaGrupo = "";

        for (int i = 0; i < cartas.length; i++) {
            if (!cartas[i].estaEnGrupo()) {
                int ordinal = cartas[i].getNombre().ordinal();

                // Si el ordinal es 0,10, 11 o 12, suma 10; de lo contrario, suma el ordinal directamente.
                suma += (ordinal == 0 || (ordinal >= 10 && ordinal <= 12)) ? 10 : (ordinal + 1);

                // Agrega el nombre de la carta y su pinta al mensaje de cartas que no forman grupo.
                if (!cartasNoFormaGrupo.equals("")) {
                    cartasNoFormaGrupo += "\n";
                }
                cartasNoFormaGrupo += cartas[i].getNombre() + " de " + cartas[i].getPinta();
            }
        }

        // Verifica si todas las cartas forman grupo o no.
        if (cartasNoFormaGrupo.equals("")) {
            return "Todas las cartas forman grupo, su puntaje es CERO";
        } else {
            return "Su puntaje es: " + suma + "\nLas cartas que no forman grupo son:\n" + cartasNoFormaGrupo;
        }
    }

}
