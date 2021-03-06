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

import com.ingenious3.annotations.Mutable;
import com.ingenious3.collections.IItems;
import com.ingenious3.identifier.Identifier;

import java.util.Set;

@Mutable
public interface Writer<T> extends IItems<T> {

    void add(T item);
    void revertAdd(Identifier ui);
    void markDeleted(Identifier ui);
    void revertMarkDeleted(Identifier ui);
    boolean markedDeleted(Identifier ui);
    T getAdd(Identifier ui);
    T getDelete(Identifier ui);

    Set<T> deleteItems();
    Set<T> addItems();
}
