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
 * Date: 06/04/2015
 */
package com.ingenious3.csp.element;

import com.ingenious3.collections.IItems;
import com.ingenious3.csp.writer.IItemsWriter;
import com.ingenious3.identifier.UI;
import com.ingenious3.validation.IValidate;

import java.util.Observable;
import java.util.Set;

public class ObservableItemsWriter extends Observable implements IObservableItemsWriter {

    private IItemsWriter itemsWriter;

    public ObservableItemsWriter(IItemsWriter itemsWriter) {
        this.itemsWriter = itemsWriter;
    }

    public static ObservableItemsWriter valueOf(IItemsWriter writer){
        return new ObservableItemsWriter(writer);
    }

    @Override
    public void add(Item item) {
        IValidate.validate(item);

        itemsWriter.add(item);
        this.setChanged();
        notifyObservers(FactoryImpl.itemAddition(item));
    }

    @Override
    public void revertAdd(UI ui) {
        IValidate.validate(ui);
        IValidate.validate(getAdd(ui));

        this.setChanged();
        notifyObservers(FactoryImpl.itemRevertAddition(getAdd(ui)));
        itemsWriter.revertAdd(ui);
    }


    @Override
    public void change(Item original, Item change) {
        itemsWriter.change(original, change);
    }

    @Override
    public void markDeleted(UI ui) {
        IValidate.validate(ui);
        IValidate.validate(get(ui));

        this.setChanged();
        notifyObservers(FactoryImpl.itemDeletion(get(ui)));
        itemsWriter.markDeleted(ui);
    }

    @Override
    public void revertMarkDeleted(UI ui) {
        IValidate.validate(ui);
        IValidate.validate(getDelete(ui));

        this.setChanged();
        notifyObservers(FactoryImpl.itemRevertDeletion(getDelete(ui)));

        itemsWriter.revertMarkDeleted(ui);
    }

    @Override
    public boolean markedDeleted(UI ui) {
        return itemsWriter.markedDeleted(ui);
    }

    @Override
    public IItems<Item> itemsMarkedDeleted() {
        return itemsWriter.itemsMarkedDeleted();
    }

    @Override
    public boolean containsKey(UI ui) {
        return itemsWriter.containsKey(ui);
    }

    @Override
    public boolean containsValue(Item item) {
        return itemsWriter.containsValue(item);
    }

    @Override
    public Item get(UI ui) {
        return itemsWriter.get(ui);
    }

    @Override
    public Item getAdd(UI ui) {
        return itemsWriter.getAdd(ui);
    }

    @Override
    public Item getDelete(UI ui) {
        return itemsWriter.getDelete(ui);
    }

    @Override
    public Set<Item> items() {
        return itemsWriter.items();
    }

    @Override
    public boolean isEmpty() {
        return itemsWriter.isEmpty();
    }
}
