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
package com.ingenious3.csp;

import com.ingenious3.csp.builder.ItemsBuilder;
import com.ingenious3.csp.element.FactoryImpl;
import com.ingenious3.csp.element.Item;
import com.ingenious3.csp.persistence.IItemPersistence;
import com.ingenious3.csp.persistence.ItemsWriter;
import com.ingenious3.exceptions.IngeniousIllegalArgumentException;
import com.ingenious3.util.IngeniousUtils;
import org.junit.Test;

import java.util.Set;

public class IItemsWriterImplTest {

//    @Test
//    public void testChangeSet(){
//        ItemsWriter changeItemsWriter = new ItemsBuilder().set(IngeniousUtils.newConcurrentSet()).buildWriter();
//
//        Item str = FactoryImpl.createStringItem("Original");
//        changeItemsWriter.add(str);
//
//        Assert.assertEquals("Original", str.toString());
//
//        changeItemsWriter.change(str, FactoryImpl.createStringItem("Modified."));
//
//        Set<Item> items = changeItemsWriter.items();
//        Assert.assertEquals("Original item was changed.",1, items.size());
//        Assert.assertEquals(FactoryImpl.createStringItem("Modified."),items.iterator().next());
//    }

    @Test
    public void testChangeSetNotReflected() {
        ItemsWriter changeItemsWriter = new ItemsBuilder(IngeniousUtils.newConcurrentSet()).buildWriter();
        Item str = FactoryImpl.createStringItem("Original");
        changeItemsWriter.add(str);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testOtherMethod(){
        Set<Item> set = IngeniousUtils.newConcurrentSet();
        IItemPersistence source = FactoryImpl.createItemPersistence(new ItemsBuilder(set).buildReader(), new ItemsBuilder(set).buildObservableWriter(), false);
        Item id = FactoryImpl.createStringItem("Whatever");

        source.get(id);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testReadWriteReadNonPersistent(){
        Set<Item> set = IngeniousUtils.newConcurrentSet();
        IItemPersistence source = FactoryImpl.createItemPersistence(new ItemsBuilder(set).buildReader(), new ItemsBuilder(set).buildObservableWriter(), false);
        Item id = FactoryImpl.createStringItem("Whatever");

        source.add(id);
        Item i = source.get(id);
    }

//    @Test
//    public void testReadWriteReadPersistent(){
//        Set<Item> set = IngeniousUtils.newConcurrentSet();
//        IItemPersistence source = FactoryImpl.createItemPersistence(new ItemsBuilder().set(set).buildReader(), new ItemsBuilder().set(set).buildObservableWriter(), true);
//        Item id = FactoryImpl.createStringItem("Whatever");
//
//        source.add(id);
//        Item i = source.get(id);
//        Assert.assertEquals(id, i);
//    }
}
