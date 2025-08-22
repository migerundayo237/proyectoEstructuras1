package Ficheros; // Define el paquete "Ficheros" para esta clase.

// Importaciones de E/S (entrada/salida) de archivos y streams.
import java.io.*; // Incluye File, FileInputStream, FileOutputStream, Object streams, etc.
// Importación para codificación de texto estándar (UTF-8).
import java.nio.charset.StandardCharsets; // Proporciona la constante UTF_8 para lectores/escritores de texto.
import java.util.Arrays;

import AEROLINEA.Aerolinea;
// Importamos clases del dominio (entidades del sistema).
import AEROLINEA.Aeropuerto; // Clase Aeropuerto.
import AEROLINEA.Avion;      // Clase Avion.
import AEROLINEA.Cliente;    // Clase Cliente.
import AEROLINEA.Empleado;   // Clase Empleado.
import AEROLINEA.Vuelo;      // Clase Vuelo.
import AEROLINEA.Reservacion;// Clase Reservacion.

/**
 * Clase ManejoFicheros
 * - Crea estructura de carpetas bajo "Ficheros/".
 * - Carga datos iniciales desde "Ficheros/datos_iniciales.txt" solo la primera vez (usa flag ".initialized").
 * - Provee métodos CRUD por tipo, guardando cada objeto en su propio archivo ".obj".
 * - No usa ArrayList ni List; únicamente arreglos y contadores.
 */
public class ManejoFicheros { // Declaración de la clase pública.

    // ===== Campos con rutas de carpetas base =====
    private File raiz;            // Carpeta raíz "Ficheros".
    private File dirAeropuerto;   // Subcarpeta "Ficheros/Aeropuerto".
    private File dirAvion;        // Subcarpeta "Ficheros/Avion".
    private File dirCliente;      // Subcarpeta "Ficheros/Cliente".
    private File dirEmpleado;     // Subcarpeta "Ficheros/Empleado".
    private File dirVuelo;        // Subcarpeta "Ficheros/Vuelo".
    private File dirReservacion;  // Subcarpeta "Ficheros/Reservacion".
    private Aerolinea a;
    // ===== Archivos auxiliares =====
    private File flagInicializado;     // Archivo "Ficheros/.initialized" para marcar que ya se cargó el TXT.
    private File ficheroInicial;       // Archivo "Ficheros/datos_iniciales.txt" con datos iniciales.

    // ===== Filtro reutilizable para listar solo archivos .obj =====
    private static final FilenameFilter OBJ_FILTER = new FilenameFilter() { // Clase anónima para filtrar nombres.
        public boolean accept(File dir, String name) { // Método que decide si aceptar el archivo.
            return name != null && name.endsWith(".obj"); // Acepta solo nombres que terminen en ".obj".
        }
    };

    // ===== Constructor =====
    public ManejoFicheros() { // Constructor por defecto.
        raiz = new File("Ficheros");                         // Inicializa objeto File a la carpeta raíz.
        dirAeropuerto = new File(raiz, "Aeropuerto");        // Define subcarpeta Aeropuerto.
        dirAvion = new File(raiz, "Avion");                  // Define subcarpeta Avion.
        dirCliente = new File(raiz, "Cliente");              // Define subcarpeta Cliente.
        dirEmpleado = new File(raiz, "Empleado");            // Define subcarpeta Empleado.
        dirVuelo = new File(raiz, "Vuelo");                  // Define subcarpeta Vuelo.
        dirReservacion = new File(raiz, "Reservacion");      // Define subcarpeta Reservacion.
        flagInicializado = new File(raiz, ".initialized");   // Apunta al archivo de bandera/flag.
        ficheroInicial = new File(raiz, "datos_iniciales.txt"); // Apunta al TXT de datos iniciales.
        a = new Aerolinea(null, null, null, null, null, null, this);

        crearDirectorios();   // Asegura que todas las carpetas existan.
        cargarInicialSiAplica(); // Intenta carga inicial desde TXT si procede.
        a.setClientes(leerObjetos(dirCliente, Cliente.class));
        a.setEmpleados(leerObjetos(dirEmpleado, Empleado.class));
        a.setAviones(leerObjetos(dirAvion, Avion.class));
        a.setVuelos(leerObjetos(dirVuelo, Vuelo.class));
        a.setAeropuertos(leerObjetos(dirAeropuerto, Aeropuerto.class));
        a.setReservaciones(leerObjetos(dirReservacion, Reservacion.class));
    }
    
