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

import com.ingenious3.collections.AbstractIItems;
import com.ingenious3.csp.element.Decorated;
import com.ingenious3.csp.element.Item;
import com.ingenious3.util.IngeniousUtils;

import java.util.Set;

public final class DecoratedItems extends AbstractIItems<IDecorator<Item>> implements Decorated<Item> {

    private DecoratedItems(Set<IDecorator<Item>> set) {
        super(set);
    }

    public static DecoratedItems valueOf(Set<IDecorator<Item>> set) {
        return new DecoratedItems(set);
    }

    public static DecoratedItems empty(){return DecoratedItems.valueOf(IngeniousUtils.newConcurrentSet());}
}
