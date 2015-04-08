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

import com.ingenious3.annotations.Immutable;
import com.ingenious3.builder.ImmutableItemsBuilder;
import com.ingenious3.csp.element.Item;
import com.ingenious3.csp.reader.IItemsReader;
import com.ingenious3.csp.writer.IItemsWriter;
import com.ingenious3.exceptions.IngeniousExceptionsFactory;
import com.ingenious3.identifier.UI;
import com.ingenious3.validation.IValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Immutable
public final class ItemPersistence implements IItemPersistence {

    private static final Logger LOG = LoggerFactory.getLogger(ItemPersistence.class);

    private final boolean alwaysPersist;
    private final IItemsReader read;
    private final IItemsWriter write;

    private ItemPersistence(IItemsReader itemsReader, IItemsWriter itemsWriter, boolean alwaysPersist){
        this.read = itemsReader;
        this.write = itemsWriter;
        this.alwaysPersist = alwaysPersist;
    }

    public static ItemPersistence valueOf(IItemsReader itemsReader, IItemsWriter itemsWriter, boolean alwaysPersist) {
        IValidate.validate(itemsReader);
        IValidate.validate(itemsWriter);

        ItemPersistence persistence = new ItemPersistence(itemsReader, itemsWriter, alwaysPersist);
        return persistence;
    }

    @Override
    public Item get(UI ui) {
        IValidate.validate(ui);

        if(alwaysPersist && write.containsKey(ui)){
            return write.get(ui);
        }
        if(alwaysPersist && write.markedDeleted(ui)){
            throw IngeniousExceptionsFactory.illegalArgument("Argument with UI {} was marked as deleted.", ui);
        }
        if(read.containsKey(ui)){
            return read.get(ui);
        }

        throw IngeniousExceptionsFactory.illegalArgument("Argument with UI {} not found.", ui);
    }

    @Override
    public ItemPersistence add(Item item) {
        IValidate.validate(item);

        this.write.add(item);
        return this;
    }

    @Override
    public IItemsReader itemsToPersist() {
        return (IItemsReader)(new ImmutableItemsBuilder<Item>().addAll(write.items())).build();
    }

}
