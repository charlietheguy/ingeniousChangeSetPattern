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

import com.ingenious3.csp.changeset.IItemsWriter;
import com.ingenious3.csp.element.Item;
import com.ingenious3.identifier.UI;
import com.ingenious3.validation.IValidate;

public final class ItemPersistence implements IItemPersistence {

    private IItemsWriter set;

    private ItemPersistence(IItemsWriter itemsWriter){
        this.set = itemsWriter;
    }

    public static ItemPersistence valueOf(IItemsWriter itemsWriter) {
        IValidate.validate(itemsWriter);

        return new ItemPersistence(itemsWriter);
    }

    @Override
    public Item get(UI id) {
        return this.set.get(id);
    }

    @Override
    public void add(Item item) {
        this.set.add(item);
    }
}
