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

import com.ingenious3.csp.builder.ItemsWriterBuilder;
import com.ingenious3.csp.element.FactoryImpl;
import com.ingenious3.csp.element.Item;
import com.ingenious3.csp.element.ItemsWriter;
import com.ingenious3.csp.persistence.IItemPersistence;
import com.ingenious3.exceptions.IngeniousIllegalArgumentException;
import com.ingenious3.util.IngeniousUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class IItemsWriterImplTest {

    @Test
    public void testChangeSet(){
        ItemsWriter changeItemsWriter = new ItemsWriterBuilder().set(IngeniousUtils.newConcurrentSet()).transactional(true).reflected(true).build();

        Item str = FactoryImpl.createItem("Original");
        changeItemsWriter.add(str);

        Assert.assertEquals("Original", str.toString());

        changeItemsWriter.change(str, FactoryImpl.createItem("Modified."));

        Assert.assertEquals(FactoryImpl.createItem("Modified."), changeItemsWriter.get(str));
    }

    @Test
    public void testChangeSetNotReflected() {
        ItemsWriter changeItemsWriter = new ItemsWriterBuilder().set(IngeniousUtils.newConcurrentSet()).transactional(true).reflected(false).build();
        Item str = FactoryImpl.createItem("Original");
        changeItemsWriter.add(str);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testOtherMethod(){
        Set<Item> set = IngeniousUtils.newConcurrentSet();
        IItemPersistence source = FactoryImpl.createItemPersistence(new ItemsWriterBuilder().set(set).transactional(true).reflected(true).build());
        Item id = FactoryImpl.createItem("Whatever");

        ItemReader.read(source, id);
    }

    @Test
    public void testReadWriteRead(){
        Set<Item> set = IngeniousUtils.newConcurrentSet();
        IItemPersistence source = FactoryImpl.createItemPersistence(new ItemsWriterBuilder().set(set).transactional(true).reflected(true).build());
        Item id = FactoryImpl.createItem("Whatever");

        source.add(id);
        Item i = ItemReader.read(source, id);
        Assert.assertEquals(id, i);
    }
}
