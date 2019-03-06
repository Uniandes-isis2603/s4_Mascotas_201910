/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mascotas.ejb;

import co.edu.uniandes.csw.mascotas.entities.MascotaEntity;
import co.edu.uniandes.csw.mascotas.persistence.MascotaPersistence;
import javax.ejb.Stateless;
import javax.inject.Inject;
import co.edu.uniandes.csw.mascotas.exceptions.BusinessLogicException;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Natalia Sanabria Forero (n.sanabria)
 */
@Stateless
public class MascotaLogic 
{
    // ---------------------------------------
    // Constantes
    // ---------------------------------------
    
    private static final Logger LOGGER = Logger.getLogger(MascotaLogic.class.getName());
    
    /**
     * Constante que define el tipo de mascota perro 
     */
    public final static String PERRO = "PERRO";
    
    /**
     * Constante que define el tipo de mascota gato
     */
    public final static String GATO = "GATO";
        
    @Inject
    private MascotaPersistence persistencia;
    
    /**
     * Crea una nueva mascota por medio de la entidad ingresada por parámetro
     * @param mascota
     * @return
     * @throws BusinessLogicException 
     */
    public MascotaEntity crearMascota( MascotaEntity mascota ) throws BusinessLogicException
    {
        //Validación de reglas de negocio
        if( !mascota.getTipo().equals(PERRO) && !mascota.getTipo().equals(GATO))
        {
            throw new BusinessLogicException("Una mascota sólo puede ser perro o gato");
        }
        if(mascota.getFotos()== null)
        {
            throw new BusinessLogicException("Por favor incluya al menos una foto o video");
        }
        if(mascota.getDescripcion()==null)
        {
            throw new BusinessLogicException("Es necesario diligenciar el campo de descripción");
        }
        mascota = persistencia.create(mascota);
        return mascota;
    }
    
    /**
     * Busca una mascota por su identificador
     * @param id Identificador único de la mascota
     * @return buscada
     */
    public MascotaEntity buscarMascotaPorId( Long id )
    {
        MascotaEntity buscada = persistencia.find(id);
        return buscada;
    }
    
    /**
     * Retorna una lista con todas las mascotas que hay en la base de datos
     * @return 
     */
    public List<MascotaEntity> darTodasLasMascotas( )
    {
        List<MascotaEntity> respuesta = persistencia.findAll();
        return respuesta;
        
    }
    
    public MascotaEntity cambiarEstadoMascota(Long pId, MascotaEntity mascota) throws BusinessLogicException
    {
        String estadoMascota = mascota.getEstado().name();
        MascotaEntity.Estados_mascota[] estados = MascotaEntity.Estados_mascota.values();
        boolean found = false;
        for (MascotaEntity.Estados_mascota estado : estados) {
            if (estadoMascota.equals(estado.name())) {
                found = true;
                persistencia.actualizarEstadoMascota(mascota);
            }
        }
        if (!found ) 
        {
            throw new BusinessLogicException("El estado de la mascota no corresponde a un estado válido.");
        }
        return mascota;

        
    }
    
    public List<MascotaEntity> darMascotasPorEstado(String pEstado) throws BusinessLogicException
    {
        // Validar reglas de negocio (estado pertenece a los posibles estados)
         MascotaEntity.Estados_mascota[] estados = MascotaEntity.Estados_mascota.values();
        boolean found = false;
        for (MascotaEntity.Estados_mascota estado : estados) {
            if (pEstado.equals(estado.name())) {
                found = true;
            }
        }
        if(!found)
            throw new BusinessLogicException( " El estado ingresado no es válido. ");
        
        return persistencia.darMascotasPorEstado(pEstado);
    }
}
