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

import com.ingenious3.csp.persistence.IItemPersistence;
import com.ingenious3.csp.persistence.ItemPersistence;

public interface FactoryImpl {
    public static Item createItem(String id){
        return Item.valueOf(id);
    }

    static IItemPersistence createItemPersistence(ItemsWriter itemsWriter) {
        return ItemPersistence.valueOf(itemsWriter);
    }
}