    public Aerolinea getAerolinea() {
        return a;
    }

    // ==========================================================
    // =====================   AEROPUERTO   =====================
    // ==========================================================

    public void addAeropuerto(Aeropuerto a) { // Agrega/guarda un aeropuerto nuevo.
        if (a == null) return; // Si el objeto es nulo, no hace nada.
        String id = a.getCodigo(); // Obtiene ID único desde el objeto (ajusta si tu clase usa otro nombre).
        escribirObjeto(new File(dirAeropuerto, id + ".obj"), a); // Serializa el objeto a "Ficheros/Aeropuerto/<id>.obj".
    }

    public Aeropuerto getAeropuertoById(String id) { // Recupera un aeropuerto por ID.
        if (id == null || id.length() == 0) return null; // Valida el ID.
        return (Aeropuerto) leerObjeto(new File(dirAeropuerto, id + ".obj")); // Deserializa y castea.
    }

    public Aeropuerto[] getAllAeropuerto() { // Devuelve todos los aeropuertos existentes.
        File[] archivos = listarObj(dirAeropuerto); // Lista archivos ".obj" en la carpeta.
        int total = archivos.length; // Número total de archivos .obj.
        Aeropuerto[] temp = new Aeropuerto[total]; // Arreglo temporal del tamaño máximo posible.
        int k = 0; // Contador de elementos válidos realmente leídos.
        for (int i = 0; i < total; i++) { // Recorre cada archivo.
            Object o = leerObjeto(archivos[i]); // Deserializa el archivo actual.
            if (o instanceof Aeropuerto) { // Verifica tipo correcto.
                temp[k] = (Aeropuerto) o; // Inserta en el arreglo temporal.
                k = k + 1; // Incrementa el índice válido.
            }
        }
        if (k == total) return temp; // Si todos eran válidos, retorna el arreglo tal cual.
        Aeropuerto[] exacto = new Aeropuerto[k]; // Si no, crea uno exacto del tamaño k.
        for (int i = 0; i < k; i++) exacto[i] = temp[i]; // Copia solo los válidos.
        return exacto; // Retorna el arreglo exacto.
    }

    public void updateAeropuerto(String id, Aeropuerto nuevo) { // Actualiza un aeropuerto existente.
        if (id == null || id.length() == 0 || nuevo == null) return; // Valida parámetros.
        escribirObjeto(new File(dirAeropuerto, id + ".obj"), nuevo); // Sobrescribe el archivo con el nuevo objeto.
    }

    public void deleteAeropuerto(String id) { // Elimina un aeropuerto por ID.
        if (id == null || id.length() == 0) return; // Valida ID.
        File f = new File(dirAeropuerto, id + ".obj"); // Ubica el archivo por ID.
        if (f.exists()) f.delete(); // Si existe, lo borra.
    }

    // ==========================================================
    // ========================   AVION   ========================
    // ==========================================================

    public void addAvion(Avion a) { // Agrega/guarda un avión nuevo.
        if (a == null) return; // Valida objeto.
        String id = a.getCodigoA(); // Obtiene ID (ajusta si tu clase usa otro getter).
        escribirObjeto(new File(dirAvion, id + ".obj"), a); // Serializa a "Ficheros/Avion/<id>.obj".
        
    }

    public Avion getAvionById(String id) { // Recupera un avión por ID.
        if (id == null || id.length() == 0) return null; // Valida ID.
        return (Avion) leerObjeto(new File(dirAvion, id + ".obj")); // Deserializa y castea.
    }

