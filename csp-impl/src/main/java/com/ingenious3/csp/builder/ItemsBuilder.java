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

package com.ingenious3.csp.builder;

import com.ingenious3.annotations.Mutable;
import com.ingenious3.collections.IItems;
import com.ingenious3.csp.element.FactoryImpl;
import com.ingenious3.csp.element.IObservableItemsWriter;
import com.ingenious3.element.Item;
import com.ingenious3.csp.persistence.ItemsReader;
import com.ingenious3.csp.persistence.ItemsWriter;

import java.util.Set;

import static com.ingenious3.validation.IValidate.validate;

@Mutable
public class ItemsBuilder {

    private Set<Item> set;

    public ItemsBuilder(Set<Item> set) {
        validate(set);
        this.set = IItems.unmodifiableSet(set);
    }

    public ItemsWriter buildWriter() {
        return FactoryImpl.itemsWriter(set);
    }

    public IObservableItemsWriter buildObservableWriter() {
        return FactoryImpl.observableItemsWriter(buildWriter());
    }

    public ItemsReader buildReader() {
        return FactoryImpl.itemsReader(set);
    }

}
