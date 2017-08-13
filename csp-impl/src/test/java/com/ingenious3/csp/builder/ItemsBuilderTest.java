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
 * Date: 13/04/2015
 */
package com.ingenious3.csp.builder;

import com.google.common.collect.Sets;
import com.ingenious3.csp.element.FactoryImpl;
import com.ingenious3.element.Item;
import com.ingenious3.csp.persistence.ItemsReader;
import com.ingenious3.exceptions.IngeniousIllegalArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ItemsBuilderTest {
    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testNullBuilders(){
        new ItemsBuilder(null);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testNullArgumentBuilders(){
        Set<Item> set = Sets.newHashSet();
        set.add(null);
        new ItemsBuilder(set);
    }

    @Test
    public void testBuildReader(){
        Set<Item> set = Sets.newHashSet();
        final String id = "Item";
        set.add(FactoryImpl.createStringItem(id));
        ItemsReader reader = new ItemsBuilder(set).buildReader();

        Assert.assertNotNull("Reader is not null", reader);
        Item newItem = FactoryImpl.createStringItem(id);
        Item readerItem = reader.get(FactoryImpl.createStringItem(id));
        Assert.assertEquals(newItem, readerItem);
    }
}
