package baseSistema;

/**
 *
 * @author mavg
 */
public class NombresTablas {

    private final String cat_autoridades = "cat_autoridades";
    private final String cat_autorizadores = "cat_autorizados";
    private final String cat_datos_la = "cat_datos_la";
    private final String cat_destinos = "cat_destinos";
    private final String cat_filtros = "cat_filtros";
    private final String cat_oficinas = "cat_oficinas";
    private final String cat_procedimientos = "cat_procedimientos";
    private final String cat_sentido_ejecutoria = "cat_sentidos_ejecutoria";
    private final String cat_supuestos = "cat_supuestos";
    private final String cat_tipo_amparo = "cat_tipo_amparos";
    private final String cat_tipo_auto = "cat_tipo_autos";
    private final String cat_tipo_auto_sria_acuerdos = "cat_tipo_autos_sria_acuerdos";
    private final String cat_tipo_autoridad = "cat_tipo_autoridades";
    private final String cat_tipo_documenot_asr = "cat_tipo_documentos_asr";
    private final String cat_tipo_notificacion = "cat_tipo_notificaciones";
    private final String cat_tipo_partes = "cat_tipo_partes";
    private final String cat_tipo_pretension = "cat_tipo_pretensiones";
    private final String cat_tipo_promociones = "cat_tipo_promociones";
    private final String cat_tipo_pruebas = "cat_tipo_pruebas";
    private final String cat_tipo_sentencia = "cat_tipo_sentencias";
    private final String gen_calendario = "gen_calendario";
    private final String gen_municipios = "gen_municipios";
    private final String gen_usuarios = "gen_usuarios";
    private final String pri_actores = "pri_actores";
    private final String pri_expedientes = "pri_expediente";
    private final String pri_promociones = "pri_promociones";
    private final String pri_terceros_actores = "pri_terceros_actores";
    private final String relExp_Autoridad = "rel_expediente_has_cat_autoridades";
    private final String relExp_Autorizado = "rel_expediente_has_cat_autorizados";
    private final String relExp_Actor = "rel_expediente_has_pri_actores";
    private final String relExp_Promocion = "rel_expediente_has_pri_promociones";
    private final String relExp_TerActor = "rel_expediente_has_pri_terceros_actores";
    private final String relExp_TerAutoridad = "rel_expediente_has_pri_terceros_autoridades";
    private final String pri_archivo = "pri_archivo_expedientes";
    private final String pri_anexo = "pri_anexo_expedientes";
    
    public NombresTablas() {
    }

    public String getCat_autoridades() {
        return cat_autoridades;
    }

    public String getCat_autorizadores() {
        return cat_autorizadores;
    }

    public String getCat_datos_la() {
        return cat_datos_la;
    }
    public String getCat_destinos() {
        return cat_destinos;
    }

    public String getCat_filtros() {
        return cat_filtros;
    }

    public String getCat_oficinas() {
        return cat_oficinas;
    }
    
    public String getCat_procedimientos() {
        return cat_procedimientos;
    }

    public String getCat_sentido_ejecutoria() {
        return cat_sentido_ejecutoria;
    }

    public String getCat_supuestos() {
        return cat_supuestos;
    }

    public String getCat_tipo_amparo() {
        return cat_tipo_amparo;
    }

    public String getCat_tipo_auto() {
        return cat_tipo_auto;
    }

    public String getCat_tipo_auto_sria_acuerdos() {
        return cat_tipo_auto_sria_acuerdos;
    }

    public String getCat_tipo_autoridad() {
        return cat_tipo_autoridad;
    }

    public String getCat_tipo_documenot_asr() {
        return cat_tipo_documenot_asr;
    }

    public String getCat_tipo_notificacion() {
        return cat_tipo_notificacion;
    }

    public String getCat_tipo_partes() {
        return cat_tipo_partes;
    }

    public String getCat_tipo_pretension() {
        return cat_tipo_pretension;
    }

    public String getCat_tipo_promociones() {
        return cat_tipo_promociones;
    }

    public String getCat_tipo_pruebas() {
        return cat_tipo_pruebas;
    }

    public String getCat_tipo_sentencia() {
        return cat_tipo_sentencia;
    }

    public String getGen_calendario() {
        return gen_calendario;
    }

    public String getGen_municipios() {
        return gen_municipios;
    }

    public String getGen_usuarios() {
        return gen_usuarios;
    }

    public String getPri_promociones() {
        return pri_promociones;
    }

    public String getPri_actores() {
        return pri_actores;
    }

    public String getPri_expedientes() {
        return pri_expedientes;
    }

    public String getPri_terceros_actores() {
        return pri_terceros_actores;
    }

    public String getRelExp_Autoridad() {
        return relExp_Autoridad;
    }

    public String getRelExp_Autorizado() {
        return relExp_Autorizado;
    }

    public String getRelExp_Actor() {
        return relExp_Actor;
    }

    public String getRelExp_Promocion() {
        return relExp_Promocion;
    }

    public String getRelExp_TerActor() {
        return relExp_TerActor;
    }

    public String getRelExp_TerAutoridad() {
        return relExp_TerAutoridad;
    }

    public String getPri_archivo() {
        return pri_archivo;
    }

    public String getPri_anexo() {
        return pri_anexo;
    }
    
}
