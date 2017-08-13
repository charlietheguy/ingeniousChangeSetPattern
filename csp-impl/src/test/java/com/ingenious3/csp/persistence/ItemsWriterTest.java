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
import com.ingenious3.element.Item;
import com.ingenious3.exceptions.IngeniousIllegalArgumentException;
import com.ingenious3.exceptions.IngeniousRuntimeException;
import com.ingenious3.util.IngeniousUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashSet;

public class ItemsWriterTest {

    private final Item ITEMA = FactoryImpl.createStringItem("A");

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testAddNull(){
        ItemsWriter.valueOf(null);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testAddSetWithNull(){
        ItemsWriter.valueOf(new LinkedHashSet<Item>(){{add(null);}});
    }

    @Test
    public void testEmpty(){
        ItemsWriter writer = ItemsWriter.valueOf(IngeniousUtils.newConcurrentSet());

        Assert.assertEquals("No items to be added.", 0, writer.addItems().size());
        Assert.assertEquals("No items to be removed.", 0, writer.deleteItems().size());
    }

    @Test
    public void testAddA(){
        ItemsWriter writer = ItemsWriter.valueOf(IngeniousUtils.newConcurrentSet());
        writer.add(ITEMA);

        Assert.assertFalse("Not marked as deleted.", writer.markedDeleted(ITEMA));
        Assert.assertEquals("Item A successfully found.", ITEMA, writer.getAdd(ITEMA));

        Assert.assertEquals("1 item to be added.", 1, writer.addItems().size());
        Assert.assertEquals("No items to be removed.", 0, writer.deleteItems().size());
        Assert.assertEquals("Item is A", ITEMA, writer.addItems().iterator().next());
    }

    @Test
    public void testAddRevert(){
        ItemsWriter writer = ItemsWriter.valueOf(IngeniousUtils.newConcurrentSet());
        writer.add(ITEMA);
        writer.revertAdd(ITEMA);

        Assert.assertEquals("No items to be added.", 0, writer.addItems().size());
        Assert.assertEquals("No items to be removed.", 0, writer.deleteItems().size());
    }

    @Test
    public void testAddRevertAdd(){
        ItemsWriter writer = ItemsWriter.valueOf(IngeniousUtils.newConcurrentSet());
        writer.add(ITEMA);
        writer.revertAdd(ITEMA);
        writer.add(ITEMA);

        Assert.assertEquals("1 item to be added.", 1, writer.addItems().size());
        Assert.assertEquals("No items to be removed.", 0, writer.deleteItems().size());
        Assert.assertEquals("Item is A", ITEMA, writer.addItems().iterator().next());
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testAddAlreadyAdded(){
        ItemsWriter writer = ItemsWriter.valueOf(IngeniousUtils.newConcurrentSet());
        writer.add(ITEMA);
        writer.add(ITEMA);
    }

    @Test
    public void testRemoveA(){
        ItemsWriter writer = ItemsWriter.valueOf(new LinkedHashSet<Item>(){{add(ITEMA);}});
        writer.markDeleted(ITEMA);

        Assert.assertEquals("Item A successfully found.", ITEMA, writer.getDelete(ITEMA));

        Assert.assertEquals("1 item to be removed.", 1, writer.deleteItems().size());
        Assert.assertEquals("No items to be added.", 0, writer.addItems().size());
        Assert.assertEquals("Item is A", ITEMA, writer.deleteItems().iterator().next());
    }

    @Test
    public void testDeleteRevert(){
        ItemsWriter writer = ItemsWriter.valueOf(new LinkedHashSet<Item>(){{add(ITEMA);}});
        writer.markDeleted(ITEMA);
        writer.revertMarkDeleted(ITEMA);

        Assert.assertEquals("No items to be added.", 0, writer.addItems().size());
        Assert.assertEquals("No items to be removed.", 0, writer.deleteItems().size());
    }

    @Test
    public void testDeleteRevertDelete(){
        ItemsWriter writer = ItemsWriter.valueOf(new LinkedHashSet<Item>(){{add(ITEMA);}});
        writer.markDeleted(ITEMA);

        Assert.assertTrue("Marked as deleted", writer.markedDeleted(ITEMA));

        writer.revertMarkDeleted(ITEMA);
        writer.markDeleted(ITEMA);

        Assert.assertEquals("1 item to be deleted.", 1, writer.deleteItems().size());
        Assert.assertEquals("No items to be added.", 0, writer.addItems().size());
        Assert.assertEquals("Item is A", ITEMA, writer.deleteItems().iterator().next());
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testDeleteAlreadyAdded(){
        ItemsWriter writer = ItemsWriter.valueOf(new LinkedHashSet<Item>(){{add(ITEMA);}});
        writer.markDeleted(ITEMA);
        writer.markDeleted(ITEMA);
    }

    @Test(expected = IngeniousRuntimeException.class)
    public void testAddNegativeTest(){
        ItemsWriter writer = ItemsWriter.valueOf(IngeniousUtils.newConcurrentSet());
        writer.getAdd(ITEMA);
    }

    @Test(expected = IngeniousRuntimeException.class)
    public void testDeleteNegativeTest(){
        ItemsWriter writer = ItemsWriter.valueOf(IngeniousUtils.newConcurrentSet());
        writer.getDelete(ITEMA);
    }



}
