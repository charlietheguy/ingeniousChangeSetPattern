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

import com.ingenious3.builder.ImmutableItemsBuilder;
import com.ingenious3.collections.IItems;
import com.ingenious3.csp.element.item.ItemAddition;
import com.ingenious3.csp.element.item.ItemDeletion;
import com.ingenious3.csp.element.item.ItemRevertAddition;
import com.ingenious3.csp.element.item.ItemRevertDeletion;
import com.ingenious3.csp.persistence.IItemPersistence;
import com.ingenious3.csp.persistence.ItemPersistence;
import com.ingenious3.csp.persistence.ItemsReader;
import com.ingenious3.csp.reader.IItemsReader;
import com.ingenious3.exceptions.IngeniousExceptionsFactory;
import com.ingenious3.identifier.Identifier;
import com.ingenious3.identifier.impl.StringID;

import java.util.Set;

public interface FactoryImpl {
    public static Item createStringItem(String id){
        return Item.valueOf(FactoryImpl.uiString(id));
    }

    static IItemPersistence createItemPersistence(IItemsReader itemsReader, IObservableItemsWriter itemsWriter, boolean alwaysPersist) {
        return ItemPersistence.valueOf(itemsReader, itemsWriter, alwaysPersist);
    }

    static IItems<Item> createImmutableItems(Set<Item> items) {
        return ItemsReader.valueOf(new ImmutableItemsBuilder<Item>().addAll(items).build());
    }

    static Identifier itemAddition(Item item) {
        return new ItemAddition(item);
    }

    static Identifier itemRevertAddition(Item item) {
        return new ItemRevertAddition(item);
    }

    static ItemDeletion itemDeletion(Item item) {
        return new ItemDeletion(item);
    }

    static ItemRevertDeletion itemRevertDeletion(Item item) {
        return new ItemRevertDeletion(item);
    }

    public enum IDTypes {Long, String}

    static StringID uiString(Object id){
        return createUI(id, IDTypes.String);
    }

    @SuppressWarnings("unchecked")
    static <T> T createUI(Object id, IDTypes type) {
        switch (type){
            case String:
                return (T)StringID.valueOf((String)id);
            default:
                throw IngeniousExceptionsFactory.illegalArgument("Passed in object {} and type {}.", id, type);
        }
    }
}
