/*-
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2018 - 2021 Apromore Pty Ltd.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
/*
 * $Id: JGraphCompoundLayout.java,v 1.1 2009/09/25 15:14:15 david Exp $
 * Copyright (c) 2001-2005, Gaudenz Alder
 * 
 * All rights reserved. 
 * 
 * This file is licensed under the JGraph software license, a copy of which
 * will have been provided to you in the file LICENSE at the root of your
 * installation directory. If you are unable to locate this file please
 * contact JGraph sales for another copy.
 */
package org.apromore.jgraph.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This allows to recursively compose any number of abstract layouts into a
 * compound abstract layout. Note that this is more flexible than a decorator
 * pattern, because you can use different class hierarchies to implement the
 * input (facade) and the layout algorithms, while adhering to the rule that
 * each layout algorithm uses the output of the last layout as its input.
 */
public class JGraphCompoundLayout implements JGraphLayout {

	/**
	 * Holds all layouts.
	 */
	protected List layouts = new ArrayList();

	/**
	 * Default constructor
	 */
	public JGraphCompoundLayout() {
	}

	/**
	 * Constructs a compound layout consisting of the specified first- and
	 * secondStep.
	 */
	public JGraphCompoundLayout(JGraphLayout[] layouts) {
		this.layouts.addAll(Arrays.asList(layouts));
	}

	/**
	 * Runs all layouts in the order they were inserted. Note: The facade
	 * encapsulates the input and output of the algorithm, thus ensuring that
	 * each algorithm runs on the outcome of preceding algorithm.
	 */
	public void run(JGraphFacade graph) {
		Iterator it = layouts.iterator();
		while (it.hasNext()) {
			Object layout = it.next();
			if (layout instanceof JGraphLayout)
				((JGraphLayout) layout).run(graph);
		}
	}

	/**
	 * Adds a layout to {@link #layouts}.
	 * 
	 * @param layout
	 *            The layout to add.
	 */
	public void add(JGraphLayout layout) {
		layouts.add(layout);
	}

	/**
	 * Removes a layout from {@link #layouts}
	 * 
	 * @param layout
	 *            The layout to remove.
	 */
	public void remove(JGraphLayout layout) {
		layouts.remove(layout);
	}

	/**
	 * Returns the list of layouts.
	 * 
	 * @return Returns the list of layouts.
	 */
	public List getLayouts() {
		return layouts;
	}

}
