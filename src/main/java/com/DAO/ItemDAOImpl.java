package com.DAO;

import com.exception.BadRequestException;
import com.exception.InternalServerException;
import com.exception.ObjectNotFoundException;
import com.model.Item;
import org.hibernate.HibernateException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

@Transactional
public class ItemDAOImpl implements ItemDAO {

    private static final String DELETE_BY_NAME_QUERY = "DELETE FROM ITEM WHERE NAME LIKE :name";
    private static final String CHECK_ITEM_NAME_QUERY = "SELECT COUNT(*) FROM ITEM WHERE NAME = :name";
    private static final String CHECK_ITEM_CONTAINS_NAME_QUERY = "SELECT COUNT(*) FROM ITEM WHERE NAME LIKE :name";
    @PersistenceContext
    private EntityManager entityManager;

    public Item save(Item item) throws InternalServerException {
        try {
            item.setDateCreated(new Date());
            item.setLastUpdatedDate(new Date());

            entityManager.persist(item);
            return item;
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new InternalServerException(e.getMessage());
        }
    }

    public Item findById(Long id) throws ObjectNotFoundException, InternalServerException {
        try {
            Item item = entityManager.find(Item.class, id);

            if (item == null) {
                throw new ObjectNotFoundException("missed item with id " + id);
            }

            return item;
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new InternalServerException(e.getMessage());
        }
    }

    public Item update(Item item) throws InternalServerException {
        try {
            item.setDateCreated(item.getDateCreated());
            item.setLastUpdatedDate(new Date());

            return entityManager.merge(item);

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new InternalServerException(e.getMessage());
        }
    }

    public void delete(Item item) throws InternalServerException {
        try {
            entityManager.remove(entityManager.merge(item));

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new InternalServerException(e.getMessage());
        }
    }

    public void deleteByName(String name) throws InternalServerException {
        try {
            entityManager.createNativeQuery(DELETE_BY_NAME_QUERY)
                    .setParameter("name", "%" + name + "%")
                    .executeUpdate();

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new InternalServerException(e.getMessage());
        }
    }

    public void checkItemName(String name) throws BadRequestException, InternalServerException {
        try {
            BigDecimal db = (BigDecimal) entityManager.createNativeQuery(CHECK_ITEM_NAME_QUERY)
                    .setParameter("name", name)
                    .getSingleResult();

            if (db.longValue() != 0) {
                throw new BadRequestException("item with name " + name + " already exists");
            }
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new InternalServerException(e.getMessage());
        }
    }

    public void checkItemContainsName(String name) throws BadRequestException, InternalServerException {
        try {
            BigDecimal db = (BigDecimal) entityManager.createNativeQuery(CHECK_ITEM_CONTAINS_NAME_QUERY)
                    .setParameter("name", "%" + name + "%")
                    .getSingleResult();

            if (db.longValue() == 0) {
                throw new BadRequestException("missed items that contains name " + name);
            }
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new InternalServerException(e.getMessage());
        }
    }
}
