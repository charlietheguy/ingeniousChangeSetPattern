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
 * Date: 06/04/2015
 */
package com.ingenious3.csp;

import com.google.common.collect.Sets;
import com.ingenious3.csp.element.FactoryImpl;
import com.ingenious3.csp.element.IObservableItemsWriter;
import com.ingenious3.csp.element.Item;
import com.ingenious3.csp.element.ObservableItemsWriter;
import com.ingenious3.csp.persistence.IPersistence;
import com.ingenious3.csp.persistence.ItemPersistenceObservable;
import com.ingenious3.csp.reader.IItemsReader;
import com.ingenious3.csp.writer.IItemsWriter;
import com.ingenious3.util.IngeniousUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static com.ingenious3.csp.persistence.IPersistence.PERSIST_STRATEGY;

public class ObservableItemsWriterTest {

    @Test
    public void testGeneration(){
        Set<Item> items = Sets.newHashSet();
        items.add(FactoryImpl.createStringItem("ID"));
        IItemsWriter writer = FactoryImpl.itemsWriter(items);
        IObservableItemsWriter oWriter = ObservableItemsWriter.valueOf(writer);
        IItemsReader reader = FactoryImpl.itemsReader(IngeniousUtils.newConcurrentSet());
        ItemPersistenceObservable.valueOf(reader, oWriter, PERSIST_STRATEGY.ALWAYS_PERSIST);

        oWriter.add(FactoryImpl.createStringItem("ID2"));
        oWriter.revertAdd(FactoryImpl.createStringItem("ID2"));

        oWriter.add(FactoryImpl.createStringItem("ID"));
        oWriter.markDeleted(FactoryImpl.createStringItem("ID"));
        oWriter.revertMarkDeleted(FactoryImpl.createStringItem("ID"));
    }

    @Test
    public void testItemsToPersistOnDemand(){
        Set<Item> items = Sets.newHashSet();
        items.add(FactoryImpl.createStringItem("ID"));
        IItemsWriter writer = FactoryImpl.itemsWriter(items);
        IObservableItemsWriter oWriter = ObservableItemsWriter.valueOf(writer);
        IItemsReader reader = FactoryImpl.itemsReader(IngeniousUtils.newConcurrentSet());
        IPersistence<Item> persistence = ItemPersistenceObservable.valueOf(reader, oWriter, PERSIST_STRATEGY.PERSIST_ON_DEMAND);

        oWriter.add(FactoryImpl.createStringItem("ID2"));
        Assert.assertNotNull("Items to persist should have one entry", persistence.itemsToPersist().items());
        Assert.assertEquals("Items to persist should have one entry", 1, persistence.itemsToPersist().items().size());

        persistence = persistence.persist();

        Assert.assertNotNull("Items to persist should have NO entry", persistence.itemsToPersist().items());
        Assert.assertEquals("Items to persist should have NO entry", 0, persistence.itemsToPersist().items().size());
    }

    @Test
    public void testItemsToPersistAlways(){
        Set<Item> items = Sets.newHashSet();
        items.add(FactoryImpl.createStringItem("ID"));
        IItemsWriter writer = FactoryImpl.itemsWriter(items);
        IObservableItemsWriter oWriter = ObservableItemsWriter.valueOf(writer);
        IItemsReader reader = FactoryImpl.itemsReader(IngeniousUtils.newConcurrentSet());
        IPersistence<Item> persistence = ItemPersistenceObservable.valueOf(reader, oWriter, PERSIST_STRATEGY.ALWAYS_PERSIST);

        oWriter.add(FactoryImpl.createStringItem("ID2"));
        Assert.assertNotNull("Items to persist should have NO entry", persistence.itemsToPersist().items());
        Assert.assertEquals("Items to persist should have NO entry", 0, persistence.itemsToPersist().items().size());

        persistence = persistence.persist();

        Assert.assertNotNull("Items to persist should have NO entry", persistence.itemsToPersist().items());
        Assert.assertEquals("Items to persist should have NO entry", 0, persistence.itemsToPersist().items().size());
    }
}
