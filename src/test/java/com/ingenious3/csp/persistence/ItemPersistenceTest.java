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
 * Date: 20/04/2015
 */
package com.ingenious3.csp.persistence;

import com.ingenious3.csp.element.FactoryImpl;
import com.ingenious3.csp.element.Item;
import com.ingenious3.exceptions.IngeniousIllegalArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashSet;

public class ItemPersistenceTest {

    private final Item ITEMA = FactoryImpl.createStringItem("A");

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testInvalidInputReader(){
        FactoryImpl.createItemPersistence(null, ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.ALWAYS_PERSIST);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testInvalidInputWriter(){
        FactoryImpl.createItemPersistence(ItemsReader.empty(), null, IPersistence.PERSIST_STRATEGY.ALWAYS_PERSIST);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testInvalidInputStrategy(){
        FactoryImpl.createItemPersistence(ItemsReader.empty(), ItemsWriter.empty(), null);
    }

    @Test
    public void testValidPersistence(){
        IItemPersistence persistence = FactoryImpl.createItemPersistence(ItemsReader.empty(), ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.ALWAYS_PERSIST);
        Assert.assertEquals("No items to persist", 0, persistence.itemsToPersist().items().size());
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testGetNull(){
        IItemPersistence persistence = FactoryImpl.createItemPersistence(ItemsReader.empty(), ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.ALWAYS_PERSIST);
        persistence.get(null);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testGetNonExistingItem(){
        IItemPersistence persistence = FactoryImpl.createItemPersistence(ItemsReader.empty(), ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.ALWAYS_PERSIST);
        persistence.get(ITEMA);
    }

    @Test
    public void testGetExistingItem(){
        IItemPersistence persistence = FactoryImpl.createItemPersistence(ItemsReader.valueOf(new LinkedHashSet<Item>(){{add(ITEMA);}}), ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.ALWAYS_PERSIST);
        Assert.assertEquals("Item is Item A", ITEMA, persistence.get(ITEMA));
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testAddNull(){
        IItemPersistence persistence = FactoryImpl.createItemPersistence(ItemsReader.empty(), ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.ALWAYS_PERSIST);
        persistence.add(null);
    }

    @Test
    public void testAddITEMA(){
        IItemPersistence persistence = FactoryImpl.createItemPersistence(ItemsReader.empty(), ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.ALWAYS_PERSIST);
        Assert.assertEquals("Persistence added", ITEMA, persistence.add(ITEMA).get(ITEMA));
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testAddExistingItem(){
        IItemPersistence persistence = FactoryImpl.createItemPersistence(ItemsReader.valueOf(new LinkedHashSet<Item>(){{add(ITEMA);}}), ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.ALWAYS_PERSIST);
        persistence.add(ITEMA);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testAddExistingPersistItem(){
        IItemPersistence persistence = FactoryImpl.createItemPersistence(ItemsReader.valueOf(new LinkedHashSet<Item>(){{add(ITEMA);}}), ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.PERSIST_ON_DEMAND);
        persistence.add(ITEMA);
    }

    @Test
    public void testAlwaysPersistWriteAddition(){
        IItemPersistence persistence = FactoryImpl.createItemPersistence(ItemsReader.empty(), ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.ALWAYS_PERSIST);
        Assert.assertEquals("Persistence added", ITEMA, persistence.add(ITEMA).get(ITEMA));
    }

    @Test
    public void testAddITEMAOnDemand(){
        IItemPersistence persistence = FactoryImpl.createItemPersistence(ItemsReader.empty(), ItemsWriter.empty(), IPersistence.PERSIST_STRATEGY.PERSIST_ON_DEMAND);
        try {
            Assert.assertEquals("Persistence added", ITEMA, persistence.add(ITEMA).get(ITEMA));
            Assert.fail();
        }catch (IngeniousIllegalArgumentException e){
            Assert.assertEquals("Persistence added", ITEMA, (persistence.persist()).get(ITEMA));
        }
    }


}