    public Avion[] getAllAvion() { // Devuelve todos los aviones.
        File[] archivos = listarObj(dirAvion); // Lista ".obj" en Avion.
        int total = archivos.length; // Cuenta archivos.
        Avion[] temp = new Avion[total]; // Arreglo temporal.
        int k = 0; // Contador de elementos válidos.
        for (int i = 0; i < total; i++) { // Recorre archivos.
            Object o = leerObjeto(archivos[i]); // Deserializa.
            if (o instanceof Avion) { // Verifica tipo.
                temp[k] = (Avion) o; // Inserta.
                k = k + 1; // Avanza contador.
            }
        }
        if (k == total) return temp; // Si todo válido, retorna temp.
        Avion[] exacto = new Avion[k]; // Si no, crea arreglo exacto.
        for (int i = 0; i < k; i++) exacto[i] = temp[i]; // Copia válidos.
        return exacto; // Retorna exacto.
    }

    public void updateAvion(String id, Avion nuevo) { // Actualiza un avión por ID.
        if (id == null || id.length() == 0 || nuevo == null) return; // Valida.
        escribirObjeto(new File(dirAvion, id + ".obj"), nuevo); // Sobrescribe archivo.
    }

    public void deleteAvion(String id) { // Elimina un avión por ID.
        if (id == null || id.length() == 0) return; // Valida.
        File f = new File(dirAvion, id + ".obj"); // Ubica archivo.
        if (f.exists()) f.delete(); // Borra si existe.
    }

    // ==========================================================
    // =======================   CLIENTE   =======================
    // ==========================================================

    private String buildPersonaId(String tipoDoc, String numDoc) {
        return tipoDoc + "_" + numDoc;
    }
    
    public void addCliente(Cliente c) { // Agrega/guarda un cliente.
        if (c == null) return; // Valida.
        String id = buildPersonaId(c.getTipoDocumento(), c.getNumDocumento()); // Usa número de documento como ID.
        escribirObjeto(new File(dirCliente, id + ".obj"), c); // Serializa a "Ficheros/Cliente/<id>.obj".
    }

    public Cliente getClienteById(String tipoDoc, String numDoc) { // Recupera cliente por ID.
    	if (tipoDoc == null || numDoc == null || tipoDoc.isEmpty() || numDoc.isEmpty()) return null; // Valida.
    	String id = buildPersonaId(tipoDoc, numDoc);
    	return (Cliente) leerObjeto(new File(dirCliente, id + ".obj")); // Deserializa y castea.
    }

    public Cliente[] getAllCliente() { // Devuelve todos los clientes.
        File[] archivos = listarObj(dirCliente); // Lista ".obj" en Cliente.
        int total = archivos.length; // Cuenta archivos.
        Cliente[] temp = new Cliente[total]; // Arreglo temporal.
        int k = 0; // Contador válidos.
        for (int i = 0; i < total; i++) { // Recorre archivos.
            Object o = leerObjeto(archivos[i]); // Deserializa.
            if (o instanceof Cliente) { // Verifica tipo.
                temp[k] = (Cliente) o; // Inserta.
                k = k + 1; // Incrementa contador.
            }
        }
        if (k == total) return temp; // Si todo válido, retorna temp.
        Cliente[] exacto = new Cliente[k]; // Arreglo exacto.
        for (int i = 0; i < k; i++) exacto[i] = temp[i]; // Copia válidos.
        return exacto; // Retorna exacto.
    }

    public void updateCliente(String tipoDoc, String numDoc, Cliente nuevo) { // Actualiza cliente por ID.
        if (tipoDoc == null || numDoc == null || tipoDoc.isEmpty() || numDoc.isEmpty() || nuevo == null) return; // Valida.
        String oldId = buildPersonaId(tipoDoc, numDoc); // ID anterior (archivo actual).
        String newId = buildPersonaId(nuevo.getTipoDocumento(), nuevo.getNumDocumento()); // ID desde el objeto (puede haber cambiado).

        escribirObjeto(new File(dirCliente, newId + ".obj"), nuevo); // Sobrescribe (o crea) usando el ID actual del objeto.

        if (!oldId.equals(newId)) { // Si cambió el documento/tipo, borra el archivo viejo para evitar duplicados.
            File oldFile = new File(dirCliente, oldId + ".obj"); // Ubica archivo viejo.
            if (oldFile.exists()) oldFile.delete(); // Borra si existe.
        }
    }


