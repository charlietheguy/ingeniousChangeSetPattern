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

import com.ingenious3.collections.IItems;
import com.ingenious3.csp.element.Item;
import com.ingenious3.csp.element.ItemsWriter;
import com.ingenious3.validation.IValidate;

import java.util.Set;

public class ItemsWriterBuilder {

    private boolean transactional;
    private boolean reflected;
    private Set<Item> set;

    public ItemsWriterBuilder transactional(boolean transactional) {
        this.transactional = transactional;
        return this;
    }

    public ItemsWriterBuilder reflected(boolean reflected) {
        this.reflected = reflected;
        return this;
    }

    public ItemsWriterBuilder set(Set<Item> set){
        IValidate.validate(set);
        this.set = IItems.unmodifiableSet(set);
        return this;
    }

    public ItemsWriter build() {
        check();
        return ItemsWriter.valueOf(set, transactional, reflected);
    }

    public ItemsWriterBuilder check() {
        IValidate.validate(set);
        return this;
    }
}
