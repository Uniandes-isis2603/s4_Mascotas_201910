/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mascotas.persistence;

import co.edu.uniandes.csw.mascotas.entities.ArticuloEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Daniela Gonzalez
 */

@Stateless
public class ArticuloPersistence {
    
    @PersistenceContext (unitName = "mascotasPU")
    protected EntityManager em;
    
    
     public ArticuloEntity create(ArticuloEntity articuloEntity){
        
        em.persist(articuloEntity);
        return articuloEntity;
    }
     
     public ArticuloEntity find(Long articuloId){
        
        return em.find(ArticuloEntity.class, articuloId);
    }
     
     public List<ArticuloEntity> findAll(){
        
        TypedQuery query = em.createQuery("select u from ArticuloEntity u", ArticuloEntity.class);
        return query.getResultList();
    }
    
}