    public void deleteCliente(String tipoDoc, String numDoc) { // Elimina cliente por ID.
    	if (tipoDoc == null || numDoc == null || tipoDoc.isEmpty() || numDoc.isEmpty()) return; // Valida.
    	String id = buildPersonaId(tipoDoc, numDoc);
    	File f = new File(dirCliente, id + ".obj"); // Ubica archivo.
        if (f.exists()) f.delete(); // Borra si existe.
    }

    // ==========================================================
    // =======================   EMPLEADO   ======================
    // ==========================================================

    public void addEmpleado(Empleado e) { // Agrega/guarda empleado.
        if (e == null) return; // Valida.
        String id = buildPersonaId(e.getTipoDocumento(), e.getNumDocumento()); // ID desde tipo+documento.
        escribirObjeto(new File(dirEmpleado, id + ".obj"), e); // Serializa a "Ficheros/Empleado/<id>.obj".
    }

    public Empleado getEmpleadoById(String tipoDoc, String numDoc) { // Recupera empleado por ID.
        if (tipoDoc == null || numDoc == null || tipoDoc.length() == 0 || numDoc.length() == 0) return null; // Valida.
        String id = buildPersonaId(tipoDoc, numDoc); // Construye ID.
        return (Empleado) leerObjeto(new File(dirEmpleado, id + ".obj")); // Deserializa y castea.
    }

    public Empleado[] getAllEmpleado() { // Devuelve todos los empleados.
        File[] archivos = listarObj(dirEmpleado); // Lista ".obj".
        int total = archivos.length; // Cuenta.
        Empleado[] temp = new Empleado[total]; // Arreglo temporal.
        int k = 0; // Contador válidos.
        for (int i = 0; i < total; i++) { // Recorre.
            Object o = leerObjeto(archivos[i]); // Deserializa.
            if (o instanceof Empleado) { // Verifica tipo.
                temp[k] = (Empleado) o; // Inserta.
                k = k + 1; // Avanza.
            }
        }
        if (k == total) return temp; // Retorna si exacto.
        Empleado[] exacto = new Empleado[k]; // Arreglo exacto.
        for (int i = 0; i < k; i++) exacto[i] = temp[i]; // Copia válidos.
        return exacto; // Retorna exacto.
    }

    public void updateEmpleado(String tipoDoc, String numDoc, Empleado nuevo) { // Actualiza empleado.
        if (tipoDoc == null || numDoc == null || tipoDoc.length() == 0 || numDoc.length() == 0 || nuevo == null) return; // Valida.
        String oldId = buildPersonaId(tipoDoc, numDoc); // ID anterior (archivo actual).
        String newId = buildPersonaId(nuevo.getTipoDocumento(), nuevo.getNumDocumento()); // ID desde el objeto (puede haber cambiado).

        escribirObjeto(new File(dirEmpleado, newId + ".obj"), nuevo); // Sobrescribe (o crea) con el ID actual del objeto.

        if (!oldId.equals(newId)) { // Si cambió documento/tipo, elimina el archivo anterior para evitar duplicados.
            File oldFile = new File(dirEmpleado, oldId + ".obj"); // Ubica archivo viejo.
            if (oldFile.exists()) oldFile.delete(); // Borra si existe.
        }
    }

    public void deleteEmpleado(String tipoDoc, String numDoc) { // Elimina empleado.
        if (tipoDoc == null || numDoc == null || tipoDoc.length() == 0 || numDoc.length() == 0) return; // Valida.
        String id = buildPersonaId(tipoDoc, numDoc); // Construye ID.
        File f = new File(dirEmpleado, id + ".obj"); // Ubica archivo.
        if (f.exists()) f.delete(); // Borra si existe.
    }



    // ==========================================================
    // =========================   VUELO   =======================
    // ==========================================================

    public void addVuelo(Vuelo v) { // Agrega/guarda vuelo.
        if (v == null) return; // Valida.
        String id = v.getCodigo(); // ID del vuelo.
        escribirObjeto(new File(dirVuelo, id + ".obj"), v); // Serializa a "Ficheros/Vuelo/<id>.obj".
    }

    public Vuelo getVueloById(String id) { // Recupera vuelo por ID.
        if (id == null || id.length() == 0) return null; // Valida.
        return (Vuelo) leerObjeto(new File(dirVuelo, id + ".obj")); // Deserializa y castea.
    }

