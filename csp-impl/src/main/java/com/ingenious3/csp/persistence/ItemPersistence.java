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
 * Date: 29/03/2015
 */
package com.ingenious3.csp.persistence;

import com.ingenious3.annotations.Mutable;
import com.ingenious3.builder.ImmutableItemsBuilder;
import com.ingenious3.csp.element.Decorated;
import com.ingenious3.csp.element.FactoryImpl;
import com.ingenious3.csp.element.Item;
import com.ingenious3.csp.element.item.ItemAddition;
import com.ingenious3.csp.element.item.ItemDeletion;
import com.ingenious3.csp.element.item.ItemRevertAddition;
import com.ingenious3.csp.element.item.ItemRevertDeletion;
import com.ingenious3.csp.reader.IItemsReader;
import com.ingenious3.csp.writer.IItemsWriter;
import com.ingenious3.exceptions.IngeniousExceptionsFactory;
import com.ingenious3.identifier.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.ingenious3.validation.IValidate.validate;

@Mutable
public final class ItemPersistence implements IItemPersistence {

    private final static Logger LOG = LoggerFactory.getLogger(ItemPersistence.class);

    private final PERSIST_STRATEGY persistStrategy;
    private final IItemsReader read;
    private final IItemsWriter write;

    private ItemPersistence(IItemsReader itemsReader, IItemsWriter itemsWriter, PERSIST_STRATEGY persistStrategy){
        this.read = itemsReader;
        this.write = itemsWriter;
        this.persistStrategy = persistStrategy;
    }

    public static ItemPersistence valueOf(IItemsReader itemsReader, IItemsWriter itemsWriter, PERSIST_STRATEGY persistStrategy) {
        validate(itemsReader);
        validate(itemsWriter);
        validate(persistStrategy);

        return new ItemPersistence(itemsReader, itemsWriter, persistStrategy);
    }

    @Override
    public Item get(UI ui) {
        validate(ui);
        validate(read.containsKey(ui));

        return read.get(ui);
    }

    @Override
    public IPersistence<Item> add(Item item) {
        validate(item);
        validate(!read.containsKey(item), IngeniousExceptionsFactory.illegalArgument("Item {} already exists in reader.", item));

        this.write.add(item);
        if(alwaysPersist()){
            return persist();
        }
        return this;
    }

    @Override
    public IItemsReader itemsToPersist() {
        return FactoryImpl.itemsReader(write.items());
    }

    @Override
    public IPersistence<Item> remove(Item item) {
        validate(item);
        validate(!read.containsKey(item), IngeniousExceptionsFactory.illegalArgument("Item {} doesn't exist in reader.", item));

        this.write.markDeleted(item);
        if(alwaysPersist()){
            return persist();
        }
        return this;
    }

    @Override
    public IPersistence<Item> persist() {
        LOG.info("You are persisting items, therefore this IMMUTABLE persistence instance stays as a fingerprint only. Use proper assignment of the new persistence.");

        Set<Item> items = new ImmutableItemsBuilder<>(this.read.items())
        .addAll(this.write.items())
        .removeAll(this.write.deleteItems())
        .addAll(this.write.addItems()).build();

        ItemsReader reader = FactoryImpl.itemsReader(items);
        return ItemPersistence.valueOf(reader, ItemsWriter.empty(), persistStrategy);
    }

    @Override
    public IPersistence<Item> persist(Decorated<Item> decorator) {
        validate(decorator);
        validate(decorator.items());
        validate(read.items());

        final ImmutableItemsBuilder<Item> builder = new ImmutableItemsBuilder<>(read.items());

        decorator.items().parallelStream().forEach(item -> {
            if(item instanceof ItemAddition){
                builder.add(item.item());
            }else if(item instanceof ItemRevertAddition){
                builder.remove(item.item());
            }else if(item instanceof ItemDeletion){
                builder.remove(item.item());
            }else if(item instanceof ItemRevertDeletion){
                builder.add(item.item());
            }
        });

        ItemsReader reader = FactoryImpl.itemsReader(builder.build());
        return ItemPersistence.valueOf(reader, ItemsWriter.empty(), persistStrategy);
    }

    @Override
    public boolean alwaysPersist() {
        return PERSIST_STRATEGY.ALWAYS_PERSIST.equals(persistStrategy);
    }


}
