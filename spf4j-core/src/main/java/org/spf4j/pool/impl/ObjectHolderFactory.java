
/*
 * Copyright (c) 2001, Zoltan Farkas All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.spf4j.pool.impl;

import org.spf4j.pool.ObjectCreationException;
import org.spf4j.pool.ObjectDisposeException;
import org.spf4j.pool.ObjectPool;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author zoly
 */
public final class ObjectHolderFactory<T> implements ObjectPool.Factory<ObjectHolder<T>> {

    private final Queue<ObjectHolder<T>> objects;
    private final ObjectPool.Factory<T> factory;

    public ObjectHolderFactory(final int precreateNumber, final ObjectPool.Factory<T> factory)
            throws ObjectCreationException {
        objects = new LinkedList<ObjectHolder<T>>();
        this.factory = factory;
        for (int i = 0; i < precreateNumber; i++) {
            objects.add(new ObjectHolder<T>(factory, false));
        }
    }

    public ObjectHolderFactory(final ObjectPool.Factory<T> factory) {
        objects = new LinkedList<ObjectHolder<T>>();
        this.factory = factory;
    }

    @Override
    public ObjectHolder<T> create() throws ObjectCreationException {
        if (objects.isEmpty()) {
            return new ObjectHolder<T>(factory);
        } else {
            return objects.remove();
        }
    }

    @Override
    public void dispose(final ObjectHolder<T> object) throws ObjectDisposeException {
        if (!object.disposeIfNotBorrowed()) {
            throw new RuntimeException("Object from holder is borrowed " + object);
        }
    }

    @Override
    public Exception validate(final ObjectHolder<T> object, final Exception e) {
        return null;
    }
}
