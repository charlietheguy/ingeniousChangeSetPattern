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
import com.ingenious3.annotations.Review;
import com.ingenious3.builder.ImmutableItemsBuilder;
import com.ingenious3.collections.AbstractIItems;
import com.ingenious3.collections.IItems;
import com.ingenious3.csp.element.Item;
import com.ingenious3.csp.writer.IItemsWriter;
import com.ingenious3.exceptions.IngeniousExceptionsFactory;
import com.ingenious3.identifier.Identifier;
import com.ingenious3.util.IngeniousUtils;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.ingenious3.validation.IValidate.validate;

@NotThreadSafe
@Mutable
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

    @Review("Review, whether the addition of already existing argument should be treated strictly. Behaving strictly now.")
    public void add(Item item) {
        validate(item);
        validate(!toAdd.containsKey(item), IngeniousExceptionsFactory.illegalArgument("You are attempting to add an already existing argument {}.", item));

        this.toAdd.put(item, item);
    }

    @Override
    public void markDeleted(Identifier ui) {
        validate(ui);
        validate(containsKey(ui), IngeniousExceptionsFactory.illegalArgument("You are attempting to delete a not existing argument {}.", ui));
        validate(!toDelete.containsKey(ui), IngeniousExceptionsFactory.illegalArgument("You are attempting to delete already deleted argument {}.", ui));

        toDelete.put(ui, get(ui));
    }

    @Override
    public void revertMarkDeleted(Identifier ui) {
        validate(ui);
        validate(IItems.containsValue(get(ui), toDelete), IngeniousExceptionsFactory.illegalArgument("Key ui {} does not exist in the map with items marked as deleted.", ui));

        toDelete.remove(ui);
    }

    @Override
    public boolean markedDeleted(Identifier ui) {
        validate(ui);

        return toDelete.containsKey(ui);
    }


    public void revertAdd(Identifier ui) {
        validate(ui);
        validate(this.toAdd.containsKey(ui), IngeniousExceptionsFactory.illegalArgument("You are attempting to revert addition of a not existing argument {}.", ui));

        this.toAdd.remove(ui);
    }

    public Item getAdd(Identifier ui) {
        validate(ui);
        validate(this.toAdd.containsKey(ui));

        return this.toAdd.get(ui);
    }

    public Item getDelete(Identifier ui) {
        validate(ui);
        validate(this.toDelete.containsKey(ui));

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
