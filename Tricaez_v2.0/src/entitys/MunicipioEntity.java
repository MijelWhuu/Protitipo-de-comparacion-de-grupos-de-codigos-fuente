package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class MunicipioEntity {

    private Integer id_municipio;
    private String nombre;
    
    public MunicipioEntity() {
        this.id_municipio = -1;
        this.nombre = "[Elija una opción]";
    }

    public MunicipioEntity(Integer id_municipio) {
        this.id_municipio = id_municipio;
    }
    
    public MunicipioEntity(Integer id_municipio, String nombre) {
        this.id_municipio = id_municipio;
        this.nombre = nombre;
    }
    
    public Integer getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(Integer id_municipio) {
        this.id_municipio = id_municipio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id_municipio);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MunicipioEntity other = (MunicipioEntity) obj;
        if (!Objects.equals(this.id_municipio, other.id_municipio)) {
            return false;
        }
        return true;
    }
    
}
