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
 * Date: 07/04/2015
 */
package com.ingenious3.csp.element.item;

import com.ingenious3.annotations.Immutable;
import com.ingenious3.csp.element.Item;
import com.ingenious3.csp.element.ItemDecorator;
import com.ingenious3.identifier.Identifier;
import com.ingenious3.identifier.UI;
import com.ingenious3.validation.IValidate;

@Immutable
public abstract class AbstractItemOperation extends UI implements ItemDecorator, Identifier {
    private final Item item;
    private final ItemDecoratorType type;

    enum ItemDecoratorType {ADD, REVERT_ADD, DELETE, REVERT_DELETE}

    AbstractItemOperation(final Item item, ItemDecoratorType type){
        super(item);

        IValidate.validate(item.getClass().isAnnotationPresent(Immutable.class));

        this.item = item;
        this.type = type;
    }

    @Override
    public Item item(){
        return item;
    }

    @Override
    public String toString(){
        return new StringBuilder(type.name()).append(" : ").append(item.toString()).toString();
    }
}