    public Vuelo[] getAllVuelo() { // Devuelve todos los vuelos.
        File[] archivos = listarObj(dirVuelo); // Lista ".obj".
        int total = archivos.length; // Cuenta.
        Vuelo[] temp = new Vuelo[total]; // Arreglo temporal.
        int k = 0; // Contador válidos.
        for (int i = 0; i < total; i++) { // Recorre.
            Object o = leerObjeto(archivos[i]); // Deserializa.
            if (o instanceof Vuelo) { // Verifica tipo.
                temp[k] = (Vuelo) o; // Inserta.
                k = k + 1; // Avanza.
            }
        }
        if (k == total) return temp; // Retorna si exacto.
        Vuelo[] exacto = new Vuelo[k]; // Arreglo exacto.
        for (int i = 0; i < k; i++) exacto[i] = temp[i]; // Copia válidos.
        return exacto; // Retorna exacto.
    }

    public void updateVuelo(String id, Vuelo nuevo) { // Actualiza vuelo.
        if (id == null || id.isEmpty() || nuevo == null) return; // Valida.
        File f = new File(dirVuelo, id + ".obj"); // Ubica archivo.
        if (!f.exists()) return; // Salvaguarda: no sobrescribe si no existe.
        escribirObjeto(f, nuevo); // Sobrescribe.
    }
    public void deleteVuelo(String id) { // Elimina vuelo.
        if (id == null || id.length() == 0) return; // Valida.
        File f = new File(dirVuelo, id + ".obj"); // Ubica archivo.
        if (f.exists()) f.delete(); // Borra si existe.
    }

    // ==========================================================
    // ======================   RESERVACION   ====================
    // ==========================================================

    public void addReservacion(Reservacion r) { // Agrega/guarda reservación.
        if (r == null) return; // Valida.
        String id = r.getCodigo(); // ID de reservación.
        escribirObjeto(new File(dirReservacion, id + ".obj"), r); // Serializa a "Ficheros/Reservacion/<id>.obj".
    }

    public Reservacion getReservacionById(String id) { // Recupera reservación por ID.
        if (id == null || id.length() == 0) return null; // Valida.
        return (Reservacion) leerObjeto(new File(dirReservacion, id + ".obj")); // Deserializa y castea.
    }

    public Reservacion[] getAllReservacion() { // Devuelve todas las reservaciones.
        File[] archivos = listarObj(dirReservacion); // Lista ".obj".
        int total = archivos.length; // Cuenta.
        Reservacion[] temp = new Reservacion[total]; // Arreglo temporal.
        int k = 0; // Contador válidos.
        for (int i = 0; i < total; i++) { // Recorre.
            Object o = leerObjeto(archivos[i]); // Deserializa.
            if (o instanceof Reservacion) { // Verifica tipo.
                temp[k] = (Reservacion) o; // Inserta.
                k = k + 1; // Avanza.
            }
        }
        if (k == total) return temp; // Retorna si exacto.
        Reservacion[] exacto = new Reservacion[k]; // Arreglo exacto.
        for (int i = 0; i < k; i++) exacto[i] = temp[i]; // Copia válidos.
        return exacto; // Retorna exacto.
    }

    public void updateReservacion(String id, Reservacion nuevo) { // Actualiza reservación.
        if (id == null || id.length() == 0 || nuevo == null) return; // Valida.
        escribirObjeto(new File(dirReservacion, id + ".obj"), nuevo); // Sobrescribe.
    }

    public void deleteReservacion(String id) { // Elimina reservación.
        if (id == null || id.length() == 0) return; // Valida.
        File f = new File(dirReservacion, id + ".obj"); // Ubica archivo.
        if (f.exists()) f.delete(); // Borra si existe.
    }

    // ==========================================================
    // ==================  Soporte de E/S y util  =================
    // ==========================================================

