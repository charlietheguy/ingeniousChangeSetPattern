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
 * Date: 15/04/2015
 */
package com.ingenious3.csp.element;

import com.ingenious3.element.ItemDecorator;
import com.ingenious3.exceptions.IngeniousIllegalArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class ItemOperationTest {
    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testItemAdditionNull(){
        FactoryImpl.itemAddition(null);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testItemRevertAdditionNull(){
        FactoryImpl.itemRevertAddition(null);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testItemRevertDeletionNull(){
        FactoryImpl.itemRevertDeletion(null);
    }

    @Test(expected = IngeniousIllegalArgumentException.class)
    public void testItemDeletionNull(){
        FactoryImpl.itemDeletion(null);
    }

    @Test
    public void testItemAddition(){
        ItemDecorator item = FactoryImpl.itemAddition(FactoryImpl.createStringItem("a"));
        Assert.assertEquals("ADD : a", item.toString());
    }

    @Test
    public void testItemRevertAddition(){
        ItemDecorator item = FactoryImpl.itemRevertAddition(FactoryImpl.createStringItem("a"));
        Assert.assertEquals("REVERT_ADD : a", item.toString());
    }

    @Test
    public void testItemRevertDeletion(){
        ItemDecorator item = FactoryImpl.itemRevertDeletion(FactoryImpl.createStringItem("a"));
        Assert.assertEquals("REVERT_DELETE : a", item.toString());
    }

    @Test
    public void testItemDeletion(){
        ItemDecorator item = FactoryImpl.itemDeletion(FactoryImpl.createStringItem("a"));
        Assert.assertEquals("DELETE : a", item.toString());
    }
}
