package com.DAO;

import com.exception.BadRequestException;
import com.exception.ObjectNotFoundException;
import com.model.Item;

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

    public Item save(Item item) {

        item.setDateCreated(new Date());
        item.setLastUpdatedDate(new Date());

        entityManager.persist(item);
        return item;
    }

    public Item findById(Long id) throws ObjectNotFoundException {
        Item item = entityManager.find(Item.class, id);

        if (item == null) {
            throw new ObjectNotFoundException("missed item with id " + id);
        }

        return item;
    }

    public Item update(Item item) {

        item.setDateCreated(item.getDateCreated());
        item.setLastUpdatedDate(new Date());

        return entityManager.merge(item);
    }

    public void delete(Item item) {
        entityManager.remove(entityManager.merge(item));
    }

    public void deleteByName(String name) {
        entityManager.createNativeQuery(DELETE_BY_NAME_QUERY)
                .setParameter("name", "%" + name + "%")
                .executeUpdate();
    }

    public void checkItemName(String name) throws BadRequestException {
        BigDecimal db = (BigDecimal) entityManager.createNativeQuery(CHECK_ITEM_NAME_QUERY)
                .setParameter("name", name)
                .getSingleResult();

        if (db.longValue() != 0) {
            throw new BadRequestException("item with name " + name + " already exists");
        }
    }

    public void checkItemContainsName(String name) throws BadRequestException {
        BigDecimal db = (BigDecimal) entityManager.createNativeQuery(CHECK_ITEM_CONTAINS_NAME_QUERY)
                .setParameter("name", "%" + name + "%")
                .getSingleResult();

        if (db.longValue() == 0) {
            throw new BadRequestException("missed items that contains name " + name);
        }
    }
}