    private void crearDirectorios() { // Crea la estructura de carpetas si no existe.
        if (!raiz.exists()) raiz.mkdirs();             // Crea "Ficheros".
        if (!dirAeropuerto.exists()) dirAeropuerto.mkdirs(); // Crea "Ficheros/Aeropuerto".
        if (!dirAvion.exists()) dirAvion.mkdirs();           // Crea "Ficheros/Avion".
        if (!dirCliente.exists()) dirCliente.mkdirs();       // Crea "Ficheros/Cliente".
        if (!dirEmpleado.exists()) dirEmpleado.mkdirs();     // Crea "Ficheros/Empleado".
        if (!dirVuelo.exists()) dirVuelo.mkdirs();           // Crea "Ficheros/Vuelo".
        if (!dirReservacion.exists()) dirReservacion.mkdirs(); // Crea "Ficheros/Reservacion".
    }
    
    private static final boolean LOG = true;

    private void log(String s) {
        if (LOG) System.out.println("[ManejoFicheros] " + s);
    }

    /** Remove BOM and trim. */
    private String limpiar(String s) {
        if (s == null) return "";
        return s.replace("\uFEFF", "").trim();
    }

    private void cargarInicialSiAplica() {
        log("Working dir = " + new java.io.File(".").getAbsolutePath());
        log("TXT esperado = " + ficheroInicial.getAbsolutePath());
        log(".initialized existe? " + flagInicializado.exists());
        log("datos_iniciales.txt existe? " + ficheroInicial.exists());

        if (flagInicializado.exists()) {
            log("Ya inicializado, omito lectura del TXT.");
            return;
        }
        if (!ficheroInicial.exists()) {
            log("No existe el TXT, omito carga inicial.");
            return;
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(ficheroInicial), java.nio.charset.StandardCharsets.UTF_8)
            );

            String linea;
            String seccion = null;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                System.out.println("[TRACE] Línea leída: '" + linea + "' (sección actual: " + seccion + ")");

                // detect a new section
                if (linea.startsWith("[") && linea.endsWith("]")) {
                    seccion = linea.substring(1, linea.length() - 1).trim();
                    System.out.println("[TRACE] -> Nueva sección detectada: " + seccion);
                    // skip the header line right after the section
                    String header = br.readLine();
                    System.out.println("[TRACE] -> Saltando cabecera: '" + header + "'");
                    continue;
                }

                if (seccion == null) {
                    System.out.println("[TRACE] Línea fuera de sección, ignorada.");
                    continue;
                }

                String[] f = linea.split("\\|");
                System.out.println("[TRACE] -> Campos parseados: " + Arrays.toString(f));

