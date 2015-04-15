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
 * Date: 28/03/2015
 */
package com.ingenious3.csp.writer;

import com.ingenious3.collections.IItems;
import com.ingenious3.identifier.UI;

import java.util.Set;

public interface Writer<T> extends IItems<T> {

    void add(T item);
    void revertAdd(UI ui);
    void change(T original, T change);
    void markDeleted(UI ui);
    void revertMarkDeleted(UI ui);
    boolean markedDeleted(UI ui);
    T getAdd(UI ui);
    T getDelete(UI ui);

    Set<T> deleteItems();
    Set<T> addItems();
}
