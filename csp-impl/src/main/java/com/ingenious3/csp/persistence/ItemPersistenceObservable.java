/*
 * (C) Copyright 2015 Charlie The Guy (http://ingenious3.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU GENERAL PUBLIC LICENSE
 * version 3 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.0.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Charlie The Guy
 * Date: 08/04/2015
 */
package com.ingenious3.csp.persistence;

import com.ingenious3.annotations.Mutable;
import com.ingenious3.csp.element.Decorated;
import com.ingenious3.csp.element.FactoryImpl;
import com.ingenious3.csp.element.IObservableItemsWriter;
import com.ingenious3.csp.element.Item;
import com.ingenious3.csp.element.ItemDecorator;
import com.ingenious3.csp.reader.IItemsReader;
import com.ingenious3.csp.reader.Reader;
import com.ingenious3.exceptions.IngeniousExceptionsFactory;
import com.ingenious3.identifier.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

import static com.ingenious3.validation.IValidate.validate;

@Mutable
public final class ItemPersistenceObservable implements IPersistenceDecorator, Observer {

    private static final Logger LOG = LoggerFactory.getLogger(ItemPersistenceObservable.class);

    private IPersistence<Item> persistence;

    public ItemPersistenceObservable(IItemPersistence persistence) {
        this.persistence = persistence;
    }


    public static ItemPersistenceObservable valueOf(IItemsReader itemsReader, IObservableItemsWriter itemsWriter, PERSIST_STRATEGY persistStrategy) {
        validate(itemsReader);
        validate(itemsWriter);

        ItemPersistenceObservable persistence = new ItemPersistenceObservable(FactoryImpl.createItemPersistence(itemsReader, itemsWriter, persistStrategy));
        itemsWriter.addObserver(persistence);
        return persistence;
    }


    @Override
    public void update(Observable o, Object arg) {
        validate(arg);

        if (!(arg instanceof ItemDecorator)) {
            throw IngeniousExceptionsFactory.illegalArgument("ItemDecorator type expected, not {}.", arg.getClass());
        }

        ItemDecorator itemDecorator = (ItemDecorator) arg;

        if (persistence.alwaysPersist()) {
            this.persistence = persist(itemDecorator);
        }
        LOG.info("================= Tried to work it out.{}", itemDecorator);
    }


    @Override
    public Item get(UI id) {
        return persistence.get(id);
    }

    @Override
    public IPersistence<Item> add(Item item) {
        return persistence.add(item);
    }

    @Override
    public Reader<Item> itemsToPersist() {
        return persistence.itemsToPersist();
    }

    @Override
    public IPersistence<Item> remove(Item item) {
        persistence.remove(item);
        return this;
    }

    private IPersistence<Item> persist(ItemDecorator decorator) {
        Decorated<Item> decoratedItems = FactoryImpl.decoratedItems(decorator);
        return persist(decoratedItems);
    }

    @Override
    public IPersistence<Item> persist(Decorated<Item> decorator) {
        return persistence.persist(decorator);
    }

    @Override
    public boolean alwaysPersist() {
        return persistence.alwaysPersist();
    }

    @Override
    public IPersistence<Item> persist() {
        return persistence.persist();
    }
}
