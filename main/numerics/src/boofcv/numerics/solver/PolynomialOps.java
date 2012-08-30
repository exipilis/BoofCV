/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.numerics.solver;

import boofcv.numerics.solver.impl.FindRealRootsSturm;
import boofcv.numerics.solver.impl.RootFinderCompanion;
import boofcv.numerics.solver.impl.SturmSequence;
import boofcv.numerics.solver.impl.WrapRealRootsSturm;

/**
 * @author Peter Abeles
 */
public class PolynomialOps {

	/**
	 * Given the coefficients compute the vertex (minimum/maximum) of the quadratic.
	 *
	 * y = a*x<sup>2</sp> + b*x + c
	 *
	 * @param a quadratic coefficient.
	 * @param b quadratic coefficient.
	 * @return The quadratic's vertex.
	 */
	public static double quadraticVertex( double a, double b ) {
		return -b/(2.0*a);
	}

	public static void derivative( Polynomial poly , Polynomial deriv ) {
		deriv.size = poly.size - 1;

		for( int i = 1; i < poly.size; i++ ) {
			deriv.c[i-1] = poly.c[i]*i;
		}
	}

	// TODO try using a linear search alg here
	public static double refineRoot( Polynomial poly, double root , int maxIterations ) {

//		for( int i = 0; i < maxIterations; i++ ) {
//
//			double v = poly.c[poly.size-1];
//			double d = v*(poly.size-1);
//
//			for( int j = poly.size-1; j > 0; j-- ) {
//				v = poly.c[j] + v*root;
//				d = poly.c[j]*j + d*root;
//			}
//			v = poly.c[0] + v*root;
//
//			if( d == 0 )
//				return root;
//
//			root -= v/d;
//		}
//
//		return root;

		Polynomial deriv = new Polynomial(poly.size());
		derivative(poly,deriv);

		for( int i = 0; i < maxIterations; i++ ) {

			double v = poly.evaluate(root);
			double d = deriv.evaluate(root);

			if( d == 0 )
				return root;

			root -= v/d;
		}

		return root;
	}


	/**
	 * <p>
	 * Polynomial division. Computes both the quotient and the remainder.<br>
	 * <br>
	 * quotient = numerator/denominator<br>
	 * remainder = numerator % denominator
	 * </p>
	 *
	 * @param numerator Numerator in the division. Not modified.
	 * @param denominator Denominator in the division. Not modified.
	 * @param quotient Output quotient, Modified.
	 * @param remainder Output remainder. Modified.
	 */
	public static void divide( Polynomial numerator , Polynomial denominator , Polynomial quotient , Polynomial remainder  ) {
		int nn = numerator.size-1; int nd = denominator.size-1;

		while( nd >= 0 && denominator.c[nd] == 0 )
			nd -= 1;

		quotient.size = nn-nd+1;
		remainder.setTo(numerator);

		for( int k = nn-nd; k >= 0; k-- ) {
			double c = quotient.c[k] = remainder.c[nd+k]/denominator.c[nd];
			for( int j = k+nd; j >= k; j-- ) {
				remainder.c[j] -= c*denominator.c[j-k];
			}
		}

		// The remainder can't be larger than the denominator
		remainder.size = nd;
	}

	public static int countRealRoots( Polynomial poly ) {
		SturmSequence sturm = new SturmSequence(poly.size);

		sturm.initialize(poly);

		return sturm.countRealRoots(Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY);
	}

	public static PolynomialRoots createRootFinder( int maxCoefficients , int which ) {
		switch( which ) {
			case 0:
				FindRealRootsSturm sturm = new FindRealRootsSturm(maxCoefficients,Double.POSITIVE_INFINITY,1e-10,200);
				return new WrapRealRootsSturm(sturm);

			case 1:
				return new RootFinderCompanion();
		}
		throw new RuntimeException("Unknown algorithm");
	}

}