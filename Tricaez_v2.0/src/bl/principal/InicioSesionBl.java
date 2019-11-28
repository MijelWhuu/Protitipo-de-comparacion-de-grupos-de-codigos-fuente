package bl.principal;

import dao.principal.InicioSesionDao;
import entitys.SesionEntity;

/**
 *
 * @author mavg
 */
public class InicioSesionBl {
    
    InicioSesionDao dao = new InicioSesionDao();

    public InicioSesionBl() {
    }
    
    public SesionEntity VerificaUsuario(String usuario, String contraseña) {
        return dao.cargaUsuarios(usuario,contraseña);
    }
    
}
