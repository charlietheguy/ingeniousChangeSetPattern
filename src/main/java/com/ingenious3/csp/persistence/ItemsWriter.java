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

import com.ingenious3.builder.ImmutableItemsBuilder;
import com.ingenious3.collections.AbstractIItems;
import com.ingenious3.collections.IItems;
import com.ingenious3.csp.element.Item;
import com.ingenious3.csp.writer.IItemsWriter;
import com.ingenious3.exceptions.IngeniousExceptionsFactory;
import com.ingenious3.identifier.Identifier;
import com.ingenious3.util.IngeniousUtils;
import com.ingenious3.validation.IValidate;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@NotThreadSafe
public final class ItemsWriter extends AbstractIItems<Item> implements IItemsWriter {

    private final Map<Identifier, Item> toDelete;
    private final Map<Identifier, Item> toAdd;


    private ItemsWriter(Set<Item> set) {
        super(set);
        this.toDelete = IngeniousUtils.newConcurrentMap();
        this.toAdd = IngeniousUtils.newConcurrentMap();
    }

    public static ItemsWriter valueOf(Set<Item> set) {
        return new ItemsWriter(set);
    }

    public void add(Item item) {
        IValidate.validate(item);

        this.toAdd.put(item, item);
    }

    public void change(Item original, Item change) {
        IValidate.validate(original);
        IValidate.validate(change);

        revertAdd(original);
        add(change);
    }

    @Override
    public void markDeleted(Identifier ui) {
        IValidate.validate(ui);

        toDelete.put(ui, get(ui));
    }

    @Override
    public void revertMarkDeleted(Identifier ui) {
        IValidate.validate(ui);

        if(IItems.containsValue(get(ui), toDelete)){
            toDelete.remove(ui);
            return;
        }

        throw IngeniousExceptionsFactory.illegalArgument("Key ui {} does not exist in the map with items marked as deleted.", ui);
    }

    @Override
    public boolean markedDeleted(Identifier ui) {
        IValidate.validate(ui);

        return toDelete.containsKey(ui);
    }


    public void revertAdd(Identifier original) {
        IValidate.validate(original);
        IValidate.validate(this.toAdd.containsKey(original));

        this.toAdd.remove(original);
    }

    public Item getAdd(Identifier ui) {
        IValidate.validate(ui);
        IValidate.validate(this.toAdd.containsKey(ui));

        return this.toAdd.get(ui);
    }

    public Item getDelete(Identifier ui) {
        IValidate.validate(ui);
        IValidate.validate(this.toDelete.containsKey(ui));

        return this.toDelete.get(ui);
    }

    @Override
    public Set<Item> deleteItems() {
        Set<Item> set = new ImmutableItemsBuilder<>(new LinkedHashSet<>(this.toDelete.values())).build();
        return IItems.unmodifiableSet(set);
    }

    @Override
    public Set<Item> addItems() {
        Set<Item> set = new ImmutableItemsBuilder<>(new LinkedHashSet<>(this.toAdd.values())).build();
        return IItems.unmodifiableSet(set);
    }


    public static ItemsWriter empty(){return valueOf(IngeniousUtils.newConcurrentSet());}
}
