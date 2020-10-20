package com.service;

import com.exception.BadRequestException;
import com.exception.ObjectNotFoundException;
import com.model.Item;
import org.springframework.stereotype.Service;

@Service
public interface ItemService {

    Item save(Item item) throws BadRequestException;

    Item findById(Long id) throws ObjectNotFoundException;

    Item update(Item item) throws BadRequestException, ObjectNotFoundException;

    void delete(Long id) throws ObjectNotFoundException;

    void deleteByName(String name) throws BadRequestException;
}
