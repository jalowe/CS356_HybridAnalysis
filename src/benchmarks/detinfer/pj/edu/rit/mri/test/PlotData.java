//******************************************************************************
//
// File:    PlotData.java
// Package: benchmarks.detinfer.pj.edu.ritmri.test
// Unit:    Class benchmarks.detinfer.pj.edu.ritmri.test.PlotData
//
// This Java source file is copyright (C) 2008 by Alan Kaminsky. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ark@cs.rit.edu.
//
// This Java source file is part of the Parallel Java Library ("PJ"). PJ is free
// software; you can redistribute it and/or modify it under the terms of the GNU
// General Public License as published by the Free Software Foundation; either
// version 3 of the License, or (at your option) any later version.
//
// PJ is distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
// A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//
// A copy of the GNU General Public License is provided in the file gpl.txt. You
// may also obtain a copy of the GNU General Public License on the World Wide
// Web at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************

package benchmarks.detinfer.pj.edu.ritmri.test;

import benchmarks.detinfer.pj.edu.ritnumeric.ListXYSeries;

import benchmarks.detinfer.pj.edu.ritnumeric.plot.Dots;
import benchmarks.detinfer.pj.edu.ritnumeric.plot.Plot;
import benchmarks.detinfer.pj.edu.ritnumeric.plot.Strokes;

import java.awt.Color;

import java.io.File;

import java.util.Scanner;

/**
 * Class PlotData is a main program that displays a plot of a spin signal data
 * set. The data set is generated by the {@linkplain GenData} program. A
 * noise-free spin signal derived from the given spin densities and spin
 * relaxation rates is superimposed on the plot.
 * <P>
 * Usage: java benchmarks.detinfer.pj.edu.ritmri.test.PlotData <I>datafile</I> [
 * <I>&rho;</I><SUB>1</SUB> <I>x</I><SUB>1</SUB> . . . ]
 * <BR><I>datafile</I> = File containing <I>t</I> and <I>S</I>(<I>t</I>) values
 * <BR><I>rho</I><SUB>1</SUB> = Spin density
 * <BR><I>x</I><SUB>1</SUB> = Spin relaxation rate
 *
 * @author  Alan Kaminsky
 * @version 10-Jun-2008
 */
public class PlotData
	{

// Prevent construction.

	private PlotData()
		{
		}

// Main program.

	/**
	 * Main program.
	 */
	public static void main
		(String[] args)
		throws Exception
		{
		// Parse command line arguments.
		if (args.length < 1 || (args.length % 2) != 1) usage();
		File datafile = new File (args[0]);
		int ns = (args.length - 1)/2;
		double[] rho = new double [ns];
		double[] x = new double [ns];
		for (int i = 0; i < ns; ++ i)
			{
			rho[i] = Double.parseDouble (args[2*i+1]);
			x[i] = Double.parseDouble (args[2*i+2]);
			}

		// Get data X-Y series.
		ListXYSeries dataseries = new ListXYSeries();
		dataseries.add (new Scanner (datafile));

		// Create plot with data X-Y series.
		Plot plot = new Plot();
		plot.xAxisLength (1200)
			.xAxisMajorDivisions (20)
			.yAxisLength (600)
			.yAxisMajorDivisions (10)
			.seriesDots (Dots.circle (Color.BLACK, null, null, 5))
			.seriesColor (Color.BLACK)
			.seriesStroke (Strokes.solid (2))
			.xySeries (dataseries);

		// If specified, generate spin signal X-Y series and add to plot.
		if (ns > 0)
			{
			double chisqr = 0.0;
			ListXYSeries signalseries = new ListXYSeries();
			for (int i = 0; i < dataseries.length(); ++ i)
				{
				double t = dataseries.x(i);
				double s = 0.0;
				for (int j = 0; j < ns; ++ j)
					{
					s += rho[j]*(1.0 - 2.0*Math.exp(-x[j]*t));
					}
				double d = s - dataseries.y(i);
				chisqr += d*d;
				signalseries.add (t, s);
				}
			System.out.println ("chi^2 = "+chisqr);
			plot.seriesDots (null)
				.seriesColor (Color.RED)
				.seriesStroke (Strokes.solid (1))
				.xySeries (signalseries);
			}

		// Display plot.
		plot.getFrame().setVisible (true);
		}

// Hidden operations.

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
		{
		System.err.println ("Usage: java benchmarks.detinfer.pj.edu.ritmri.test.PlotData <datafile> [<rho1> <x1> ...]");
		System.err.println ("<datafile> = File containing t and S(t) values");
		System.err.println ("<rho1> = Spin density");
		System.err.println ("<x1> = Spin relaxation rate");
		System.exit (1);
		}

	}
