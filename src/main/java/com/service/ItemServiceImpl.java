package com.service;

import com.DAO.ItemDAO;
import com.exception.BadRequestException;
import com.exception.ObjectNotFoundException;
import com.model.Item;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;

    @Autowired
    public ItemServiceImpl(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public Item save(Item item) throws BadRequestException {
        try {
            validateItem(item);
            itemDAO.checkItemName(item.getName());

            return itemDAO.save(item);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cant save item " + item.getName() + " : " + e.getMessage());
        }
    }

    public Item findById(Long id) throws ObjectNotFoundException {
        return itemDAO.findById(id);
    }

    public Item update(Item item) throws BadRequestException, ObjectNotFoundException {
        try {
            validateUpdate(item);

            return itemDAO.update(item);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cant update item " + item.getId() + " : " + e.getMessage());
        }
    }

    public void delete(Long id) throws ObjectNotFoundException {
        itemDAO.delete(findById(id));
    }

    public void deleteByName(String name) throws BadRequestException {
        try {
            itemDAO.checkItemContainsName(name);

            itemDAO.deleteByName(name);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cant delete items with name " + name + " : " + e.getMessage());
        }
    }

    private void validateItem(Item item) throws BadRequestException {

        if (item.getName().length() > 50) {
            throw new BadRequestException("name length must be <= 50");
        }
        if (item.getDescription().length() > 200) {
            throw new BadRequestException("name description must be <= 200");
        }
    }

    private void validateUpdate(Item item) throws BadRequestException, ObjectNotFoundException {

        Item oldItem = findById(item.getId());
        validateItem(item);

        if (!item.getName().equals(oldItem.getName())) {
            itemDAO.checkItemName(item.getName());
        }

        item.setDateCreated(oldItem.getDateCreated());
    }
}
