package com.DAO;

import com.exception.BadRequestException;
import com.exception.ObjectNotFoundException;
import com.model.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDAO {

    Item save(Item item);

    Item findById(Long id) throws ObjectNotFoundException;

    Item update(Item item);

    void delete(Item item);

    void deleteByName(String name);

    void checkItemName(String name) throws BadRequestException;

    void checkItemContainsName(String name) throws BadRequestException;
}
