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
package com.ingenious3.csp.element;

import com.ingenious3.collections.IItems;
import com.ingenious3.csp.writer.IItemsWriter;
import com.ingenious3.exceptions.IngeniousExceptionsFactory;
import com.ingenious3.identifier.UI;
import com.ingenious3.util.IngeniousUtils;
import com.ingenious3.validation.IValidate;

import java.util.Map;
import java.util.Set;

public final class ItemsWriter implements IItemsWriter {

    private Map<UI, Item> items;
    private Map<UI, Item> toDelete;

    private ItemsWriter(Set<Item> set) {
        this.items = IngeniousUtils.newConcurrentMap();
        set.forEach(item -> this.items.put(item, item));
        this.toDelete = IngeniousUtils.newConcurrentMap();
    }

    public static ItemsWriter valueOf(Set<Item> set) {
        return new ItemsWriter(set);
    }

    public void add(Item item) {
        IValidate.validate(item);

        this.items.put(item, item);
    }

    public void change(Item original, Item change) {
        IValidate.validate(original);
        IValidate.validate(change);

        revertAdd(original);
        add(change);
    }

    @Override
    public void markDeleted(UI ui) {
        IValidate.validate(ui);

        toDelete.put(ui, get(ui));
    }

    @Override
    public void revertMarkDeleted(UI ui) {
        IValidate.validate(ui);

        if(IItems.containsValue(get(ui), toDelete)){
            toDelete.remove(ui);
            return;
        }

        throw IngeniousExceptionsFactory.illegalArgument("Key ui {} does not exist in the map with items marked as deleted.", ui);
    }

    @Override
    public boolean markedDeleted(UI ui) {
        IValidate.validate(ui);

        return toDelete.containsKey(ui);
    }

    @Override
    public IItems<Item> itemsMarkedDeleted() {
        Set<Item> set = IngeniousUtils.newConcurrentSet();
        set.addAll(toDelete.values());
        return Factory.createImmutableItems(set);
    }

    public void revertAdd(UI original) {
        IValidate.validate(original);

        this.items.remove(original);
    }


    public static ItemsWriter empty(){return ItemsWriter.valueOf(IngeniousUtils.newConcurrentSet());}

    @Override
    public Set<Item> items() {
        Set<Item> set = IngeniousUtils.newConcurrentSet();
        set.addAll(this.items.values());
        return set;
    }

    @Override
    public boolean containsValue(Item item) {
        return IItems.containsValue(item, items);
    }

    @Override
    public boolean containsKey(UI ui) { return IItems.containsKey(ui, items); }

    @Override
    public Item get(UI id) {
        return IItems.defensiveCopyOf(id, items);
    }

    @Override
    public String toString(){ return IItems.toString(items); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemsWriter states = (ItemsWriter) o;

        if (!items.equals(states.items)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    @Override
    public boolean isEmpty() {return IItems.empty(items.keySet());}
}
