/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mascotas.resources;

import co.edu.uniandes.csw.mascotas.dtos.MascotaDTO;
import co.edu.uniandes.csw.mascotas.ejb.MascotaLogic;
import co.edu.uniandes.csw.mascotas.entities.MascotaEntity;
import co.edu.uniandes.csw.mascotas.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Natalia Sanabria Forero
 */
@Path("mascotas")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class MascotaResource {
    
    private static final Logger LOGGER = Logger.getLogger(MascotaResource.class.getName());
    
    @Inject
    private MascotaLogic logica;
    
    @POST
    public MascotaDTO crearMascota(MascotaDTO mascota) throws BusinessLogicException
    {
       MascotaEntity entidad = mascota.toEntity();
       entidad = logica.crearMascota(entidad);
       return new MascotaDTO(entidad);
    }
    
    @GET
    @Path("{mascotaId: \\d+}")
    public MascotaDTO darMascota( @PathParam("MascotaId") Long mascotaId ) throws WebApplicationException
    {
        MascotaEntity entidad;
        entidad = logica.buscarMascotaPorId(mascotaId);
        if(entidad == null)
            throw new WebApplicationException("El recurso /mascotas/"+mascotaId+" no existe.", 404);
        return new MascotaDTO(entidad);
    }

    /**
     * Retorna una lista con las mascotas cuyo estado es el ingresado por parámetro
     * @param pEstado
     * @return
     * @throws BusinessLogicException 
     */
    @GET
    @Path("/estado/{mascotaEstado: \\d+}")
    public List<MascotaDTO> darMascotasPorEstado( @PathParam("MascotaEstado")String pEstado ) throws BusinessLogicException
    {
        List<MascotaDTO> mascotas = new ArrayList<>();
        List<MascotaEntity> entidades = logica.darMascotasPorEstado(pEstado);
        entidades.forEach((entidad) -> { mascotas.add(new MascotaDTO(entidad));});
        return mascotas;
    }

    /**
     * Actualiza el estado de una mascota con el estado dado por parámetro
     * @param id
     * @param mascota Mascota a la cual se le actualizará el estado
     * @return mascotaDTO mascota con el estado actualizado
     * @throws co.edu.uniandes.csw.mascotas.exceptions.BusinessLogicException
     */
    @PUT
    @Path("{id: \\d+}")
    public MascotaDTO actualizarEstadoMascota( @PathParam("id") Long id, MascotaDTO mascota ) throws BusinessLogicException
    {
       mascota.setId(id);
       if(logica.buscarMascotaPorId(id)==null)
           throw new WebApplicationException("El recurso /mascotas/"+id+" no existe.",404);
       MascotaDTO dto = new MascotaDTO(logica.cambiarEstadoMascota(id, mascota.toEntity()));
       return dto;
    }
    
    @GET
    public List<MascotaDTO> darTodasLasMascotas( )
    {
        List<MascotaDTO> respuesta = new ArrayList<>();
        List<MascotaEntity> list = logica.darTodasLasMascotas();
        for(MascotaEntity entity: list)
            respuesta.add(new MascotaDTO(entity));
        return respuesta;
    }
}