                switch (seccion) {
                    case "AEROPUERTO":
                        System.out.println("[TRACE] Procesando aeropuerto...");
                        if (f.length >= 5) {
                            Aeropuerto a = new Aeropuerto(f[0], f[1], f[2], f[3], Boolean.parseBoolean(f[4]));
                            addAeropuerto(a);
                            System.out.println("[DEBUG] Aeropuerto agregado: " + a.getNombre());
                        }
                        break;

                    case "AVION":
                        System.out.println("[TRACE] Procesando avión...");
                        if (f.length >= 2) {
                            Avion av = new Avion(f[0], Integer.parseInt(f[1]));
                            addAvion(av);
                            System.out.println("[DEBUG] Avion agregado: " + av.getModelo());
                        }
                        break;

                    case "CLIENTE":
                        System.out.println("[TRACE] Procesando cliente...");
                        if (f.length >= 7) {
                            Cliente c = new Cliente(
                                f[0], f[1], f[2], f[3], f[4].charAt(0),
                                Boolean.parseBoolean(f[5]),
                                Long.parseLong(f[6])
                            );
                            addCliente(c);
                            System.out.println("[DEBUG] Cliente agregado: " + c.getNombreP());
                        }
                        break;

                    case "EMPLEADO":
                        System.out.println("[TRACE] Procesando empleado...");
                        if (f.length >= 6) {
                            Empleado e = new Empleado(f[0], f[1], f[2], f[3], f[4].charAt(0), f[5]);
                            addEmpleado(e);
                            System.out.println("[DEBUG] Empleado agregado: " + e.getNombreP());
                        }
                        break;

                    default:
                        System.out.println("[WARN] Línea ignorada en sección desconocida: " + seccion);
                }
            }

            // Resumen y flag
            log("Aeropuertos: " + getAllAeropuerto().length);
            log("Aviones:     " + getAllAvion().length);
            log("Clientes:    " + getAllCliente().length);
            log("Empleados:   " + getAllEmpleado().length);
            escribirFlag();
            log("Inicialización completada. Flag escrito.");

        } catch (java.io.IOException e) {
            System.err.println("Error cargando datos iniciales: " + e.getMessage());
        } finally {
            if (br != null) try { br.close(); } catch (java.io.IOException ignored) {}
        }
    }

    private File[] listarObj(File dir) { // Lista los archivos ".obj" de un directorio.
        File[] archivos = dir.listFiles(OBJ_FILTER); // Usa el filtro OBJ_FILTER declarado arriba.
        if (archivos == null) return new File[0];    // Si es null (error o vacío), retorna arreglo vacío.
        return archivos;                              // Retorna el arreglo de archivos encontrados.
    }

    private void escribirFlag() { // Crea/actualiza el archivo ".initialized".
        FileWriter fw = null; // Declaramos el escritor.
        try {
            fw = new FileWriter(flagInicializado); // Abrimos el escritor sobre el archivo flag.
            fw.write("ok\n"); // Escribimos una marca simple.
        } catch (IOException e) { // Capturamos errores al escribir.
            System.err.println("No se pudo escribir .initialized"); // Reportamos error.
        } finally { // Cerramos el escritor en finally.
            if (fw != null) { try { fw.close(); } catch (IOException ignored) {} } // Cerramos con manejo de excepción.
        }
    }

    private void escribirObjeto(File destino, Object obj) { // Serializa un objeto a archivo .obj.
        ObjectOutputStream oos = null; // Declaramos el stream de salida de objetos.
        try {
            oos = new ObjectOutputStream(new FileOutputStream(destino)); // Abrimos el stream sobre el archivo destino.
            oos.writeObject(obj); // Escribimos el objeto (requiere que la clase implemente Serializable).
        } catch (NotSerializableException nse) { // Si la clase no es Serializable...
            System.err.println("La clase no implementa Serializable: " + obj.getClass().getName()); // Avisamos.
            escribirComoTexto(destino, obj); // Hacemos fallback: guardamos como texto (.txt) usando toString().
        } catch (IOException e) { // Otros errores de E/S.
            System.err.println("Error escribiendo " + destino.getName() + ": " + e.getMessage()); // Reportamos error.
        } finally { // Cerramos el stream en finally.
            if (oos != null) { try { oos.close(); } catch (IOException ignored) {} } // Cerramos y silenciamos excepciones.
        }
    }

    private Object leerObjeto(File origen) { // Deserializa un objeto desde un archivo .obj.
        if (origen == null || !origen.exists()) return null; // Si no existe, devolvemos null.
        ObjectInputStream ois = null; // Declaramos el stream de entrada de objetos.
        try {
            ois = new ObjectInputStream(new FileInputStream(origen)); // Abrimos el stream sobre el archivo origen.
            return ois.readObject(); // Leemos el objeto y lo retornamos.
        } catch (Exception e) { // Capturamos IOException y ClassNotFoundException en un solo catch.
            System.err.println("Error leyendo " + origen.getName() + ": " + e.getMessage()); // Reportamos error.
            return null; // Devolvemos null si falló.
        } finally { // Cerramos el stream.
            if (ois != null) { try { ois.close(); } catch (IOException ignored) {} } // Cerramos con manejo de excepción.
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T[] leerObjetos(File directorio, Class<T> tipo) {
        if (directorio == null || !directorio.exists() || !directorio.isDirectory()) {
            System.err.println("Directorio no válido: " + directorio);
            return (T[]) java.lang.reflect.Array.newInstance(tipo, 0);
        }

        File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(".obj"));
        if (archivos == null || archivos.length == 0) {
            return (T[]) java.lang.reflect.Array.newInstance(tipo, 0);
        }

        T[] temp = (T[]) java.lang.reflect.Array.newInstance(tipo, archivos.length);
        int k = 0;

        for (File f : archivos) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                Object obj = ois.readObject();
                if (tipo.isInstance(obj)) {
                    temp[k++] = (T) obj;
                } else {
                    System.err.println("Archivo " + f.getName() + " no es del tipo esperado: " + tipo.getSimpleName());
                }
            } catch (Exception e) {
                System.err.println("Error leyendo " + f.getName() + ": " + e.getMessage());
            }
        }

        // recortar el arreglo si hubo errores o archivos inválidos
        if (k < temp.length) {
            T[] exacto = (T[]) java.lang.reflect.Array.newInstance(tipo, k);
            System.arraycopy(temp, 0, exacto, 0, k);
            return exacto;
        }

        return temp;
    }


    private void escribirComoTexto(File destinoBinario, Object obj) { // Guardado alternativo en .txt (snapshot).
        File txt = new File( // Creamos el File del .txt en la misma carpeta.
            destinoBinario.getParentFile(), // Carpeta contenedora.
            destinoBinario.getName().replace(".obj", ".txt") // Reemplazamos la extensión a .txt.
        );
        Writer w = null; // Declaramos un Writer para texto.
        try {
            w = new OutputStreamWriter(new FileOutputStream(txt), StandardCharsets.UTF_8); // Abrimos writer UTF-8.
            w.write(String.valueOf(obj)); // Escribimos el toString() del objeto.
            w.write("\n"); // Agregamos salto de línea final.
        } catch (IOException e) { // Capturamos errores de E/S.
            System.err.println("Error guardando fallback .txt: " + e.getMessage()); // Reportamos el error.
        } finally { // Cerramos el writer.
            if (w != null) { try { w.close(); } catch (IOException ignored) {} } // Cerramos con manejo de excepción.
        }
    }

    // ==================== Parsers auxiliares ====================

    private String[] partir(String linea, String sep) { // Parte una línea por un separador y hace trim a cada campo.
        // Calculamos cuántos separadores hay para dimensionar el arreglo manualmente (evitando colecciones).
        int count = 1; // Mínimo un campo.
        for (int i = 0; i < linea.length(); i++) { // Recorremos la línea carácter por carácter.
            if (linea.charAt(i) == sep.charAt(0)) count = count + 1; // Contamos '|' como separador.
        }
        String[] campos = new String[count]; // Creamos el arreglo con el número de campos.
        int idx = 0; // Índice actual dentro del arreglo de salida.
        int inicio = 0; // Posición de inicio del próximo campo en la cadena.
        for (int i = 0; i < linea.length(); i++) { // Recorremos la línea nuevamente.
            if (linea.charAt(i) == sep.charAt(0)) { // Si encontramos el separador.
                String parte = linea.substring(inicio, i).trim(); // Extraemos y hacemos trim del campo.
                campos[idx] = parte; // Guardamos en el arreglo.
                idx = idx + 1; // Pasamos al siguiente índice.
                inicio = i + 1; // El siguiente campo inicia después del separador.
            }
        }
        String ultima = linea.substring(inicio).trim(); // Tomamos el último campo (hasta el final de la línea).
        campos[idx] = ultima; // Guardamos el último campo en el arreglo.
        return campos; // Devolvemos el arreglo de campos.
    }

    private boolean esCabecera(String[] f) {
        if (f.length == 0) return true;
        String s = f[0].toLowerCase();
        return s.contains("nombre") || s.contains("modelo") || s.contains("codigo") || s.contains("tipoDoc");
    }
    
    private boolean parseBool(String s) { // Convierte un String a boolean de forma tolerante.
        if (s == null) return false; // Nulo => false.
        String x = s.trim().toLowerCase(); // Normalizamos.
        // Aceptamos varias representaciones de verdadero.
        return x.equals("true") || x.equals("t") || x.equals("1") || x.equals("si") || x.equals("sí");
    }

    private int parseInt(String s, int def) { // Convierte String a int con valor por defecto.
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; } // Intenta parsear, si falla devuelve def.
    }

    private long parseLong(String s, long def) { // Convierte String a long con valor por defecto.
        try { return Long.parseLong(s.trim()); } catch (Exception e) { return def; } // Intenta parsear, si falla devuelve def.
    }

    private char parseChar(String s, char def) { // Devuelve el primer carácter del String o el por defecto.
        if (s == null) return def; // Nulo => default.
        String t = s.trim(); // Quitamos espacios.
        if (t.length() == 0) return def; // Vacío => default.
        return t.charAt(0); // Primer carácter.
    }
}