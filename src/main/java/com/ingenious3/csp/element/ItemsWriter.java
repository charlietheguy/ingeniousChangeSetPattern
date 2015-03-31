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

import com.ingenious3.annotations.Dependency;
import com.ingenious3.annotations.Optimized;
import com.ingenious3.collections.IItems;
import com.ingenious3.csp.changeset.IItemsWriter;
import com.ingenious3.identifier.UI;
import com.ingenious3.util.IngeniousUtils;

import java.util.Map;
import java.util.Set;

public final class ItemsWriter implements IItemsWriter {

    private final boolean transactional;
    private final boolean reflected;
    private Map<UI, Item> items;

    private ItemsWriter(Set<Item> set, boolean transactional, boolean reflected) {
        this.transactional = transactional;
        this.reflected = reflected;
        this.items = IngeniousUtils.newConcurrentMap();
        set.forEach(item -> this.items.put(item, item));
    }

    public static ItemsWriter valueOf(Set<Item> set, boolean transactional, boolean reflected) {
        return new ItemsWriter(set, transactional, reflected);
    }

    public void add(Item item) {
        this.items.put(item, item);
    }

    public void change(Item original, Item change) {
        this.items.put(original, change);
    }

    public Item get(Item item) {
        return this.items.get(item);
    }

    public void revert(UI ui) {
        this.items.remove(ui);
    }

    public static ItemsWriter empty(){return ItemsWriter.valueOf(IngeniousUtils.newConcurrentSet(), true, true);}

    @Optimized(info = "Optimized not to count the set everytime in items() function.")
    @Dependency(values = {Map.class}, info = "Providing a fast itemSet to the map.")
    private Set<Item> itemsSet;

    @Override
    public Set<Item> items() {
        return itemsSet;
    }

    @Override
    public boolean containsValue(Item state) {
        return IItems.containsValue(state, items);
    }

    @Override
    public boolean containsKey(UI ui) {
        return IItems.containsKey(ui, items);
    }

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
