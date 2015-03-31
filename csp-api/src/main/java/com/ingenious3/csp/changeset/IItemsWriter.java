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
package com.ingenious3.csp.changeset;

import com.ingenious3.collections.IItems;
import com.ingenious3.csp.element.Item;
import com.ingenious3.identifier.UI;

public interface IItemsWriter extends IItems<Item> {
    void add(Item item);
    void revert(UI ui);
    void change(Item original, Item change);
}
